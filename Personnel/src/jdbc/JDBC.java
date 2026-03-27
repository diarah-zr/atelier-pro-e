package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	        
	        String requeteRoot = "select numero_Employe, nom_Employe, prenom_Employe, mail_Employe, password_Employe " +
	                             "from employe where numero_Ligue is null";
	        Statement instruction = connection.createStatement();
	        ResultSet rsRoot = instruction.executeQuery(requeteRoot);
	        if (rsRoot.next())
	        {
	            
	            gestionPersonnel.setRoot(new Employe(
	                gestionPersonnel,
	                rsRoot.getInt("numero_Employe"),
	                null,
	                rsRoot.getString("nom_Employe"),
	                rsRoot.getString("prenom_Employe"),
	                rsRoot.getString("mail_Employe"),
	                rsRoot.getString("password_Employe"),
	                null, null));
	        }
	        else
	        {
	           
	            gestionPersonnel.addRoot("root", "toor");
	        }

	        
	        String requeteLigues = "select numero_Ligue, nom_Ligue from ligue";
	        ResultSet ligues = instruction.executeQuery(requeteLigues);
	        while (ligues.next())
	            gestionPersonnel.addLigue(ligues.getInt("numero_Ligue"), ligues.getString("nom_Ligue"));
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
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
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
}
