package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.HashMap;
import java.util.Map;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;

public class EmployeConsole 
{
    // Cache : un menu par employé pour éviter la recréation
    private Map<Employe, Menu> menus = new HashMap<>();

    Menu editerEmploye(Employe employe)
    {
        if (menus.containsKey(employe))
            return menus.get(employe);

        Menu menu = new Menu("Modifier " + employe.getNom(), "m");
        menu.add(afficher(employe));
        menu.add(changerNom(employe));
        menu.add(changerPrenom(employe));
        menu.add(changerMail(employe));
        menu.add(changerPassword(employe));
        menu.addBack("q");
        menus.put(employe, menu);
        return menu;
    }

    private Option afficher(Employe employe)
    {
        return new Option("Afficher l'employé", "l", () -> System.out.println(employe));
    }

	private Option changerNom(final Employe employe)
    {
        return new Option("Changer le nom", "n", 
                () -> employe.setNom(getString("Nouveau nom : ")));
    }
    
    private Option changerPrenom(final Employe employe)
    {
        return new Option("Changer le prénom", "p", 
                () -> employe.setPrenom(getString("Nouveau prénom : ")));
    }
    
    private Option changerMail(final Employe employe)
    {
        return new Option("Changer le mail", "e", 
                () -> employe.setMail(getString("Nouveau mail : ")));
    }
    
    private Option changerPassword(final Employe employe)
    {
        return new Option("Changer le password", "x", 
                () -> employe.setPassword(getString("Nouveau password : ")));
    }
}