import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import structures.ABR;
import structures.HashTable;
import utils.ChargeurDonnees;
import utils.MoteurMorphologique;
import utils.MoteurMorphologique.ResultatValidation;
import utils.MoteurMorphologique.ResultatDecomposition;
import models.RacineNode;
import models.Scheme;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Interface graphique Swing moderne inspirée du design React
 * Avec gradients, animations douces et design professionnel
 * VERSION CORRIGÉE - Affichage des résultats fonctionnel
 */
public class MainSwing extends JFrame {

    private ABR arbreRacines;
    private HashTable tableSchemes;
    private MoteurMorphologique moteur;

    // Zone de résultats par panneau
    private JTextArea outputAreaGeneration;
    private JTextArea outputAreaValidation;
    private JTextArea outputAreaSchemes;

    private JComboBox<String> racineCombo; // Changé en ComboBox
    private JTextField motField, racineValField, motDecField, rechField, rechSchemeField, nouvelleRacineField;
    private JComboBox<String> schemeCombo;

    private static final String FICHIER_RACINES = "data/racines.txt";

    // Couleurs du thème (inspirées du design React)
    private static final Color PRIMARY_GREEN = new Color(6, 95, 70);
    private static final Color PRIMARY_GREEN_LIGHT = new Color(16, 185, 129);
    private static final Color ACCENT_AMBER = new Color(245, 158, 11);
    private static final Color BG_LIGHT = new Color(236, 253, 245);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color TEXT_DARK = new Color(17, 24, 39);
    private static final Color TEXT_GRAY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(209, 250, 229);

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainSwing() {
        super("محرك البحث المورفولوجي العربي - Moteur Morphologique Arabe");

        // Initialiser les données
        initialiserDonnees();

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null);

