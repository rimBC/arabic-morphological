import structures.ABR;
import structures.HashTable;
import utils.ChargeurDonnees;
import utils.MoteurMorphologique;
import utils.MoteurMorphologique.ResultatValidation;
import utils.MoteurMorphologique.ResultatDecomposition;
import models.Scheme;
import models.RacineNode;

import java.util.Scanner;
import java.util.List;

/**
 * Application principale du Moteur de Recherche Morphologique Arabe
 * Projet Algorithmique 2025-2026
 */
public class Main {

    private static ABR arbreRacines;
    private static HashTable tableSchemes;
    private static MoteurMorphologique moteur;
    private static Scanner scanner;
    private static final String FICHIER_RACINES = "data/racines.txt";

    public static void main(String[] args) {
        // Initialisation
        initialiser();

        // Afficher le logo et l'en-tête
        afficherEntete();

        // Charger les données
        chargerDonnees();

        // Menu principal
        menuPrincipal();

        // Fermeture
        scanner.close();
        System.out.println("\n👋 Merci d'avoir utilisé le Moteur Morphologique Arabe!");
    }

    /**
     * Initialise les structures de données
     */
    private static void initialiser() {
        arbreRacines = new ABR();
        tableSchemes = new HashTable();
        scanner = new Scanner(System.in);
    }

