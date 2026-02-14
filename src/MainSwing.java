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


public class MainSwing extends JFrame {

    private ABR arbreRacines;
    private HashTable tableSchemes;
    private MoteurMorphologique moteur;

    private JComboBox<String> racineCombo;
    private JTextField racineField, motField, racineValField, motDecField, rechSchemeField, nouvelleRacineField;
    private JComboBox<String> schemeCombo;

    private static final String FICHIER_RACINES = "data/racines.txt";

    // Palette de couleurs moderne et professionnelle
    private static final Color PRIMARY = new Color(99, 102, 241);        // Indigo moderne
    private static final Color PRIMARY_LIGHT = new Color(129, 140, 248);
    private static final Color PRIMARY_DARK = new Color(67, 56, 202);
    private static final Color SECONDARY = new Color(236, 72, 153);      // Rose vibrant
    private static final Color ACCENT = new Color(59, 130, 246);         // Bleu ciel
    private static final Color SUCCESS = new Color(34, 197, 94);         // Vert moderne
    private static final Color WARNING = new Color(251, 146, 60);        // Orange
    private static final Color ERROR = new Color(239, 68, 68);           // Rouge

    private static final Color BG_PRIMARY = new Color(248, 250, 252);    // Gris très clair
    private static final Color BG_SECONDARY = Color.WHITE;
    private static final Color BG_CARD = Color.WHITE;

    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);     // Slate foncé
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);

    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color BORDER_FOCUS = PRIMARY_LIGHT;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel mainScrollablePanel;

    public MainSwing() {
        super("محرك البحث المورفولوجي العربي - Moteur Morphologique Arabe");

        initialiserDonnees();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        getContentPane().setBackground(BG_PRIMARY);

        // En-tête avec logos
        add(creerEnteteAvecLogos(), BorderLayout.NORTH);

        // Navigation latérale
        add(creerNavigationModerne(), BorderLayout.WEST);

        // Panel principal SCROLLABLE
        mainScrollablePanel = new JPanel();
        mainScrollablePanel.setLayout(new BorderLayout());
        mainScrollablePanel.setBackground(BG_PRIMARY);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_PRIMARY);

        contentPanel.add(creerPanneauGenerationModerne(), "generation");
        contentPanel.add(creerPanneauValidationModerne(), "validation");
        contentPanel.add(creerPanneauDecompositionModerne(), "decomposition");
        contentPanel.add(creerPanneauRacinesModerne(), "racines");
        contentPanel.add(creerPanneauSchemesModerne(), "schemes");
        contentPanel.add(creerPanneauStatistiquesModerne(), "stats");

        mainScrollablePanel.add(contentPanel, BorderLayout.CENTER);

        // SCROLL sur tout le contenu
        JScrollPane mainScroll = new JScrollPane(mainScrollablePanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(mainScroll, BorderLayout.CENTER);
        add(creerFooter(), BorderLayout.SOUTH);
    }

    /**
     * En-tête avec logos de part et d'autre
     */
    private JPanel creerEnteteAvecLogos() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient moderne indigo -> violet
                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_DARK,
                        getWidth(), getHeight(), new Color(126, 34, 206)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Formes décoratives
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillOval(-100, -50, 300, 300);
                g2d.fillOval(getWidth() - 200, -100, 350, 350);
            }
        };

        panel.setPreferredSize(new Dimension(0, 160));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Logo isi GAUCHE
        JPanel logoGauche = creerLogoMU();
        logoGauche.setPreferredSize(new Dimension(160, 50));
        panel.add(logoGauche, BorderLayout.WEST);

        // Centre avec titres
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JLabel titreArabe = new JLabel("محرك البحث المورفولوجي العربي");
        titreArabe.setFont(new Font("Arial Unicode MS", Font.BOLD, 38));
        titreArabe.setForeground(Color.WHITE);
        titreArabe.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Moteur de Recherche Morphologique Arabe");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        sousTitre.setForeground(new Color(255, 255, 255, 220));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);


        centre.add(Box.createVerticalGlue());
        centre.add(titreArabe);
        centre.add(Box.createVerticalStrut(8));
        centre.add(sousTitre);
        centre.add(Box.createVerticalStrut(5));
        centre.add(Box.createVerticalGlue());

        panel.add(centre, BorderLayout.CENTER);

        // Logo utm DROITE
        JPanel logoDroit = creerLogoMU2();
        logoDroit.setPreferredSize(new Dimension(120, 120));
        panel.add(logoDroit, BorderLayout.EAST);

        return panel;
    }

    /**
     * Créer un logo stylisé
     */
    private JPanel creerLogoMU() {
        // Charger l'image une seule fois
        ImageIcon logoIcon = new ImageIcon("data/ISI.png");
        // (remplace par le vrai nom du fichier : mu_logo.png / logo.png / etc.)

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Activer l'anti-aliasing (meilleure qualité)
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Calcul de la taille (avec marge)
                int size = getWidth() - 20;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;



                // Dessiner le logo MU (image centrée et redimensionnée)
                Image logo = logoIcon.getImage();
                g2d.drawImage(logo, x, y, size, size, this);
            }
        };

        panel.setOpaque(false);
        return panel;
    }
    private JPanel creerLogoMU2() {
        // Charger l'image une seule fois
        ImageIcon logoIcon = new ImageIcon("data/Utm.png");
        // (remplace par le vrai nom du fichier : mu_logo.png / logo.png / etc.)

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Activer l'anti-aliasing (meilleure qualité)
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Calcul de la taille (avec marge)
                int size = getWidth();
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;



                // Dessiner le logo MU (image centrée et redimensionnée)
                Image logo = logoIcon.getImage();
                g2d.drawImage(logo, x, y, size, size, this);
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    private JPanel creerNavigationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SECONDARY);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER),
                new EmptyBorder(25, 20, 25, 20)
        ));

        String[][] tabs = {
                {"🔄", "التوليد", "Génération", "generation"},
                {"✓", "التحقق", "Validation", "validation"},
                {"🔬", "التحليل", "Décomposition", "decomposition"},
                {"📚", "الجذور", "Racines", "racines"},
                {"🔧", "الأوزان", "Schèmes", "schemes"},
                {"📊", "الإحصائيات", "Statistiques", "stats"}
        };

        ButtonGroup group = new ButtonGroup();
        boolean first = true;

        for (String[] tab : tabs) {
            JToggleButton btn = creerBoutonNavigation(tab[0], tab[1], tab[2], tab[3]);
            group.add(btn);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(8));

            if (first) {
                btn.setSelected(true);
                first = false;
            }
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JToggleButton creerBoutonNavigation(String icon, String arabe, String francais, String cardName) {
        JToggleButton btn = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected()) {
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), 0, PRIMARY_LIGHT);
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                    // Ombre portée
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 15, 15);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(241, 245, 249));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }

                super.paintComponent(g);
            }
        };

        btn.setLayout(new BorderLayout(12, 0));
        btn.setBorder(new EmptyBorder(15, 18, 15, 18));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Icône
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLabel.setPreferredSize(new Dimension(30, 30));
        btn.add(iconLabel, BorderLayout.WEST);

        // Texte
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel arabeLabel = new JLabel(arabe);
        arabeLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 13));
        arabeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel frLabel = new JLabel(francais);
        frLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        frLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(arabeLabel);
        textPanel.add(frLabel);
        btn.add(textPanel, BorderLayout.CENTER);

        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));

        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                iconLabel.setForeground(Color.WHITE);
                arabeLabel.setForeground(Color.WHITE);
                frLabel.setForeground(new Color(255, 255, 255, 200));
            } else {
                iconLabel.setForeground(TEXT_PRIMARY);
                arabeLabel.setForeground(TEXT_PRIMARY);
                frLabel.setForeground(TEXT_SECONDARY);
            }
        });

        return btn;
    }

    private JPanel creerPanneauGenerationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("🔄", "توليد الكلمات المشتقة", "Génération de mots dérivés");
        card.add(header);
        card.add(Box.createVerticalStrut(30));

        // Racine
        card.add(creerLabelModerne("الجذر | Racine trilitère"));
        card.add(Box.createVerticalStrut(10));
        racineCombo = creerComboBoxModerne();
        racineCombo.addItem("-- اختر الجذر | Choisir --");
        List<String> racines = arbreRacines.getToutesLesRacines();
        for (String r : racines) {
            racineCombo.addItem(r);
        }
        card.add(racineCombo);
        card.add(Box.createVerticalStrut(20));

        // Schème
        card.add(creerLabelModerne("الوزن | Schème morphologique"));
        card.add(Box.createVerticalStrut(10));
        schemeCombo = creerComboBoxModerne();
        List<String> schemes = tableSchemes.getTousLesNoms();
        schemeCombo.addItem("-- اختر الوزن | Choisir --");
        for (String s : schemes) {
            schemeCombo.addItem(s);
        }
        card.add(schemeCombo);
        card.add(Box.createVerticalStrut(30));

        // Boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        JButton genererBtn = creerBoutonPrimaire(" توليد | Générer");
        genererBtn.addActionListener(e -> genererMotSpecifique());

        JButton tousBtn = creerBoutonSecondaire(" الكل | Tout");
        tousBtn.addActionListener(e -> genererTousDerivees());

        buttonPanel.add(genererBtn);
        buttonPanel.add(tousBtn);
        card.add(buttonPanel);

        panel.add(card);
        return panel;
    }

    private JPanel creerPanneauValidationModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("✓", "التحقق المورفولوجي", "Validation morphologique");
        card.add(header);
        card.add(Box.createVerticalStrut(30));

        card.add(creerLabelModerne("الكلمة | Mot à valider"));
        card.add(Box.createVerticalStrut(10));
        motField = creerChampTexteArabe();
        motField.setToolTipText("مثال: كاتب");
        card.add(motField);
        card.add(Box.createVerticalStrut(20));

        card.add(creerLabelModerne("الجذر | Racine"));
        card.add(Box.createVerticalStrut(10));
        racineValField = creerChampTexteArabe();
        racineValField.setToolTipText("مثال: كتب");
        card.add(racineValField);
        card.add(Box.createVerticalStrut(30));

        JButton validerBtn = creerBoutonPrimaire(" تحقق | Valider");
        validerBtn.addActionListener(e -> validerMot());
        validerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        card.add(validerBtn);

        panel.add(card);
        return panel;
    }

    private JPanel creerPanneauDecompositionModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("🔬", "التحليل المورفولوجي", "Décomposition morphologique");
        card.add(header);
        card.add(Box.createVerticalStrut(30));

        card.add(creerLabelModerne("الكلمة | Mot à décomposer"));
        card.add(Box.createVerticalStrut(10));
        motDecField = creerChampTexteArabe();
        motDecField.setToolTipText("مثال: كاتب");
        card.add(motDecField);
        card.add(Box.createVerticalStrut(30));

        JButton decomposerBtn = creerBoutonPrimaire(" تحليل | Décomposer");
        decomposerBtn.addActionListener(e -> decomposerMot());
        decomposerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        card.add(decomposerBtn);

        panel.add(card);
        return panel;
    }

    private JPanel creerPanneauRacinesModerne() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("📚", "إدارة الجذور", "Gestion des racines");
        card.add(header);
        card.add(Box.createVerticalStrut(25));

        // Panneau d'ajout
        JPanel addPanel = new JPanel(new BorderLayout(15, 0));
        addPanel.setBackground(Color.WHITE);
        addPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        nouvelleRacineField = creerChampTexteArabe();
        nouvelleRacineField.setToolTipText("جذر جديد | Nouvelle racine");
        addPanel.add(nouvelleRacineField, BorderLayout.CENTER);

        JButton ajouterBtn = creerBoutonPrimaire(" إضافة");
        ajouterBtn.setPreferredSize(new Dimension(160, 55));
        ajouterBtn.addActionListener(e -> ajouterRacine());
        addPanel.add(ajouterBtn, BorderLayout.EAST);

        card.add(addPanel);
        panel.add(card, BorderLayout.NORTH);

        // Grille de racines
        JPanel gridPanel = new JPanel(new GridLayout(0, 6, 18, 18));
        gridPanel.setBackground(BG_PRIMARY);

        List<String> racines = arbreRacines.getToutesLesRacines();
        for (String racine : racines) {
            gridPanel.add(creerCarteRacine(racine));
        }

        // Pas de scroll ici, le scroll est sur le panel principal
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Carte racine cliquable qui affiche les dérivés dans un popup
     */
    private JPanel creerCarteRacine(String racine) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient moderne
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(241, 245, 249),
                        getWidth(), getHeight(), new Color(249, 250, 251)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Bordure
                g2d.setColor(BORDER);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 20, 25, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(140, 130));

        JLabel racineLabel = new JLabel(racine);
        racineLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 36));
        racineLabel.setForeground(PRIMARY);
        racineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Compter les dérivés
        List<String> derives = moteur.genererTousLesDerivees(racine);
        JLabel countLabel = new JLabel(derives.size() + " مشتق");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        countLabel.setForeground(TEXT_SECONDARY);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(racineLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(countLabel);
        card.add(Box.createVerticalGlue());

        // Clic pour afficher les dérivés dans un popup
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                afficherDerivesPopup(racine, derives);
            }
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY, 3, true),
                        new EmptyBorder(24, 19, 24, 19)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(25, 20, 25, 20));
            }
        });

        return card;
    }

    /**
     * Affiche un popup moderne avec tous les dérivés d'une racine
     */
    private void afficherDerivesPopup(String racine, List<String> derives) {
        JDialog dialog = new JDialog(this, "المشتقات | Dérivés de: " + racine, true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // En-tête du popup
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel racineLabel = new JLabel(racine);
        racineLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 48));
        racineLabel.setForeground(PRIMARY);
        racineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = new JLabel(derives.size() + " مشتقات موجودة | dérivés trouvés");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(TEXT_SECONDARY);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(racineLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Liste des dérivés dans une grille
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setBackground(Color.WHITE);

        for (String derive : derives) {
            JPanel deriveCard = new JPanel();
            deriveCard.setBackground(new Color(241, 245, 249));
            deriveCard.setBorder(new CompoundBorder(
                    new LineBorder(BORDER, 1, true),
                    new EmptyBorder(15, 20, 15, 20)
            ));
            deriveCard.setLayout(new BorderLayout());

            JLabel deriveLabel = new JLabel(derive);
            deriveLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 24));
            deriveLabel.setForeground(TEXT_PRIMARY);
            deriveLabel.setHorizontalAlignment(SwingConstants.CENTER);

            deriveCard.add(deriveLabel);
            gridPanel.add(deriveCard);
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Bouton fermer
        JButton closeBtn = creerBoutonPrimaire("✖ إغلاق | Fermer");
        closeBtn.addActionListener(e -> dialog.dispose());
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel creerPanneauSchemesModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("🔧", "الأوزان المورفولوجية", "Schèmes morphologiques");
        card.add(header);
        card.add(Box.createVerticalStrut(25));

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(15, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        rechSchemeField = creerChampTexteArabe();
        rechSchemeField.setToolTipText("ابحث عن وزن | Rechercher");
        searchPanel.add(rechSchemeField, BorderLayout.CENTER);

        JButton rechercherBtn = creerBoutonPrimaire(" بحث | Rechercher");
        rechercherBtn.setPreferredSize(new Dimension(200, 55));
        rechercherBtn.addActionListener(e -> rechercherScheme());
        searchPanel.add(rechercherBtn, BorderLayout.EAST);

        card.add(searchPanel);
        panel.add(card);
        panel.add(Box.createVerticalStrut(25));

        // Grille de schèmes
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(BG_PRIMARY);

        List<String> schemes = tableSchemes.getTousLesNoms();
        for (String scheme : schemes) {
            gridPanel.add(creerCarteScheme(scheme));
        }

        // Pas de scroll ici
        panel.add(gridPanel);

        return panel;
    }

    private JPanel creerCarteScheme(String scheme) {
        Scheme schemeObj = tableSchemes.rechercher(scheme);
        String type = (schemeObj != null) ? schemeObj.getType().toString() : "N/A";
        String description = (schemeObj != null) ? schemeObj.getDescription() : "Aucune description";

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 2, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel schemeLabel = new JLabel(scheme);
        schemeLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 30));
        schemeLabel.setForeground(PRIMARY);
        schemeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel("Type: " + type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(SECONDARY);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='width:350px'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(schemeLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(descLabel);

        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY, 2, true),
                        new EmptyBorder(25, 25, 25, 25)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                        new LineBorder(BORDER, 2, true),
                        new EmptyBorder(25, 25, 25, 25)
                ));
            }
        });

        return card;
    }

    private JPanel creerPanneauStatistiquesModerne() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = creerCarteModerne();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel header = creerEnteteSection("📊", "الإحصائيات", "Statistiques du système");
        card.add(header);

        panel.add(card);
        panel.add(Box.createVerticalStrut(25));

        // Grille de statistiques
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        statsGrid.setBackground(BG_PRIMARY);

        statsGrid.add(creerCarteStat("📚", "جذور | Racines", String.valueOf(arbreRacines.getTaille()), PRIMARY));
        statsGrid.add(creerCarteStat("🔧", "أوزان | Schèmes", String.valueOf(tableSchemes.getTaille()), SECONDARY));
        statsGrid.add(creerCarteStat("🔄", "مشتقات | Dérivés", "0", ACCENT));
        statsGrid.add(creerCarteStat("✓", "تحققات | Validations", "0", SUCCESS));

        panel.add(statsGrid);

        return panel;
    }

    private JPanel creerCarteStat(String icon, String label, String value, Color color) {
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Ombre douce
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(4, 4, getWidth(), getHeight(), 25, 25);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(45, 35, 45, 35));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 200));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        iconLabel.setForeground(new Color(255, 255, 255, 200));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 64));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelText.setForeground(new Color(255, 255, 255, 220));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(labelText);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel creerCarteModerne() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(35, 35, 35, 35)
        ));
        // Ombre portée subtile
        panel.setOpaque(true);
        return panel;
    }

    private JPanel creerEnteteSection(String icon, String titreArabe, String titreFr) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setPreferredSize(new Dimension(55, 55));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(241, 245, 249));
        iconLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel arabeLabel = new JLabel(titreArabe);
        arabeLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 22));
        arabeLabel.setForeground(PRIMARY);

        JLabel frLabel = new JLabel(titreFr);
        frLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        frLabel.setForeground(TEXT_SECONDARY);

        textPanel.add(arabeLabel);
        textPanel.add(frLabel);

        panel.add(iconLabel);
        panel.add(textPanel);

        return panel;
    }

    private JLabel creerLabelModerne(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField creerChampTexteArabe() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(isEnabled() ? Color.WHITE : new Color(249, 250, 251));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                if (hasFocus()) {
                    g2d.setColor(BORDER_FOCUS);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(BORDER);
                    g2d.setStroke(new BasicStroke(2));
                }
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Arial Unicode MS", Font.PLAIN, 22));
        field.setBorder(new EmptyBorder(14, 18, 14, 18));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        field.setPreferredSize(new Dimension(0, 55));
        field.setOpaque(false);
        field.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        return field;
    }

    private JComboBox<String> creerComboBoxModerne() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        combo.setPreferredSize(new Dimension(0, 55));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 2, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        return combo;
    }

    private JButton creerBoutonPrimaire(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_DARK);
                } else {
                    GradientPaint gp = new GradientPaint(
                            0, 0, PRIMARY,
                            getWidth(), 0, PRIMARY_LIGHT
                    );
                    g2d.setPaint(gp);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Ombre
                if (!getModel().isPressed()) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 15, 15);
                }

                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 55));

        return btn;
    }

    private JButton creerBoutonSecondaire(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(PRIMARY);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new CompoundBorder(
                new LineBorder(PRIMARY, 2, true),
                new EmptyBorder(14, 24, 14, 24)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 55));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(241, 245, 249));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    private JPanel creerFooter() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_DARK,
                        getWidth(), 0, new Color(109, 40, 217)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setPreferredSize(new Dimension(0, 70));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titre = new JLabel("محرك البحث المورفولوجي العربي | Mini Projet Algorithmique 2025-2026");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titre);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    // ==================== MÉTHODES D'ACTION ====================

    private void genererMotSpecifique() {
        String racine = (String) racineCombo.getSelectedItem();
        String scheme = (String) schemeCombo.getSelectedItem();

        if (racine == null || racine.contains("--") ||
                scheme == null || scheme.contains("--")) {
            afficherPopupErreur("يرجى ملء جميع الحقول\nVeuillez remplir tous les champs");
            return;
        }

        String motGenere = moteur.genererMotDerive(racine, scheme);

        if (motGenere != null) {
            afficherPopupSucces(
                    "✨ توليد ناجح | Génération réussie",
                    String.format(
                            "<html><div style='text-align:center; font-size:14px;'>" +
                                    "<p style='font-size:18px; color:#6366f1; margin:10px;'><b>%s</b></p>" +
                                    "<p style='margin:5px;'>الجذر | Racine: <b>%s</b></p>" +
                                    "<p style='margin:5px;'>الوزن | Schème: <b>%s</b></p>" +
                                    "<p style='font-size:28px; color:#22c55e; margin:15px;'><b>%s</b></p>" +
                                    "</div></html>",
                            "Résultat", racine, scheme, motGenere
                    )
            );
        } else {
            afficherPopupErreur("فشل في توليد الكلمة\nÉchec de génération");
        }
    }

    private void genererTousDerivees() {
        String racine = (String) racineCombo.getSelectedItem();

        if (racine == null || racine.contains("--")) {
            afficherPopupErreur("يرجى اختيار جذر\nVeuillez choisir une racine");
            return;
        }

        List<String> derivees = moteur.genererTousLesDerivees(racine);

        if (!derivees.isEmpty()) {
            afficherDerivesPopup(racine, derivees);
        } else {
            afficherPopupErreur("لم يتم العثور على مشتقات\nAucun dérivé trouvé");
        }
    }

    private void validerMot() {
        String mot = motField.getText().trim();
        String racine = racineValField.getText().trim();

        if (mot.isEmpty() || racine.isEmpty()) {
            afficherPopupErreur("يرجى ملء جميع الحقول\nVeuillez remplir tous les champs");
            return;
        }

        if (racine.length() != 3) {
            afficherPopupErreur("الجذر يجب أن يحتوي على 3 أحرف\nLa racine doit contenir 3 lettres");
            return;
        }

        ResultatValidation resultat = moteur.validerMot(mot, racine);

        if (resultat.estValide()) {
            afficherPopupSucces(
                    "✓ صحيح | VALIDE",
                    String.format(
                            "<html><div style='text-align:center; font-size:14px;'>" +
                                    "<p style='font-size:24px; color:#22c55e; margin:15px;'><b>✓ VALIDE</b></p>" +
                                    "<p style='margin:8px;'>الكلمة | Mot: <b style='font-size:20px;'>%s</b></p>" +
                                    "<p style='margin:8px;'>الجذر | Racine: <b style='font-size:20px;'>%s</b></p>" +
                                    "<p style='margin:8px; color:#6366f1;'>%s</p>" +
                                    "</div></html>",
                            mot, racine, resultat.toString().replace("\n", "<br>")
                    )
            );
        } else {
            afficherPopupErreur(
                    String.format(
                            "<html><div style='text-align:center; font-size:14px;'>" +
                                    "<p style='font-size:24px; color:#ef4444; margin:15px;'><b>✗ NON VALIDE</b></p>" +
                                    "<p style='margin:8px;'>الكلمة | Mot: <b>%s</b></p>" +
                                    "<p style='margin:8px;'>الجذر | Racine: <b>%s</b></p>" +
                                    "<p style='margin:8px;'>%s</p>" +
                                    "</div></html>",
                            mot, racine, resultat.toString().replace("\n", "<br>")
                    )
            );
        }
    }

    private void decomposerMot() {
        String mot = motDecField.getText().trim();

        if (mot.isEmpty()) {
            afficherPopupErreur("يرجى إدخال كلمة\nVeuillez entrer un mot");
            return;
        }

        ResultatDecomposition resultat = moteur.decomposerMot(mot);

        afficherPopupSucces(
                "🔬 تحليل مورفولوجي | Décomposition",
                String.format(
                        "<html><div style='text-align:center; font-size:14px;'>" +
                                "<p style='font-size:24px; color:#6366f1; margin:15px;'><b>%s</b></p>" +
                                "<p style='margin:10px; font-size:13px;'>%s</p>" +
                                "</div></html>",
                        mot, resultat.toString().replace("\n", "<br>")
                )
        );
    }

    private void ajouterRacine() {
        String racine = nouvelleRacineField.getText().trim();

        if (racine.isEmpty()) {
            afficherPopupErreur("يرجى إدخال جذر\nVeuillez entrer une racine");
            return;
        }

        if (racine.length() != 3) {
            afficherPopupErreur("الجذر يجب أن يحتوي على 3 أحرف\nLa racine doit contenir 3 lettres");
            return;
        }

        if (arbreRacines.rechercher(racine) != null) {
            afficherPopupErreur("الجذر موجود مسبقا\nCette racine existe déjà");
            return;
        }

        arbreRacines.inserer(racine);
        nouvelleRacineField.setText("");
        rafraichirComboRacines();

        afficherPopupSucces(
                "✓ تمت الإضافة بنجاح",
                String.format(
                        "<html><div style='text-align:center;'>" +
                                "<p style='font-size:32px; color:#22c55e; margin:20px;'><b>%s</b></p>" +
                                "<p style='font-size:16px;'>Racine ajoutée avec succès!</p>" +
                                "</div></html>",
                        racine
                )
        );

        // Rafraîchir l'affichage
        contentPanel.remove(3);
        contentPanel.add(creerPanneauRacinesModerne(), "racines", 3);
        cardLayout.show(contentPanel, "racines");
    }

    private void rafraichirComboRacines() {
        if (racineCombo != null) {
            racineCombo.removeAllItems();
            racineCombo.addItem("-- اختر الجذر | Choisir --");
            List<String> racines = arbreRacines.getToutesLesRacines();
            for (String r : racines) {
                racineCombo.addItem(r);
            }
        }
    }

    private void rechercherScheme() {
        String nom = rechSchemeField.getText().trim();

        if (nom.isEmpty()) {
            afficherPopupErreur("يرجى إدخال اسم الوزن\nVeuillez entrer le nom du schème");
            return;
        }

        Scheme scheme = tableSchemes.rechercher(nom);

        if (scheme != null) {
            afficherPopupSucces(
                    "✓ وزن موجود | Schème trouvé",
                    String.format(
                            "<html><div style='text-align:center; font-size:14px;'>" +
                                    "<p style='font-size:28px; color:#6366f1; margin:15px;'><b>%s</b></p>" +
                                    "<p style='margin:8px;'><b>Type:</b> %s</p>" +
                                    "<p style='margin:8px;'>%s</p>" +
                                    "</div></html>",
                            scheme.getNom(), scheme.getType(), scheme.getDescription()
                    )
            );
        } else {
            afficherPopupErreur(
                    String.format(
                            "✗ وزن غير موجود\nSchème '%s' non trouvé",
                            nom
                    )
            );
        }
    }

    // ==================== POPUPS MODERNES ====================

    private void afficherPopupSucces(String titre, String message) {
        JDialog dialog = new JDialog(this, titre, true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(SUCCESS, 3, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton closeBtn = creerBoutonPrimaire("✖ إغلاق | Fermer");
        closeBtn.addActionListener(e -> dialog.dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void afficherPopupErreur(String message) {
        JDialog dialog = new JDialog(this, "خطأ | Erreur", true);
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(ERROR, 3, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align:center; font-size:15px;'>" +
                        message.replace("\n", "<br>") +
                        "</div></html>"
        );
        messageLabel.setForeground(ERROR);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton closeBtn = creerBoutonPrimaire("✖ إغلاق | Fermer");
        closeBtn.addActionListener(e -> dialog.dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
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