        // Style moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créer l'interface
        creerInterface();
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
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_LIGHT);

        // En-tête avec gradient
        add(creerEnteteModerne(), BorderLayout.NORTH);

        // Navigation moderne
        add(creerNavigationModerne(), BorderLayout.WEST);

        // Contenu central avec CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentPanel.add(creerPanneauGenerationModerne(), "generation");
        contentPanel.add(creerPanneauValidationModerne(), "validation");
        contentPanel.add(creerPanneauRacinesModerne(), "racines");
        contentPanel.add(creerPanneauSchemesModerne(), "schemes");
        contentPanel.add(creerPanneauStatistiquesModerne(), "stats");

        add(contentPanel, BorderLayout.CENTER);

        // Footer moderne
        add(creerFooter(), BorderLayout.SOUTH);
    }

    private JPanel creerEnteteModerne() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient vert émeraude
                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_GREEN,
                        getWidth(), 0, new Color(4, 120, 87)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Cercle décoratif
                g2d.setColor(new Color(245, 158, 11, 25));
                g2d.fillOval(getWidth() - 200, -100, 300, 300);
            }
        };

        panel.setPreferredSize(new Dimension(0, 140));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Titre arabe
        JLabel titreArabe = new JLabel("محرك البحث المورفولوجي العربي");
        titreArabe.setFont(new Font("Arial Unicode MS", Font.BOLD, 36));
        titreArabe.setForeground(Color.WHITE);
        titreArabe.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sous-titre français
        JLabel sousTitre = new JLabel("Moteur de Recherche Morphologique et Générateur de Dérivation Arabe");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sousTitre.setForeground(new Color(255, 255, 255, 230));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(titreArabe);
        panel.add(Box.createVerticalStrut(10));
        panel.add(sousTitre);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel creerNavigationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR),
                new EmptyBorder(30, 15, 30, 15)
        ));

        String[][] tabs = {
                {"🔄", "التوليد | Génération", "generation"},
                {"✓", "التحقق | Validation", "validation"},
                {"📚", "الجذور | Racines", "racines"},
                {"🔧", "الأوزان | Schèmes", "schemes"},
                {"📊", "الإحصائيات | Stats", "stats"}
        };

        ButtonGroup group = new ButtonGroup();
        boolean first = true;

        for (String[] tab : tabs) {
            JToggleButton btn = creerBoutonNavigation(tab[0], tab[1], tab[2]);
            group.add(btn);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));

            if (first) {
                btn.setSelected(true);
                first = false;
            }
        }

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JToggleButton creerBoutonNavigation(String icon, String text, String cardName) {
        JToggleButton btn = new JToggleButton("<html><div style='padding:5px'>" +
                "<span style='font-size:18px'>" + icon + "</span> " +
                "<span style='font-size:11px'>" + text + "</span></div></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected()) {
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY_GREEN, getWidth(), 0,
                            new Color(4, 120, 87));
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(236, 253, 245));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_DARK);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(230, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            // Effacer les zones de résultats quand on change d'onglet
            clearAllOutputs();
        });

        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setForeground(Color.WHITE);
            } else {
                btn.setForeground(TEXT_DARK);
            }
        });

        return btn;
    }

    private JPanel creerPanneauGenerationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHT);

        // Carte principale
        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // En-tête de la carte
        JPanel header = creerEnteteSection("🔄", "توليد الكلمات المشتقة | Génération de mots dérivés");
        card.add(header);
        card.add(Box.createVerticalStrut(25));

        // Champ racine - ComboBox au lieu de TextField
        card.add(creerLabelModerne("الجذر (Racine trilitère)"));
        card.add(Box.createVerticalStrut(8));
        racineCombo = creerComboBoxModerne();

        // Remplir le ComboBox avec toutes les racines disponibles
        racineCombo.addItem("-- اختر الجذر --");
        List<String> racines = arbreRacines.getToutesLesRacines();
        for (String r : racines) {
            racineCombo.addItem(r);
        }
        racineCombo.setToolTipText("اختر جذرا | Choisissez une racine");
        card.add(racineCombo);
        card.add(Box.createVerticalStrut(20));

        // Sélecteur de schème
        card.add(creerLabelModerne("الوزن (Schème morphologique)"));
        card.add(Box.createVerticalStrut(8));
        schemeCombo = creerComboBoxModerne();
        List<String> schemes = tableSchemes.getTousLesNoms();
        schemeCombo.addItem("-- اختر الوزن --");
        for (String s : schemes) {
            schemeCombo.addItem(s);
        }
        card.add(schemeCombo);
        card.add(Box.createVerticalStrut(25));

        // Boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton genererBtn = creerBoutonPrimaire(" توليد | Générer");
        genererBtn.addActionListener(e -> genererMotSpecifique());

        JButton tousBtn = creerBoutonSecondaire(" توليد الكل | Tout générer");
        tousBtn.addActionListener(e -> genererTousDerivees());

        buttonPanel.add(genererBtn);
        buttonPanel.add(tousBtn);
        card.add(buttonPanel);

        panel.add(card);
        panel.add(Box.createVerticalStrut(20));

        // Zone de résultats POUR CE PANNEAU
        outputAreaGeneration = creerZoneResultats();
        JScrollPane scroll = new JScrollPane(outputAreaGeneration);
        scroll.setBorder(null);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.add(scroll);

        return panel;
    }

    private JPanel creerPanneauValidationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHT);

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("✓", "التحقق المورفولوجي | Validation morphologique");
        card.add(header);
        card.add(Box.createVerticalStrut(25));

        card.add(creerLabelModerne("الكلمة (Mot à valider)"));
        card.add(Box.createVerticalStrut(8));
        motField = creerChampTexteArabe();
        motField.setToolTipText("مثال: كاتب");
        card.add(motField);
        card.add(Box.createVerticalStrut(20));

        card.add(creerLabelModerne("الجذر (Racine)"));
        card.add(Box.createVerticalStrut(8));
        racineValField = creerChampTexteArabe();
        racineValField.setToolTipText("مثال: كتب");
        card.add(racineValField);
        card.add(Box.createVerticalStrut(25));

        JButton validerBtn = creerBoutonPrimaire(" تحقق | Valider");
        validerBtn.addActionListener(e -> validerMot());
        validerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.add(validerBtn);

        panel.add(card);
        panel.add(Box.createVerticalStrut(20));

        // Zone de résultats POUR CE PANNEAU
        outputAreaValidation = creerZoneResultats();
        JScrollPane scroll = new JScrollPane(outputAreaValidation);
        scroll.setBorder(null);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.add(scroll);

        return panel;
    }

    private JPanel creerPanneauRacinesModerne() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG_LIGHT);

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("📚", "إدارة الجذور | Gestion des racines");
        card.add(header);
        card.add(Box.createVerticalStrut(20));

        // Ajouter une racine
        JPanel addPanel = new JPanel(new BorderLayout(10, 0));
        addPanel.setBackground(Color.WHITE);
        addPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        nouvelleRacineField = creerChampTexteArabe();
        nouvelleRacineField.setToolTipText("جذر جديد");
        addPanel.add(nouvelleRacineField, BorderLayout.CENTER);

        JButton ajouterBtn = creerBoutonPrimaire(" إضافة");
        ajouterBtn.setPreferredSize(new Dimension(150, 50));
        ajouterBtn.addActionListener(e -> ajouterRacine());
        addPanel.add(ajouterBtn, BorderLayout.EAST);

        card.add(addPanel);

        panel.add(card, BorderLayout.NORTH);

        // Grille de racines
        JPanel gridPanel = new JPanel(new GridLayout(0, 5, 15, 15));
        gridPanel.setBackground(BG_LIGHT);

        List<String> racines = arbreRacines.getToutesLesRacines();
        for (String racine : racines) {
            gridPanel.add(creerCarteRacine(racine));
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel creerCarteRacine(String racine) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient subtil
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(236, 253, 245),
                        getWidth(), getHeight(), new Color(254, 243, 199)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Bordure
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 15, 20, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(150, 120));

        JLabel racineLabel = new JLabel(racine);
        racineLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 32));
        racineLabel.setForeground(PRIMARY_GREEN);
        racineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = new JLabel("0 مشتق");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(TEXT_GRAY);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(racineLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(countLabel);
        card.add(Box.createVerticalGlue());

        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_GREEN_LIGHT, 2, true),
                        new EmptyBorder(19, 14, 19, 14)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(20, 15, 20, 15));
            }
        });

        return card;
    }

    private JPanel creerPanneauSchemesModerne() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHT);

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("🔧", "الأوزان المورفولوجية | Schèmes morphologiques");
        card.add(header);
        card.add(Box.createVerticalStrut(20));

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        rechSchemeField = creerChampTexteArabe();
        rechSchemeField.setToolTipText("ابحث عن وزن | Rechercher un schème");
        searchPanel.add(rechSchemeField, BorderLayout.CENTER);

        JButton rechercherBtn = creerBoutonPrimaire(" بحث | Rechercher");
        rechercherBtn.setPreferredSize(new Dimension(180, 50));
        rechercherBtn.addActionListener(e -> rechercherScheme());
        searchPanel.add(rechercherBtn, BorderLayout.EAST);

        card.add(searchPanel);
        card.add(Box.createVerticalStrut(15));

        // Zone de résultats de recherche POUR CE PANNEAU
        outputAreaSchemes = creerZoneResultats();
        outputAreaSchemes.setPreferredSize(new Dimension(0, 80));
        card.add(outputAreaSchemes);

        panel.add(card);
        panel.add(Box.createVerticalStrut(20));

        // Grille de schèmes
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setBackground(BG_LIGHT);

        List<String> schemes = tableSchemes.getTousLesNoms();
        for (String scheme : schemes) {
            gridPanel.add(creerCarteScheme(scheme));
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll);

        return panel;
    }

    private JPanel creerCarteScheme(String scheme) {
        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Récupérer les informations du schème depuis la table de hachage
        Scheme schemeObj = tableSchemes.rechercher(scheme);
        String type = (schemeObj != null) ? schemeObj.getType().toString() : "N/A";
        String description = (schemeObj != null) ? schemeObj.getDescription() : "Aucune description disponible";

        JLabel schemeLabel = new JLabel(scheme);
        schemeLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 28));
        schemeLabel.setForeground(PRIMARY_GREEN);
        schemeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel("Type: " + type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        typeLabel.setForeground(PRIMARY_GREEN_LIGHT);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='width:280px'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(schemeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descLabel);

        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_GREEN_LIGHT, 2, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 2, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    private JPanel creerPanneauStatistiquesModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHT);

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("📊", "الإحصائيات | Statistiques du système");
        card.add(header);

        panel.add(card);
        panel.add(Box.createVerticalStrut(20));

        // Grille de statistiques
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        statsGrid.setBackground(BG_LIGHT);

        statsGrid.add(creerCarteStat("جذور | Racines", String.valueOf(arbreRacines.getTaille()), PRIMARY_GREEN));
        statsGrid.add(creerCarteStat("أوزان | Schèmes", String.valueOf(tableSchemes.getTaille()), ACCENT_AMBER));
        statsGrid.add(creerCarteStat("مشتقات | Dérivés", "0", PRIMARY_GREEN));
        statsGrid.add(creerCarteStat("تحققات | Validations", "0", ACCENT_AMBER));

        panel.add(statsGrid);

        return panel;
    }

    private JPanel creerCarteStat(String label, String value, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, color,
                        getWidth(), getHeight(), color.darker()
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 30, 40, 30));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(250, 180));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 56));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelText.setForeground(new Color(255, 255, 255, 230));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(labelText);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel creerCarteModerne() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));
        return panel;
    }

    private JPanel creerEnteteSection(String icon, String titre) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setPreferredSize(new Dimension(48, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(236, 253, 245));
        iconLabel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel titreLabel = new JLabel(titre);
        titreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titreLabel.setForeground(PRIMARY_GREEN);

        panel.add(iconLabel);
        panel.add(titreLabel);

        return panel;
    }

    private JLabel creerLabelModerne(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField creerChampTexteArabe() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (!isEnabled()) {
                    g2d.setColor(new Color(243, 244, 246));
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                if (hasFocus()) {
                    g2d.setColor(PRIMARY_GREEN_LIGHT);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                }
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);

                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        field.setBorder(new EmptyBorder(12, 15, 12, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setPreferredSize(new Dimension(0, 50));
        field.setOpaque(false);
        field.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        return field;
    }

    private JComboBox<String> creerComboBoxModerne() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        combo.setPreferredSize(new Dimension(0, 50));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 2, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return combo;
    }

    private JButton creerBoutonPrimaire(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_GREEN,
                        getWidth(), 0, new Color(4, 120, 87)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 50));

        return btn;
    }

    private JButton creerBoutonSecondaire(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(PRIMARY_GREEN);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new CompoundBorder(
                new LineBorder(PRIMARY_GREEN, 2, true),
                new EmptyBorder(12, 20, 12, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 50));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(236, 253, 245));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    private JTextArea creerZoneResultats() {
        JTextArea area = new JTextArea(6, 50);
        area.setEditable(false);
        area.setFont(new Font("Arial Unicode MS", Font.PLAIN, 13));
        area.setBackground(new Color(249, 250, 251));
        area.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        // CORRECTION: Appliquer l'orientation RTL sur area, pas sur this
        area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return area;
    }

    private JPanel creerFooter() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_GREEN);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("محرك البحث المورفولوجي العربي | Mini Projet Algorithmique 2025-2026");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Département Génie Logiciel et Systèmes d'Information (GLSI)");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sousTitre.setForeground(new Color(255, 255, 255, 200));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(sousTitre);

        return panel;
    }

    // Méthodes d'action

    private void genererMotSpecifique() {
        String racine = (String) racineCombo.getSelectedItem(); // Utiliser ComboBox
        String scheme = (String) schemeCombo.getSelectedItem();

        if (racine == null || racine.equals("-- اختر الجذر --") ||
                scheme == null || scheme.equals("-- اختر الوزن --")) {
            afficherErreur(outputAreaGeneration, "يرجى ملء جميع الحقول | Veuillez remplir tous les champs");
            return;
        }

        String motGenere = moteur.genererMotDerive(racine, scheme);

        if (motGenere != null) {
            afficherSucces(outputAreaGeneration,
                    "✨ توليد ناجح | Génération réussie\n\n" +
                            "══════════════════════════════════════════\n" +
                            "الجذر | Racine: " + racine + "\n" +
                            "الوزن | Schème: " + scheme + "\n" +
                            "النتيجة | Résultat: " + motGenere + "\n" +
                            "══════════════════════════════════════════");
        } else {
            afficherErreur(outputAreaGeneration, "فشل في توليد الكلمة | Échec de génération");
        }
    }

    private void genererTousDerivees() {
        String racine = (String) racineCombo.getSelectedItem(); // Utiliser ComboBox

        if (racine == null || racine.equals("-- اختر الجذر --")) {
            afficherErreur(outputAreaGeneration, "يرجى اختيار جذر | Veuillez choisir une racine");
            return;
        }

        List<String> derivees = moteur.genererTousLesDerivees(racine);

        if (!derivees.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("📚 جميع المشتقات | Tous les dérivés de: ").append(racine).append("\n");
            sb.append("══════════════════════════════════════════\n\n");
            int count = 1;
            for (String d : derivees) {
                sb.append(count++).append(". ").append(d).append("\n");
            }
            sb.append("\n══════════════════════════════════════════\n");
            sb.append("المجموع | Total: ").append(derivees.size()).append(" مشتق");
            afficherSucces(outputAreaGeneration, sb.toString());
        } else {
            afficherErreur(outputAreaGeneration, "لم يتم العثور على مشتقات | Aucun dérivé trouvé");
        }
    }

    private void validerMot() {
        String mot = motField.getText().trim();
        String racine = racineValField.getText().trim();

        if (mot.isEmpty() || racine.isEmpty()) {
            afficherErreur(outputAreaValidation, "يرجى ملء جميع الحقول | Veuillez remplir tous les champs");
            return;
        }

        if (racine.length() != 3) {
            afficherErreur(outputAreaValidation, "الجذر يجب أن يحتوي على 3 أحرف | La racine doit contenir 3 lettres");
            return;
        }

        ResultatValidation resultat = moteur.validerMot(mot, racine);

        if (resultat.estValide()) {
            afficherSucces(outputAreaValidation,
                    "✓ صحيح | VALIDE\n\n" +
                            "══════════════════════════════════════════\n" +
                            "الكلمة | Mot: " + mot + "\n" +
                            "الجذر | Racine: " + racine + "\n\n" +
                            resultat.toString() + "\n" +
                            "══════════════════════════════════════════");
        } else {
            afficherErreur(outputAreaValidation,
                    "✗ غير صحيح | NON VALIDE\n\n" +
                            "══════════════════════════════════════════\n" +
                            "الكلمة | Mot: " + mot + "\n" +
                            "الجذر | Racine: " + racine + "\n\n" +
                            resultat.toString() + "\n" +
                            "══════════════════════════════════════════");
        }
    }

    private void ajouterRacine() {
        String racine = nouvelleRacineField.getText().trim();

        if (racine.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "يرجى إدخال جذر | Veuillez entrer une racine",
                    "خطأ | Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (racine.length() != 3) {
            JOptionPane.showMessageDialog(this,
                    "الجذر يجب أن يحتوي على 3 أحرف | La racine doit contenir 3 lettres",
                    "خطأ | Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (arbreRacines.rechercher(racine) != null) {
            JOptionPane.showMessageDialog(this,
                    "الجذر موجود مسبقا | Cette racine existe déjà",
                    "تحذير | Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        arbreRacines.inserer(racine);
        nouvelleRacineField.setText("");

        // Rafraîchir le ComboBox des racines dans le panneau de génération
        rafraichirComboRacines();

        JOptionPane.showMessageDialog(this,
                "✓ تمت الإضافة بنجاح | Racine ajoutée avec succès: " + racine,
                "نجاح | Succès",
                JOptionPane.INFORMATION_MESSAGE);

        // Rafraîchir l'affichage en recréant le panneau
        contentPanel.remove(contentPanel.getComponent(2)); // Remove old racines panel
        contentPanel.add(creerPanneauRacinesModerne(), "racines", 2);
        cardLayout.show(contentPanel, "racines");
    }

    /**
     * Rafraîchit le ComboBox des racines après l'ajout d'une nouvelle racine
     */
    private void rafraichirComboRacines() {
        if (racineCombo != null) {
            racineCombo.removeAllItems();
            racineCombo.addItem("-- اختر الجذر --");
            List<String> racines = arbreRacines.getToutesLesRacines();
            for (String r : racines) {
                racineCombo.addItem(r);
            }
        }
    }

    private void rechercherScheme() {
        String nom = rechSchemeField.getText().trim();

        if (nom.isEmpty()) {
            afficherErreur(outputAreaSchemes, "يرجى إدخال اسم الوزن | Veuillez entrer le nom du schème");
            return;
        }

        Scheme scheme = tableSchemes.rechercher(nom);

        if (scheme != null) {
            afficherSucces(outputAreaSchemes,
                    "✓ وزن موجود | Schème trouvé!\n\n" +
                            "══════════════════════════════════════════\n" +
                            "الاسم | Nom: " + scheme.getNom() + "\n" +
                            "النوع | Type: " + scheme.getType() + "\n" +
                            "الوصف | Description: " + scheme.getDescription() + "\n" +
                            "══════════════════════════════════════════");
        } else {
            afficherErreur(outputAreaSchemes,
                    "✗ وزن غير موجود | Schème non trouvé!\n\n" +
                            "══════════════════════════════════════════\n" +
                            "الوزن '" + nom + "' غير موجود في النظام\n" +
                            "══════════════════════════════════════════");
        }
    }

    // Méthodes utilitaires pour l'affichage

    private void afficherSucces(JTextArea area, String message) {
        area.setForeground(new Color(5, 150, 105));
        area.setText(message);
    }

    private void afficherErreur(JTextArea area, String message) {
        area.setForeground(new Color(220, 38, 38));
        area.setText(message);
    }

    private void clearAllOutputs() {
        if (outputAreaGeneration != null) outputAreaGeneration.setText("");
        if (outputAreaValidation != null) outputAreaValidation.setText("");
        if (outputAreaSchemes != null) outputAreaSchemes.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainSwing frame = new MainSwing();
            frame.setVisible(true);
        });
    }
}