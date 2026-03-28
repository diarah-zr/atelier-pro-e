package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel()
	{
	    GestionPersonnel gestionPersonnel = new GestionPersonnel();
	    try
	    {
	       
	        String requeteRoot = "select numero_Employe, nom_Employe, prenom_Employe, " +
	                             "mail_Employe, password_Employe " +
	                             "from employe where numero_Ligue is null";
	        Statement instruction = connection.createStatement();
	        ResultSet rsRoot = instruction.executeQuery(requeteRoot);
	        if (rsRoot.next())
	        {
	            gestionPersonnel.addRoot(
	                rsRoot.getInt("numero_Employe"),
	                rsRoot.getString("nom_Employe"),
	                rsRoot.getString("prenom_Employe"),
	                rsRoot.getString("mail_Employe"),
	                rsRoot.getString("password_Employe"));
	        }
	        else
	        {
	            gestionPersonnel.addRoot("root", "toor");
	        }

	        
	        String requeteLigues = "select numero_Ligue, nom_Ligue from ligue";
	        ResultSet ligues = instruction.executeQuery(requeteLigues);
	        while (ligues.next())
	            gestionPersonnel.addLigue(
	                ligues.getInt("numero_Ligue"), 
	                ligues.getString("nom_Ligue"));

	        
	        String requeteEmployes = 
	            "select e.numero_Employe, e.nom_Employe, e.prenom_Employe, " +
	            "e.mail_Employe, e.password_Employe, e.date_arrivee, e.date_depart, " +
	            "e.numero_Ligue " +
	            "from employe e " +
	            "join ligue l on e.numero_Ligue = l.numero_Ligue " +  // ← JOINTURE
	            "where e.numero_Ligue is not null";
	        ResultSet employes = instruction.executeQuery(requeteEmployes);
	        while (employes.next())
	        {
	            int numeroLigue = employes.getInt("numero_Ligue");
	            Ligue ligue = gestionPersonnel.getLigues().stream()
	                .filter(l -> l.getId() == numeroLigue)
	                .findFirst()
	                .orElse(null);

	            if (ligue != null)
	            {
	                LocalDate dateArrivee = employes.getDate("date_arrivee") != null
	                    ? employes.getDate("date_arrivee").toLocalDate() : null;
	                LocalDate dateDepart = employes.getDate("date_depart") != null
	                    ? employes.getDate("date_depart").toLocalDate() : null;

	                ligue.addEmploye(
	                    employes.getInt("numero_Employe"),
	                    employes.getString("nom_Employe"),
	                    employes.getString("prenom_Employe"),
	                    employes.getString("mail_Employe"),
	                    employes.getString("password_Employe"),
	                    dateArrivee,
	                    dateDepart);
	            }
	        }
	    }
	    catch (SQLException | SauvegardeImpossible e)
	    {
	        System.out.println(e);
	    }
	    return gestionPersonnel;
	}
	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
				    "insert into ligue (nom_Ligue, numero_Admin) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
				instruction.setString(1, ligue.getNom());
				instruction.setInt(2, ligue.getAdministrateur().getId());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Employe employe) throws SauvegardeImpossible
	{
	    try
	    {
	    	PreparedStatement instruction = connection.prepareStatement(
	    		    "insert into employe (nom_Employe, prenom_Employe, mail_Employe, password_Employe, numero_Ligue) " +
	    		    "values (?, ?, ?, ?, ?)",
	    		    Statement.RETURN_GENERATED_KEYS);
	    		instruction.setString(1, employe.getNom());
	    		instruction.setString(2, employe.getPrenom());
	    		instruction.setString(3, employe.getMail()); 
	    		instruction.setString(4, employe.getPassword());
	    		if (employe.getLigue() != null)
	    		    instruction.setInt(5, employe.getLigue().getId());
	    		else
	    		    instruction.setNull(5, java.sql.Types.INTEGER);    
	        instruction.executeUpdate();
	        ResultSet id = instruction.getGeneratedKeys();
	        id.next();
	        return id.getInt(1);
	    }
	    
	    catch (SQLException exception)
	    
	    {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}
	@Override
	public void update(Ligue ligue) throws SauvegardeImpossible
	{
	    try
	    {
	        PreparedStatement instruction = connection.prepareStatement(
	            "update ligue set nom_Ligue = ?, numero_Admin = ? where numero_Ligue = ?");
	        instruction.setString(1, ligue.getNom());
	        instruction.setInt(2, ligue.getAdministrateur().getId());
	        instruction.setInt(3, ligue.getId());
	        instruction.executeUpdate();
	    }
	    catch (SQLException exception)
	    {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}
	@Override
	public void update(Employe employe) throws SauvegardeImpossible
	{
	    try
	    {
	        PreparedStatement instruction = connection.prepareStatement(
	            "update employe set nom_Employe = ?, prenom_Employe = ?, " +
	            "mail_Employe = ?, password_Employe = ?, " +
	            "date_arrivee = ?, date_depart = ? " +
	            "where numero_Employe = ?");
	        instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getPassword());
	        instruction.setObject(5, employe.getDateArrivee());
	        instruction.setObject(6, employe.getDateDepart());
	        instruction.setInt(7, employe.getId());
	        instruction.executeUpdate();
	    }
	    catch (SQLException exception)
	    {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}
}