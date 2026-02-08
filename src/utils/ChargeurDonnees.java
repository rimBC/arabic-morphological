package utils;

import structures.ABR;
import structures.HashTable;
import models.Scheme;
import models.Scheme.TypeScheme;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire pour charger les racines et initialiser les schèmes
 */
public class ChargeurDonnees {

    /**
     * Charge les racines à partir d'un fichier texte
     * @param nomFichier Le chemin du fichier
     * @param arbre L'arbre AVL où stocker les racines
     * @return Le nombre de racines chargées
     */
    public static int chargerRacinesDepuisFichier(String nomFichier, ABR arbre) {
        int compteur = 0;
        List<String> lignesInvalides = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(nomFichier), StandardCharsets.UTF_8))) {

            String ligne;
            int numeroLigne = 0;

            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                // Ignorer les lignes vides et les commentaires
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                // Vérifier que la racine est trilitère
                if (ligne.length() == 3) {
                    arbre.inserer(ligne);
                    compteur++;
                } else {
                    lignesInvalides.add("Ligne " + numeroLigne + ": '" + ligne +
                            "' (longueur " + ligne.length() + " au lieu de 3)");
                }
            }

            System.out.println("✓ Chargement terminé: " + compteur + " racines insérées");

            if (!lignesInvalides.isEmpty()) {
                System.out.println("\n⚠ Lignes ignorées (" + lignesInvalides.size() + "):");
                for (String msg : lignesInvalides) {
                    System.out.println("  " + msg);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erreur: Fichier non trouvé - " + nomFichier);
        } catch (IOException e) {
            System.err.println("❌ Erreur de lecture du fichier: " + e.getMessage());
        }

        return compteur;
    }

    /**
     * Initialise les schèmes morphologiques standards dans la table de hachage
     * @param table La table de hachage où stocker les schèmes
     */
    public static void initialiserSchemes(HashTable table) {
        // Schème 1: فاعل - Nom d'agent (celui qui fait l'action)
        table.ajouter("فاعل", new Scheme(
                "فاعل",
                "فاعل",
                "Nom d'agent - celui qui fait l'action",
                TypeScheme.NOM_AGENT
        ));

        // Schème 2: مفعول - Nom de patient (celui qui subit l'action)
        table.ajouter("مفعول", new Scheme(
                "مفعول",
                "مفعول",
                "Nom de patient - celui qui subit l'action",
                TypeScheme.NOM_PATIENT
        ));

        // Schème 3: افتعل - Verbe forme VIII
        table.ajouter("افتعل", new Scheme(
                "افتعل",
                "افتعل",
                "Verbe forme VIII",
                TypeScheme.VERBE_FORME_VIII
        ));

        // Schème 4: تفعيل - Masdar (nom d'action)
        table.ajouter("تفعيل", new Scheme(
                "تفعيل",
                "تفعيل",
                "Nom d'action (masdar)",
                TypeScheme.MASDAR
        ));

        // Schème 5: مفعل - Nom de lieu
        table.ajouter("مفعل", new Scheme(
                "مفعل",
                "مفعل",
                "Nom de lieu - endroit où se fait l'action",
                TypeScheme.NOM_LIEU
        ));

        // Schème 6: فعيل - Adjectif
        table.ajouter("فعيل", new Scheme(
                "فعيل",
                "فعيل",
                "Adjectif qualificatif",
                TypeScheme.ADJECTIF
        ));

        // Schème 7: فعال - Intensif
        table.ajouter("فعال", new Scheme(
                "فعال",
                "فعال",
                "Forme intensive du nom d'agent",
                TypeScheme.NOM_AGENT
        ));

        // Schème 8: تفاعل - Forme VI (action réciproque)
        table.ajouter("تفاعل", new Scheme(
                "تفاعل",
                "تفاعل",
                "Verbe forme VI - action réciproque",
                TypeScheme.AUTRE
        ));

        // Schème 9: انفعال - Forme VII (passif)
        table.ajouter("انفعال", new Scheme(
                "انفعال",
                "انفعال",
                "Verbe forme VII - passif/réflexif",
                TypeScheme.AUTRE
        ));

        // Schème 10: استفعال - Forme X (demande)
        table.ajouter("استفعال", new Scheme(
                "استفعال",
                "استفعال",
                "Verbe forme X - demande, recherche",
                TypeScheme.AUTRE
        ));

        System.out.println("✓ " + table.getTaille() + " schèmes morphologiques initialisés");
    }

    /**
     * Sauvegarde les racines dans un fichier
     * @param nomFichier Le chemin du fichier de sortie
     * @param arbre L'arbre contenant les racines
     */
    public static void sauvegarderRacines(String nomFichier, ABR arbre) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(nomFichier), StandardCharsets.UTF_8))) {

            List<String> racines = arbre.getToutesLesRacines();

            bw.write("# Fichier de racines arabes trilitères\n");
            bw.write("# Généré automatiquement\n");
            bw.write("# Total: " + racines.size() + " racines\n\n");

            for (String racine : racines) {
                bw.write(racine);
                bw.newLine();
            }

            System.out.println("✓ Racines sauvegardées dans: " + nomFichier);

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    /**
     * Crée un fichier de racines exemple s'il n'existe pas
     * @param nomFichier Le chemin du fichier à créer
     */
    public static void creerFichierExemple(String nomFichier) {
        File fichier = new File(nomFichier);

        if (fichier.exists()) {
            return; // Le fichier existe déjà
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(nomFichier), StandardCharsets.UTF_8))) {

            bw.write("# Fichier de racines arabes trilitères - Exemples\n");
            bw.write("# Chaque ligne contient une racine de 3 lettres\n\n");

            // Quelques racines d'exemple courantes
            String[] racinesExemple = {
                    "كتب",  // écrire
                    "درس",  // étudier
                    "علم",  // savoir
                    "فهم",  // comprendre
                    "قرأ",  // lire
                    "سمع",  // entendre
                    "ذهب",  // aller
                    "جلس",  // s'asseoir
                    "قال",  // dire
                    "عمل",  // travailler
                    "فعل",  // faire
                    "شرب",  // boire
                    "أكل",  // manger
                    "نظر",  // regarder
                    "سأل"   // demander
            };

            for (String racine : racinesExemple) {
                bw.write(racine);
                bw.newLine();
            }

            System.out.println("✓ Fichier exemple créé: " + nomFichier);

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la création du fichier exemple: " + e.getMessage());
        }
    }

    /**
     * Affiche un rapport de chargement
     */
    public static void afficherRapportChargement(ABR arbre, HashTable table) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  RAPPORT DE CHARGEMENT");
        System.out.println("═".repeat(60));
        System.out.println("📚 Racines chargées: " + arbre.getTaille());
        System.out.println("🔧 Schèmes disponibles: " + table.getTaille());
        System.out.println("📊 Hauteur de l'arbre: " +
                (arbre.estVide() ? "N/A" : "calculée"));
        System.out.println("═".repeat(60) + "\n");
    }
}