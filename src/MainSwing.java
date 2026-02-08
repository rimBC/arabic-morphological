import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import structures.ABR;
import structures.HashTable;
import utils.ChargeurDonnees;
import utils.MoteurMorphologique;
import utils.MoteurMorphologique.ResultatValidation;
import utils.MoteurMorphologique.ResultatDecomposition;
import models.RacineNode;
import java.util.List;

/**
 * Interface graphique Swing (Java standard, pas de dépendances externes)
 * Plus simple que JavaFX mais toujours professionnelle
 */
public class MainSwing extends JFrame {

    private ABR arbreRacines;
    private HashTable tableSchemes;
    private MoteurMorphologique moteur;

    private JTextArea outputArea;
    private JTextField racineField, motField, racineValField, motDecField, rechField;
    private JComboBox<String> schemeCombo;

    private static final String FICHIER_RACINES = "data/racines.txt";

    public MainSwing() {
        super("🔍 Moteur Morphologique Arabe");

        // Initialiser les données
        initialiserDonnees();

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        // Créer l'interface
        creerInterface();

        afficherMessage("✓ Système initialisé avec " + arbreRacines.getTaille() +
                " racines et " + tableSchemes.getTaille() + " schèmes");
    }

    private void initialiserDonnees() {
        arbreRacines = new ABR();
        tableSchemes = new HashTable();

        ChargeurDonnees.creerFichierExemple(FICHIER_RACINES);
        ChargeurDonnees.chargerRacinesDepuisFichier(FICHIER_RACINES, arbreRacines);
        ChargeurDonnees.initialiserSchemes(tableSchemes);

        moteur = new MoteurMorphologique(arbreRacines, tableSchemes);
    }

