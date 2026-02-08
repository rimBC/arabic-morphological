package utils;

import models.Scheme;
import models.RacineNode.MotDerive;
import structures.ABR;
import structures.HashTable;
import models.RacineNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Moteur de génération et validation morphologique.
 * Gère la génération de mots dérivés et la validation morphologique.
 */
public class MoteurMorphologique {

    private ABR arbreRacines;
    private HashTable tableSchemes;

    /**
     * Constructeur
     */
    public MoteurMorphologique(ABR arbreRacines, HashTable tableSchemes) {
        this.arbreRacines = arbreRacines;
        this.tableSchemes = tableSchemes;
    }

    /**
     * Génère un mot dérivé à partir d'une racine et d'un schème
     * @param racine La racine trilitère
     * @param nomScheme Le nom du schème à appliquer
     * @return Le mot généré, ou null en cas d'erreur
     */
    public String genererMotDerive(String racine, String nomScheme) {
        // Vérifier que la racine existe
        if (!arbreRacines.existe(racine)) {
            System.out.println("❌ Erreur: La racine '" + racine + "' n'existe pas dans l'arbre.");
            return null;
        }

        // Vérifier que le schème existe
        Scheme scheme = tableSchemes.rechercher(nomScheme);
        if (scheme == null) {
            System.out.println("❌ Erreur: Le schème '" + nomScheme + "' n'existe pas.");
            return null;
        }

        // Vérifier que la racine est trilitère
        if (racine.length() != 3) {
            System.out.println("❌ Erreur: La racine doit être trilitère (3 lettres).");
            return null;
        }

        // Générer le mot
        String motGenere = scheme.appliquerScheme(racine);

        // Ajouter le mot dérivé à la racine
        arbreRacines.ajouterMotDerive(racine, motGenere, nomScheme);

        return motGenere;
    }

    /**
     * Génère tous les mots dérivés possibles pour une racine
     * @param racine La racine trilitère
     * @return Liste des mots générés avec leurs schèmes
     */
    public List<String> genererTousLesDerivees(String racine) {
        List<String> derivees = new ArrayList<>();

        if (!arbreRacines.existe(racine)) {
            System.out.println("❌ La racine '" + racine + "' n'existe pas.");
            return derivees;
        }

        List<Scheme> schemes = tableSchemes.getTousLesSchemes();

        System.out.println("\n📚 Génération de tous les dérivés de la racine: " + racine);
        System.out.println("─".repeat(60));

        for (Scheme scheme : schemes) {
            try {
                String motGenere = scheme.appliquerScheme(racine);
                String resultat = String.format("%-15s + %-15s → %s",
                        racine, scheme.getNom(), motGenere);
                derivees.add(resultat);

                // Ajouter à la liste des dérivés de la racine
                arbreRacines.ajouterMotDerive(racine, motGenere, scheme.getNom());

                System.out.println(resultat);
            } catch (Exception e) {
                // Ignorer les erreurs de génération
            }
        }

        System.out.println("─".repeat(60));
        System.out.println("✓ Total généré: " + derivees.size() + " mots");

        return derivees;
    }

    /**
     * Valide si un mot appartient morphologiquement à une racine
     * @param mot Le mot à valider
     * @param racine La racine supposée
     * @return Résultat de validation avec détails
     */
    public ResultatValidation validerMot(String mot, String racine) {
        // Vérifier que la racine existe
        if (!arbreRacines.existe(racine)) {
            return new ResultatValidation(false, null,
                    "La racine '" + racine + "' n'existe pas dans l'arbre");
        }

        // Vérifier que la racine est trilitère
        if (racine.length() != 3) {
            return new ResultatValidation(false, null,
                    "La racine doit être trilitère");
        }

        // Tester tous les schèmes
        List<Scheme> schemes = tableSchemes.getTousLesSchemes();

        for (Scheme scheme : schemes) {
            try {
                if (scheme.correspondAuScheme(mot, racine)) {
                    // Ajouter le mot validé à la racine
                    arbreRacines.ajouterMotDerive(racine, mot, scheme.getNom());

                    return new ResultatValidation(true, scheme.getNom(),
                            "Le mot appartient à la racine via le schème " + scheme.getNom());
                }
            } catch (Exception e) {
                // Continuer avec le schème suivant
            }
        }

        return new ResultatValidation(false, null,
                "Le mot ne correspond à aucun schème connu pour cette racine");
    }

    /**
     * Décompose un mot pour identifier la racine et le schème
     * @param mot Le mot à décomposer
     * @return Résultat de décomposition
     */
    public ResultatDecomposition decomposerMot(String mot) {
        List<String> racines = arbreRacines.getToutesLesRacines();

        for (String racine : racines) {
            List<Scheme> schemes = tableSchemes.getTousLesSchemes();

            for (Scheme scheme : schemes) {
                try {
                    if (scheme.correspondAuScheme(mot, racine)) {
                        return new ResultatDecomposition(true, racine, scheme.getNom(),
                                "Mot décomposé avec succès");
                    }
                } catch (Exception e) {
                    // Continuer
                }
            }
        }

        return new ResultatDecomposition(false, null, null,
                "Impossible de décomposer le mot");
    }

    /**
     * Affiche tous les dérivés validés d'une racine
     */
    public void afficherDerivesDeRacine(String racine) {
        RacineNode noeud = arbreRacines.rechercher(racine);

        if (noeud == null) {
            System.out.println("❌ La racine '" + racine + "' n'existe pas.");
            return;
        }

        List<MotDerive> derives = noeud.getMotsDerivesValides();

        System.out.println("\n📖 Dérivés validés de la racine: " + racine);
        System.out.println("─".repeat(60));

        if (derives.isEmpty()) {
            System.out.println("Aucun dérivé enregistré pour cette racine.");
        } else {
            int i = 1;
            for (MotDerive derive : derives) {
                System.out.println(i + ". " + derive);
                i++;
            }
        }

        System.out.println("─".repeat(60));
        System.out.println("Total: " + derives.size() + " dérivés");
    }

    /**
     * Classe représentant le résultat d'une validation
     */
    public static class ResultatValidation {
        private boolean valide;
        private String scheme;
        private String message;

        public ResultatValidation(boolean valide, String scheme, String message) {
            this.valide = valide;
            this.scheme = scheme;
            this.message = message;
        }

        public boolean estValide() {
            return valide;
        }

        public String getScheme() {
            return scheme;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            if (valide) {
                return "✓ OUI - Schème utilisé: " + scheme;
            } else {
                return "✗ NON - " + message;
            }
        }
    }

    /**
     * Classe représentant le résultat d'une décomposition
     */
    public static class ResultatDecomposition {
        private boolean succes;
        private String racine;
        private String scheme;
        private String message;

        public ResultatDecomposition(boolean succes, String racine, String scheme, String message) {
            this.succes = succes;
            this.racine = racine;
            this.scheme = scheme;
            this.message = message;
        }

        public boolean estSucces() {
            return succes;
        }

        public String getRacine() {
            return racine;
        }

        public String getScheme() {
            return scheme;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            if (succes) {
                return "✓ Racine: " + racine + " | Schème: " + scheme;
            } else {
                return "✗ " + message;
            }
        }
    }
}