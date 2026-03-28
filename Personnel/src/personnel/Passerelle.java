package personnel;

public interface Passerelle 
{
	public GestionPersonnel getGestionPersonnel();
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	public int insert(Ligue ligue) throws SauvegardeImpossible;
	public int insert (Employe employe) throws SauvegardeImpossible;
<<<<<<< HEAD
}
=======
}
>>>>>>> d3295f76782da4334aebf3c7cc0b1579a1907e13
