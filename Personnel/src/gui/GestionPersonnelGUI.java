package gui;

import personnel.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class GestionPersonnelGUI extends JFrame
{
    private GestionPersonnel gestionPersonnel;

    private JList<Ligue>   listeLigues;
    private JList<Employe> listeEmployes;
    private DefaultListModel<Ligue>   modeleLigues;
    private DefaultListModel<Employe> modeleEmployes;

    private JButton boutonAjouterLigue;
    private JButton boutonSupprimerLigue;
    private JButton boutonRenommerLigue;
    private JButton boutonAjouterEmploye;
    private JButton boutonSupprimerEmploye;
    private JButton boutonModifierEmploye;
    private JButton boutonChangerAdmin;

    private JLabel labelAdmin;

    
    public GestionPersonnelGUI(GestionPersonnel gestionPersonnel)
    {
        this.gestionPersonnel = gestionPersonnel;
        setTitle("Gestion du personnel des ligues");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setContentPane(creerPanneauPrincipal());
        actualiserLigues();
    }


    private JPanel creerPanneauPrincipal()
    {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));
        panneau.add(creerPanneauLigues(),   BorderLayout.WEST);
        panneau.add(creerPanneauEmployes(), BorderLayout.CENTER);
        return panneau;
    }

    private JPanel creerPanneauLigues()
    {
        JPanel panneau = new JPanel(new BorderLayout(5, 5));
        panneau.setBorder(BorderFactory.createTitledBorder("Ligues"));
        panneau.setPreferredSize(new Dimension(280, 0));

        modeleLigues = new DefaultListModel<>();
        listeLigues  = creerListeLigues();

        panneau.add(new JScrollPane(listeLigues), BorderLayout.CENTER);
        panneau.add(creerPanneauBoutonsLigues(),  BorderLayout.SOUTH);
        return panneau;
    }

    private JPanel creerPanneauEmployes()
    {
        JPanel panneau = new JPanel(new BorderLayout(5, 5));
        panneau.setBorder(BorderFactory.createTitledBorder("Employés"));

        modeleEmployes = new DefaultListModel<>();
        listeEmployes  = creerListeEmployes();

        labelAdmin = creerLabelAdmin();

        panneau.add(labelAdmin,                    BorderLayout.NORTH);
        panneau.add(new JScrollPane(listeEmployes), BorderLayout.CENTER);
        panneau.add(creerPanneauBoutonsEmployes(),  BorderLayout.SOUTH);
        return panneau;
    }

    private JPanel creerPanneauBoutonsLigues()
    {
        JPanel panneau = new JPanel(new GridLayout(3, 1, 4, 4));
        boutonAjouterLigue   = creerBoutonAjouterLigue();
        boutonSupprimerLigue = creerBoutonSupprimerLigue();
        boutonRenommerLigue  = creerBoutonRenommerLigue();
        panneau.add(boutonAjouterLigue);
        panneau.add(boutonRenommerLigue);
        panneau.add(boutonSupprimerLigue);
        return panneau;
    }

    private JPanel creerPanneauBoutonsEmployes()
    {
        JPanel panneau = new JPanel(new GridLayout(1, 4, 4, 4));
        boutonAjouterEmploye  = creerBoutonAjouterEmploye();
        boutonModifierEmploye = creerBoutonModifierEmploye();
        boutonSupprimerEmploye = creerBoutonSupprimerEmploye();
        boutonChangerAdmin    = creerBoutonChangerAdmin();
        panneau.add(boutonAjouterEmploye);
        panneau.add(boutonModifierEmploye);
        panneau.add(boutonSupprimerEmploye);
        panneau.add(boutonChangerAdmin);
        return panneau;
    }


    private JList<Ligue> creerListeLigues()
    {
        JList<Ligue> liste = new JList<>(modeleLigues);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.addListSelectionListener(e -> surSelectionLigue());
        return liste;
    }

    private JList<Employe> creerListeEmployes()
    {
        JList<Employe> liste = new JList<>(modeleEmployes);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return liste;
    }

    private JLabel creerLabelAdmin()
    {
        JLabel label = new JLabel("Administrateur : (aucune ligue sélectionnée)");
        label.setBorder(new EmptyBorder(4, 4, 4, 4));
        return label;
    }

    private JButton creerBoutonAjouterLigue()
    {
        JButton bouton = new JButton("Ajouter");
        bouton.addActionListener(e -> surAjouterLigue());
        return bouton;
    }

    private JButton creerBoutonSupprimerLigue()
    {
        JButton bouton = new JButton("Supprimer");
        bouton.addActionListener(e -> surSupprimerLigue());
        return bouton;
    }

    private JButton creerBoutonRenommerLigue()
    {
        JButton bouton = new JButton("Renommer");
        bouton.addActionListener(e -> surRenommerLigue());
        return bouton;
    }

    private JButton creerBoutonAjouterEmploye()
    {
        JButton bouton = new JButton("Ajouter");
        bouton.addActionListener(e -> surAjouterEmploye());
        return bouton;
    }

    private JButton creerBoutonModifierEmploye()
    {
        JButton bouton = new JButton("Modifier");
        bouton.addActionListener(e -> surModifierEmploye());
        return bouton;
    }

    private JButton creerBoutonSupprimerEmploye()
    {
        JButton bouton = new JButton("Supprimer");
        bouton.addActionListener(e -> surSupprimerEmploye());
        return bouton;
    }

    private JButton creerBoutonChangerAdmin()
    {
        JButton bouton = new JButton("Définir admin");
        bouton.addActionListener(e -> surChangerAdmin());
        return bouton;
    }


    private void surSelectionLigue()
    {
        Ligue ligue = listeLigues.getSelectedValue();
        if (ligue != null)
        {
            actualiserEmployes(ligue);
            labelAdmin.setText("Administrateur : " + ligue.getAdministrateur());
        }
    }

    private void surAjouterLigue()
    {
        String nom = JOptionPane.showInputDialog(this, "Nom de la ligue :");
        if (nom != null && !nom.isBlank())
        {
            try
            {
                gestionPersonnel.addLigue(nom);
                actualiserLigues();
            }
            catch (SauvegardeImpossible e)
            {
                afficherErreur("Impossible d'ajouter la ligue : " + e.getMessage());
            }
        }
    }

    private void surSupprimerLigue()
    {
        Ligue ligue = listeLigues.getSelectedValue();
        if (ligue == null) { afficherErreur("Sélectionnez une ligue."); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer la ligue « " + ligue.getNom() + " » et tous ses employés ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION)
        {
            ligue.remove();
            actualiserLigues();
            modeleEmployes.clear();
            labelAdmin.setText("Administrateur : (aucune ligue sélectionnée)");
        }
    }

    private void surRenommerLigue()
    {
        Ligue ligue = listeLigues.getSelectedValue();
        if (ligue == null) { afficherErreur("Sélectionnez une ligue."); return; }

        String nom = JOptionPane.showInputDialog(this, "Nouveau nom :", ligue.getNom());
        if (nom != null && !nom.isBlank())
        {
            ligue.setNom(nom);
            actualiserLigues();
        }
    }

    private void surAjouterEmploye()
    {
        Ligue ligue = listeLigues.getSelectedValue();
        if (ligue == null) { afficherErreur("Sélectionnez une ligue."); return; }

        String nom    = JOptionPane.showInputDialog(this, "Nom :");
        if (nom == null || nom.isBlank()) return;
        String prenom = JOptionPane.showInputDialog(this, "Prénom :");
        if (prenom == null || prenom.isBlank()) return;
        String mail   = JOptionPane.showInputDialog(this, "Mail :");
        if (mail == null) return;
        String pass   = JOptionPane.showInputDialog(this, "Password :");
        if (pass == null) return;

        try
        {
            ligue.addEmploye(nom, prenom, mail, pass, null, null);
            actualiserEmployes(ligue);
        }
        catch (SauvegardeImpossible e)
        {
            afficherErreur("Impossible d'ajouter l'employé : " + e.getMessage());
        }
    }

    private void surModifierEmploye()
    {
        Employe employe = listeEmployes.getSelectedValue();
        if (employe == null) { afficherErreur("Sélectionnez un employé."); return; }

        String nom    = JOptionPane.showInputDialog(this, "Nouveau nom :",    employe.getNom());
        if (nom != null && !nom.isBlank()) employe.setNom(nom);

        String prenom = JOptionPane.showInputDialog(this, "Nouveau prénom :", employe.getPrenom());
        if (prenom != null && !prenom.isBlank()) employe.setPrenom(prenom);

        String mail   = JOptionPane.showInputDialog(this, "Nouveau mail :",   employe.getMail());
        if (mail != null) employe.setMail(mail);

        Ligue ligue = listeLigues.getSelectedValue();
        actualiserEmployes(ligue);
    }

    private void surSupprimerEmploye()
    {
        Employe employe = listeEmployes.getSelectedValue();
        if (employe == null) { afficherErreur("Sélectionnez un employé."); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer " + employe.getNom() + " " + employe.getPrenom() + " ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION)
        {
            employe.remove();
            Ligue ligue = listeLigues.getSelectedValue();
            actualiserEmployes(ligue);
        }
    }

    private void surChangerAdmin()
    {
        Employe employe = listeEmployes.getSelectedValue();
        if (employe == null) { afficherErreur("Sélectionnez un employé."); return; }

        Ligue ligue = listeLigues.getSelectedValue();
        ligue.setAdministrateur(employe);
        labelAdmin.setText("Administrateur : " + employe);
        JOptionPane.showMessageDialog(this,
            employe.getNom() + " " + employe.getPrenom() + " est maintenant administrateur.");
    }


    private void actualiserLigues()
    {
        modeleLigues.clear();
        for (Ligue l : gestionPersonnel.getLigues())
            modeleLigues.addElement(l);
    }

    private void actualiserEmployes(Ligue ligue)
    {
        modeleEmployes.clear();
        for (Employe e : ligue.getEmployes())
            modeleEmployes.addElement(e);
        labelAdmin.setText("Administrateur : " + ligue.getAdministrateur());
    }

    private void afficherErreur(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args)
    {
        GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();

        String password = JOptionPane.showInputDialog(null, "Password :");
        if (password == null || !gp.getRoot().checkPassword(password))
        {
            JOptionPane.showMessageDialog(null, "Password incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        SwingUtilities.invokeLater(() ->
        {
            GestionPersonnelGUI gui = new GestionPersonnelGUI(gp);
            gui.setVisible(true);
        });
    }
}