    /**
     * Affiche l'en-tête de l'application
     */
    private static void afficherEntete() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("  🔍 MOTEUR DE RECHERCHE MORPHOLOGIQUE ARABE");
        System.out.println("  📚 Système de Génération et Validation de Dérivés");
        System.out.println("═".repeat(70));
        System.out.println("  Projet Algorithmique 2025-2026");
        System.out.println("  Département GLSI - Niveau 1ING");
        System.out.println("═".repeat(70) + "\n");
    }

    /**
     * Charge les données initiales
     */
    private static void chargerDonnees() {
        System.out.println("🔄 Initialisation du système...\n");

        // Créer un fichier exemple si nécessaire
        ChargeurDonnees.creerFichierExemple(FICHIER_RACINES);

        // Charger les racines
        System.out.println("📖 Chargement des racines...");
        int nbRacines = ChargeurDonnees.chargerRacinesDepuisFichier(FICHIER_RACINES, arbreRacines);

        // Initialiser les schèmes
        System.out.println("\n🔧 Initialisation des schèmes morphologiques...");
        ChargeurDonnees.initialiserSchemes(tableSchemes);

        // Créer le moteur morphologique
        moteur = new MoteurMorphologique(arbreRacines, tableSchemes);

        // Afficher le rapport
        ChargeurDonnees.afficherRapportChargement(arbreRacines, tableSchemes);

        if (nbRacines == 0) {
            System.out.println("⚠ Aucune racine chargée. Vous pouvez en ajouter manuellement.");
        }
    }

    /**
     * Menu principal de l'application
     */
    private static void menuPrincipal() {
        boolean continuer = true;

        while (continuer) {
            afficherMenu();

            int choix = lireChoix();
            System.out.println();

            switch (choix) {
                case 1:
                    gererRacines();
                    break;
                case 2:
                    gererSchemes();
                    break;
                case 3:
                    genererMotsDerivees();
                    break;
                case 4:
                    validerMorphologie();
                    break;
                case 5:
                    decomposerMot();
                    break;
                case 6:
                    afficherDerivesRacine();
                    break;
                case 7:
                    afficherStatistiques();
                    break;
                case 8:
                    rechercherRacine();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("❌ Choix invalide. Veuillez réessayer.");
            }

            if (continuer) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Affiche le menu principal
     */
    private static void afficherMenu() {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("  MENU PRINCIPAL");
        System.out.println("─".repeat(70));
        System.out.println("  1. 📚 Gestion des racines");
        System.out.println("  2. 🔧 Gestion des schèmes");
        System.out.println("  3. ✨ Générer des mots dérivés");
        System.out.println("  4. ✓  Valider un mot morphologiquement");
        System.out.println("  5. 🔍 Décomposer un mot (trouver racine + schème)");
        System.out.println("  6. 📖 Afficher les dérivés d'une racine");
        System.out.println("  7. 📊 Afficher les statistiques");
        System.out.println("  8. 🔎 Rechercher une racine");
        System.out.println("  0. 🚪 Quitter");
        System.out.println("─".repeat(70));
        System.out.print("Votre choix: ");
    }

    /**
     * Gestion des racines
     */
    private static void gererRacines() {
        System.out.println("═".repeat(70));
        System.out.println("  GESTION DES RACINES");
        System.out.println("═".repeat(70));
        System.out.println("1. Ajouter une racine");
        System.out.println("2. Afficher toutes les racines");
        System.out.println("3. Rechercher une racine");
        System.out.println("4. Sauvegarder les racines");
        System.out.print("\nChoix: ");

        int choix = lireChoix();
        System.out.println();

        switch (choix) {
            case 1:
                ajouterRacine();
                break;
            case 2:
                arbreRacines.afficherInfixe();
                break;
            case 3:
                rechercherRacine();
                break;
            case 4:
                ChargeurDonnees.sauvegarderRacines(FICHIER_RACINES, arbreRacines);
                break;
        }
    }

    /**
     * Ajoute une nouvelle racine
     */
    private static void ajouterRacine() {
        System.out.print("Entrez la racine trilitère (3 lettres): ");
        scanner.nextLine(); // Vider le buffer
        String racine = scanner.nextLine().trim();

        if (racine.length() != 3) {
            System.out.println("❌ La racine doit contenir exactement 3 lettres.");
            return;
        }

        if (arbreRacines.existe(racine)) {
            System.out.println("⚠ Cette racine existe déjà dans l'arbre.");
        } else {
            arbreRacines.inserer(racine);
            System.out.println("✓ Racine '" + racine + "' ajoutée avec succès!");
        }
    }

    /**
     * Recherche une racine
     */
    private static void rechercherRacine() {
        System.out.print("Entrez la racine à rechercher: ");
        scanner.nextLine(); // Vider le buffer
        String racine = scanner.nextLine().trim();

        long debut = System.nanoTime();
        RacineNode noeud = arbreRacines.rechercher(racine);
        long fin = System.nanoTime();

        double tempsMs = (fin - debut) / 1_000_000.0;

        System.out.println("\n🔍 Résultat de la recherche:");
        System.out.println("─".repeat(50));

        if (noeud != null) {
            System.out.println("✓ TROUVÉE!");
            System.out.println(noeud);
        } else {
            System.out.println("✗ NON TROUVÉE");
        }

        System.out.println("⏱ Temps de recherche: " + String.format("%.6f", tempsMs) + " ms");
        System.out.println("─".repeat(50));
    }

    /**
     * Gestion des schèmes
     */
    private static void gererSchemes() {
        System.out.println("═".repeat(70));
        System.out.println("  GESTION DES SCHÈMES");
        System.out.println("═".repeat(70));
        System.out.println("1. Afficher tous les schèmes");
        System.out.println("2. Rechercher un schème");
        System.out.println("3. Ajouter un schème personnalisé");
        System.out.print("\nChoix: ");

        int choix = lireChoix();
        System.out.println();

        switch (choix) {
            case 1:
                tableSchemes.afficher();
                break;
            case 2:
                rechercherScheme();
                break;

        }
    }

    /**
     * Recherche un schème
     */
    private static void rechercherScheme() {
        System.out.print("Entrez le nom du schème: ");
        scanner.nextLine();
        String nom = scanner.nextLine().trim();

        Scheme scheme = tableSchemes.rechercher(nom);

        if (scheme != null) {
            System.out.println("✓ Schème trouvé: " + scheme);
        } else {
            System.out.println("✗ Schème non trouvé");
        }
    }


    /**
     * Génère des mots dérivés
     */
    private static void genererMotsDerivees() {
        System.out.println("═".repeat(70));
        System.out.println("  GÉNÉRATION DE MOTS DÉRIVÉS");
        System.out.println("═".repeat(70));
        System.out.println("1. Générer un mot avec un schème spécifique");
        System.out.println("2. Générer tous les dérivés d'une racine");
        System.out.print("\nChoix: ");

        int choix = lireChoix();
        scanner.nextLine();
        System.out.println();

        if (choix == 1) {
            genererMotSpecifique();
        } else if (choix == 2) {
            genererTousLesDerivees();
        }
    }

    /**
     * Génère un mot avec un schème spécifique
     */
    private static void genererMotSpecifique() {
        System.out.print("Racine: ");
        String racine = scanner.nextLine().trim();

        System.out.print("Schème: ");
        String scheme = scanner.nextLine().trim();

        String motGenere = moteur.genererMotDerive(racine, scheme);

        if (motGenere != null) {
            System.out.println("\n✨ Mot généré:");
            System.out.println("─".repeat(50));
            System.out.println("  Racine: " + racine);
            System.out.println("  Schème: " + scheme);
            System.out.println("  Résultat: " + motGenere);
            System.out.println("─".repeat(50));
        }
    }

    /**
     * Génère tous les dérivés d'une racine
     */
    private static void genererTousLesDerivees() {
        System.out.print("Racine: ");
        String racine = scanner.nextLine().trim();

        moteur.genererTousLesDerivees(racine);
    }

    /**
     * Valide un mot morphologiquement
     */
    private static void validerMorphologie() {
        System.out.println("═".repeat(70));
        System.out.println("  VALIDATION MORPHOLOGIQUE");
        System.out.println("═".repeat(70));

        scanner.nextLine();

        System.out.print("Mot à valider: ");
        String mot = scanner.nextLine().trim();

        System.out.print("Racine supposée: ");
        String racine = scanner.nextLine().trim();

        ResultatValidation resultat = moteur.validerMot(mot, racine);

        System.out.println("\n📋 Résultat de la validation:");
        System.out.println("─".repeat(50));
        System.out.println("  Mot: " + mot);
        System.out.println("  Racine: " + racine);
        System.out.println("  " + resultat);
        System.out.println("─".repeat(50));
    }

    /**
     * Décompose un mot
     */
    private static void decomposerMot() {
        System.out.println("═".repeat(70));
        System.out.println("  DÉCOMPOSITION MORPHOLOGIQUE");
        System.out.println("═".repeat(70));

        scanner.nextLine();

        System.out.print("Mot à décomposer: ");
        String mot = scanner.nextLine().trim();

        System.out.println("\n🔍 Analyse en cours...");
        ResultatDecomposition resultat = moteur.decomposerMot(mot);

        System.out.println("\n📋 Résultat:");
        System.out.println("─".repeat(50));
        System.out.println("  Mot: " + mot);
        System.out.println("  " + resultat);
        System.out.println("─".repeat(50));
    }

    /**
     * Affiche les dérivés d'une racine
     */
    private static void afficherDerivesRacine() {
        scanner.nextLine();

        System.out.print("Racine: ");
        String racine = scanner.nextLine().trim();

        moteur.afficherDerivesDeRacine(racine);
    }

    /**
     * Affiche les statistiques du système
     */
    private static void afficherStatistiques() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("  STATISTIQUES DU SYSTÈME");
        System.out.println("═".repeat(70));

        arbreRacines.afficherStatistiques();
        tableSchemes.afficherStatistiques();

        System.out.println("═".repeat(70));
    }

    /**
     * Lit un choix entier de l'utilisateur
     */
    private static int lireChoix() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Vider le buffer
            return -1;
        }
    }
}