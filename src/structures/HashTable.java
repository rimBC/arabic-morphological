
package structures;

import models.Scheme;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation d'une table de hachage pour stocker les schèmes morphologiques. (linked list )
 * Utilise le chaînage pour gérer les collisions.
 */
public class HashTable {

    /**
     * Classe interne représentant un élément de la table
     */
    private static class EntreeTable {
        String cle;
        Scheme valeur;
        EntreeTable suivant;

        EntreeTable(String cle, Scheme valeur) {
            this.cle = cle;
            this.valeur = valeur;
            this.suivant = null;
        }
    }

    private EntreeTable[] table; //root
    private int capacite;
    private int taille;
    private static final double FACTEUR_CHARGE_MAX = 0.75;

    /**
     * Constructeur avec capacité par défaut
     */
    public HashTable() {
        this(16);
    }

    /**
     * Constructeur avec capacité spécifiée
     */
    public HashTable(int capaciteInitiale) {
        this.capacite = capaciteInitiale;
        this.table = new EntreeTable[capacite];
        this.taille = 0;
    }

    /**
     * Fonction de hachage simple mais efficace
     */
    private int hash(String cle) {
        if (cle == null) return 0;

        int hash = 0;
        for (int i = 0; i < cle.length(); i++) {
            hash = 31 * hash + cle.charAt(i);
        }
        return Math.abs(hash % capacite);
    }

    /**
     * Redimensionne la table lorsque le facteur de charge est dépassé
     */
    private void redimensionner() {
        int nouvelleCapacite = capacite * 2;
        EntreeTable[] nouvelleTable = new EntreeTable[nouvelleCapacite];

        // Réinsérer tous les éléments dans la nouvelle table
        for (int i = 0; i < capacite; i++) {
            EntreeTable entree = table[i];
            while (entree != null) {
                EntreeTable suivant = entree.suivant;

                // Recalculer l'index avec la nouvelle capacité
                int hash = 0;
                for (int j = 0; j < entree.cle.length(); j++) {
                    hash = 31 * hash + entree.cle.charAt(j);
                }
                int nouvelIndex = Math.abs(hash % nouvelleCapacite);

                // Insérer au début de la chaîne
                entree.suivant = nouvelleTable[nouvelIndex];
                nouvelleTable[nouvelIndex] = entree;

                entree = suivant;
            }
        }

        table = nouvelleTable;
        capacite = nouvelleCapacite;
    }

    /**
     * Ajoute ou met à jour un schème dans la table
     */
    public void ajouter(String nom, Scheme scheme) {
        if (nom == null || scheme == null) {
            throw new IllegalArgumentException("La clé et le schème ne peuvent pas être null");
        }

        // Vérifier si un redimensionnement est nécessaire
        if ((double) taille / capacite >= FACTEUR_CHARGE_MAX) {
            redimensionner();
        }

        int index = hash(nom);
        EntreeTable entree = table[index];

        // Chercher si la clé existe déjà
        while (entree != null) {
            if (entree.cle.equals(nom)) {
                // Mettre à jour la valeur
                entree.valeur = scheme;
                return;
            }
            entree = entree.suivant;
        }

        // Ajouter un nouveau nœud au début de la chaîne
        EntreeTable nouvelleEntree = new EntreeTable(nom, scheme);
        nouvelleEntree.suivant = table[index];
        table[index] = nouvelleEntree;
        taille++;
    }

    /**
     * Recherche un schème par son nom
     * Complexité moyenne: O(1)
     */
    public Scheme rechercher(String nom) {
        if (nom == null) return null;

        int index = hash(nom);
        EntreeTable entree = table[index];

        while (entree != null) {
            if (entree.cle.equals(nom)) {
                return entree.valeur;
            }
            entree = entree.suivant;
        }

        return null;
    }

    /**
     * Vérifie si un schème existe
     */
    public boolean existe(String nom) {
        return rechercher(nom) != null;
    }

    /**
     * Supprime un schème de la table
     */
    public boolean supprimer(String nom) {
        if (nom == null) return false;

        int index = hash(nom);
        EntreeTable entree = table[index];
        EntreeTable precedent = null;

        while (entree != null) {
            if (entree.cle.equals(nom)) {
                if (precedent == null) {
                    // Supprimer le premier élément de la chaîne
                    table[index] = entree.suivant;
                } else {
                    precedent.suivant = entree.suivant;
                }
                taille--;
                return true;
            }
            precedent = entree;
            entree = entree.suivant;
        }

        return false;
    }

    /**
     * Récupère tous les schèmes stockés
     */
    public List<Scheme> getTousLesSchemes() {
        List<Scheme> schemes = new ArrayList<>();

        for (int i = 0; i < capacite; i++) {
            EntreeTable entree = table[i];
            while (entree != null) {
                schemes.add(entree.valeur);
                entree = entree.suivant;
            }
        }

        return schemes;
    }

    /**
     * Récupère tous les noms de schèmes
     */
    public List<String> getTousLesNoms() {
        List<String> noms = new ArrayList<>();

        for (int i = 0; i < capacite; i++) {
            EntreeTable entree = table[i];
            while (entree != null) {
                noms.add(entree.cle);
                entree = entree.suivant;
            }
        }

        return noms;
    }

    /**
     * Affiche tous les schèmes
     */
    public void afficher() {
        System.out.println("\n=== Schèmes morphologiques disponibles ===");
        int compteur = 1;

        for (int i = 0; i < capacite; i++) {
            EntreeTable entree = table[i];
            while (entree != null) {
                System.out.println(compteur + ". " + entree.valeur);
                compteur++;
                entree = entree.suivant;
            }
        }

        System.out.println("Total: " + taille + " schèmes");
    }

    /**
     * Retourne le nombre de schèmes stockés
     */
    public int getTaille() {
        return taille;
    }

    /**
     * Vérifie si la table est vide
     */
    public boolean estVide() {
        return taille == 0;
    }

    /**
     * Vide complètement la table
     */
    public void vider() {
        table = new EntreeTable[capacite];
        taille = 0;
    }

    /**
     * Affiche les statistiques de la table de hachage
     */
    public void afficherStatistiques() {
        System.out.println("\n=== Statistiques de la table de hachage ===");
        System.out.println("Capacité: " + capacite);
        System.out.println("Nombre d'éléments: " + taille);
        System.out.println("Facteur de charge: " + String.format("%.2f", (double) taille / capacite));

        // Calculer la longueur moyenne des chaînes
        int chainesNonVides = 0;
        int longueurMax = 0;

        for (int i = 0; i < capacite; i++) {
            if (table[i] != null) {
                chainesNonVides++;
                int longueur = 0;
                EntreeTable entree = table[i];
                while (entree != null) {
                    longueur++;
                    entree = entree.suivant;
                }
                longueurMax = Math.max(longueurMax, longueur);
            }
        }

        System.out.println("Chaînes non vides: " + chainesNonVides);
        System.out.println("Longueur max de chaîne: " + longueurMax);
        if (chainesNonVides > 0) {
            System.out.println("Longueur moyenne: " +
                    String.format("%.2f", (double) taille / chainesNonVides));
        }
    }
}