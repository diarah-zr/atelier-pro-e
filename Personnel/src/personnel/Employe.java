package personnel;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Employé d'une ligue hébergée par la M2L. Certains peuvent 
 * être administrateurs des employés de leur ligue.
 * Un seul employé, rattaché à aucune ligue, est le root.
 * Il est impossible d'instancier directement un employé, 
 * il faut passer la méthode {@link Ligue#addEmploye addEmploye}.
 */

public class Employe implements Serializable, Comparable<Employe>
{
    private static final long serialVersionUID = 4795721718037994734L;
    private int id = -1;                         
    private String nom, prenom, password, mail;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;

    
    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom,
            String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
            throws SauvegardeImpossible
    {
        this(gestionPersonnel, -1, ligue, nom, prenom, mail, password, dateArrivee, dateDepart);
        this.id = gestionPersonnel.insert(this);
    }


    Employe(GestionPersonnel gestionPersonnel, int id, Ligue ligue, String nom, String prenom,
            String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
    {
        this.gestionPersonnel = gestionPersonnel;
        this.id = id;
        this.ligue = ligue;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.password = password;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
    }
    
    public int getId() { return id; }
	
	/**
	 * Retourne vrai ssi l'employé est administrateur de la ligue 
	 * passée en paramètre.
	 * @return vrai ssi l'employé est administrateur de la ligue 
	 * passée en paramètre.
	 * @param ligue la ligue pour laquelle on souhaite vérifier si this 
	 * est l'admininstrateur.
	 */
	
	public boolean estAdmin(Ligue ligue)
	{
		return ligue.getAdministrateur() == this;
	}
	
	/**
	 * Retourne vrai ssi l'employé est le root.
	 * @return vrai ssi l'employé est le root.
	 */
	
	public boolean estRoot()
	{
		return gestionPersonnel.getRoot() == this;
	}
	
	/**
	 * Retourne le nom de l'employé.
	 * @return le nom de l'employé. 
	 */
	
	public String getNom()
	{
		return nom;
	}

	/**
	 * Change le nom de l'employé.
	 * @param nom le nouveau nom.
	 */
	
	public void setNom(String nom)
	{
	    this.nom = nom;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	public void setPrenom(String prenom)
	{
	    this.prenom = prenom;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	public void setMail(String mail)
	{
	    this.mail = mail;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	public void setPassword(String password)
	{
	    this.password = password;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	public void setDateArrivee(LocalDate dateArrivee)
	{
	    if (dateArrivee != null && this.dateDepart != null && this.dateDepart.isBefore(dateArrivee))
	        throw new ExceptionsEmploye.DatesIncoherentes();
	    this.dateArrivee = dateArrivee;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	public void setDateDepart(LocalDate dateDepart)
	{
	    if (this.dateArrivee != null && dateDepart != null && dateDepart.isBefore(this.dateArrivee))
	        throw new ExceptionsEmploye.DatesIncoherentes();
	    this.dateDepart = dateDepart;
	    try { gestionPersonnel.update(this); }
	    catch (SauvegardeImpossible e) { System.out.println("Erreur mise à jour employé : " + e.getMessage()); }
	}

	/**
	 * Retourne vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @return vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @param password le password auquel comparer celui de l'employé.
	 */
	
	public boolean checkPassword(String password)
	{
		return this.password.equals(password);
	}

	/**
	 * Change le password de l'employé.
	 * @param password le nouveau password de l'employé. 
	 */
	
	

	public String getPassword()
	{
		return password;
	}

	public Ligue getLigue()
	{
		return ligue;
	}
	
	
	
	
	
	public LocalDate getDateDepart()
	{
		return dateDepart;
	}
	
	

	/**
	 * Supprime l'employé. Si celui-ci est un administrateur, le root
	 * récupère les droits d'administration sur sa ligue.
	 */
	
	public void remove()
	{
	    Employe root = gestionPersonnel.getRoot();
	    if (this != root)
	    {
	        if (estAdmin(getLigue()))
	            getLigue().setAdministrateur(root);
	        getLigue().remove(this);
	        try { gestionPersonnel.delete(this); }  
	        catch (SauvegardeImpossible e) { System.out.println("Erreur suppression employé : " + e.getMessage()); }
	    }
	    else
	        throw new ImpossibleDeSupprimerRoot();
	}

	@Override
	public int compareTo(Employe autre)
	{
	    int cmp = getNom().compareTo(autre.getNom());
	    if (cmp != 0)
	        return cmp;
	    return getPrenom().compareTo(autre.getPrenom()); // ← corrigé
	}
	
	@Override
	public String toString()
	{
		String res = nom + " " + prenom + " " + mail + " (";
		if (estRoot())
			res += "super-utilisateur";
		else
			res += ligue.toString();
		return res + ")";
	}


	public String getPrenom() {
		// TODO Auto-generated method stub
		return prenom;
	}


	public String getMail() {
		// TODO Auto-generated method stub
		return mail;
	}
	public LocalDate getDateArrivee()
	{
	    return dateArrivee;
	}
}