    private void creerInterface() {
        // Layout principal
        setLayout(new BorderLayout(10, 10));

        // En-tête
        add(creerEntete(), BorderLayout.NORTH);

        // Onglets centraux
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("✨ Génération", creerPanneauGeneration());
        tabbedPane.addTab("✓ Validation", creerPanneauValidation());
        tabbedPane.addTab("🔍 Décomposition", creerPanneauDecomposition());
        tabbedPane.addTab("🔎 Recherche", creerPanneauRecherche());
        tabbedPane.addTab("📊 Statistiques", creerPanneauStatistiques());

        add(tabbedPane, BorderLayout.CENTER);

        // Zone de sortie en bas
        outputArea = new JTextArea(8, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setBorder(new LineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(new TitledBorder("Résultats"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel creerEntete() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(102, 126, 234));
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("🔍 Moteur de Recherche Morphologique Arabe");
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Système de Génération et Validation de Dérivés");
        sousTitre.setFont(new Font("Arial", Font.PLAIN, 14));
        sousTitre.setForeground(Color.WHITE);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(sousTitre);

        return panel;
    }

    private JPanel creerPanneauGeneration() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        JLabel titre = new JLabel("Générer des Mots Dérivés");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titre, gbc);

        // Racine
        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Racine (3 lettres):"), gbc);

        racineField = new JTextField(15);
        racineField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(racineField, gbc);

        // Schème
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Schème:"), gbc);

        schemeCombo = new JComboBox<>();
        List<String> schemes = tableSchemes.getTousLesNoms();
        for (String s : schemes) {
            schemeCombo.addItem(s);
        }
        gbc.gridx = 1;
        panel.add(schemeCombo, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton genererUnBtn = creerBouton("Générer avec Schème", new Color(76, 175, 80));
        genererUnBtn.addActionListener(e -> genererMotSpecifique());

        JButton genererTousBtn = creerBouton("Générer Tous", new Color(33, 150, 243));
        genererTousBtn.addActionListener(e -> genererTousDerivees());

        buttonPanel.add(genererUnBtn);
        buttonPanel.add(genererTousBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel creerPanneauValidation() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titre = new JLabel("Valider un Mot Morphologiquement");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titre, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Mot à valider:"), gbc);

        motField = new JTextField(15);
        motField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(motField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Racine supposée:"), gbc);

        racineValField = new JTextField(15);
        racineValField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(racineValField, gbc);

        JButton validerBtn = creerBouton("Valider", new Color(255, 152, 0));
        validerBtn.addActionListener(e -> validerMot());

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(validerBtn, gbc);

        return panel;
    }

    private JPanel creerPanneauDecomposition() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titre = new JLabel("Décomposer un Mot");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titre, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Mot à décomposer:"), gbc);

        motDecField = new JTextField(15);
        motDecField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(motDecField, gbc);

        JButton decomposerBtn = creerBouton("Décomposer", new Color(156, 39, 176));
        decomposerBtn.addActionListener(e -> decomposerMot());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(decomposerBtn, gbc);

        return panel;
    }

    private JPanel creerPanneauRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titre = new JLabel("Rechercher une Racine");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titre, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Racine:"), gbc);

        rechField = new JTextField(15);
        rechField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(rechField, gbc);

        JButton rechercherBtn = creerBouton("Rechercher", new Color(0, 188, 212));
        rechercherBtn.addActionListener(e -> rechercherRacine());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(rechercherBtn, gbc);

        return panel;
    }

    private JPanel creerPanneauStatistiques() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titre = new JLabel("Statistiques du Système");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titre, BorderLayout.NORTH);

        JTextArea statArea = new JTextArea();
        statArea.setEditable(false);
        statArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(statArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton rafraichirBtn = creerBouton("Rafraîchir", new Color(96, 125, 139));
        rafraichirBtn.addActionListener(e -> {
            StringBuilder stats = new StringBuilder();
            stats.append("═══════════════════════════════════════════\n");
            stats.append("  STATISTIQUES DU SYSTÈME\n");
            stats.append("═══════════════════════════════════════════\n\n");

            stats.append("📚 ARBRE AVL:\n");
            stats.append("  - Nombre de racines: ").append(arbreRacines.getTaille()).append("\n\n");

            stats.append("🔧 TABLE DE HACHAGE:\n");
            stats.append("  - Nombre de schèmes: ").append(tableSchemes.getTaille()).append("\n\n");

            stats.append("📖 LISTE DES RACINES:\n");
            List<String> racines = arbreRacines.getToutesLesRacines();
            int count = 1;
            for (String r : racines) {
                stats.append("  ").append(count++).append(". ").append(r).append("\n");
            }

            statArea.setText(stats.toString());
        });

        // Charger au démarrage
        rafraichirBtn.doClick();

        panel.add(rafraichirBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void genererMotSpecifique() {
        String racine = racineField.getText().trim();
        String scheme = (String) schemeCombo.getSelectedItem();

        if (racine.isEmpty() || scheme == null) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String motGenere = moteur.genererMotDerive(racine, scheme);

        if (motGenere != null) {
            afficherMessage("\n✨ Mot généré:\n" +
                    "Racine: " + racine + "\n" +
                    "Schème: " + scheme + "\n" +
                    "Résultat: " + motGenere);
        }
    }

    private void genererTousDerivees() {
        String racine = racineField.getText().trim();

        if (racine.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une racine",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> derivees = moteur.genererTousLesDerivees(racine);

        if (!derivees.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n📚 Tous les dérivés de: ").append(racine).append("\n");
            sb.append("─".repeat(60)).append("\n");
            for (String d : derivees) {
                sb.append(d).append("\n");
            }
            sb.append("─".repeat(60)).append("\n");
            sb.append("Total: ").append(derivees.size()).append(" mots");
            afficherMessage(sb.toString());
        }
    }

    private void validerMot() {
        String mot = motField.getText().trim();
        String racine = racineValField.getText().trim();

        if (mot.isEmpty() || racine.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResultatValidation resultat = moteur.validerMot(mot, racine);
        afficherMessage("\n📋 Résultat de la validation:\n" +
                "Mot: " + mot + "\n" +
                "Racine: " + racine + "\n" +
                resultat.toString());
    }

    private void decomposerMot() {
        String mot = motDecField.getText().trim();

        if (mot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un mot",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        afficherMessage("🔍 Analyse en cours...");
        ResultatDecomposition resultat = moteur.decomposerMot(mot);
        afficherMessage("\n📋 Résultat:\n" +
                "Mot: " + mot + "\n" +
                resultat.toString());
    }

    private void rechercherRacine() {
        String racine = rechField.getText().trim();

        if (racine.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une racine",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long debut = System.nanoTime();
        RacineNode noeud = arbreRacines.rechercher(racine);
        long fin = System.nanoTime();
        double tempsMs = (fin - debut) / 1_000_000.0;

        if (noeud != null) {
            afficherMessage("\n🔍 Résultat:\n" +
                    "✓ TROUVÉE!\n" +
                    noeud.toString() + "\n" +
                    "⏱ Temps: " + String.format("%.6f", tempsMs) + " ms");
        } else {
            afficherMessage("\n🔍 Résultat:\n" +
                    "✗ NON TROUVÉE\n" +
                    "⏱ Temps: " + String.format("%.6f", tempsMs) + " ms");
        }
    }

    private void afficherMessage(String message) {
        outputArea.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainSwing frame = new MainSwing();
            frame.setVisible(true);
        });
    }
}