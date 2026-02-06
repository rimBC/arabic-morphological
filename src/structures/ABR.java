package structures;

import models.RacineNode;
import models.RacineNode.MotDerive;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation d'un arbre AVL pour stocker et gérer les racines arabes.
 * L'arbre AVL garantit un équilibrage automatique pour maintenir une complexité O(log n).
 * nfasrou choix b exemple mretbin fard line w still ABR search n vs AVL search log(n)
 */
public class ABR {
    private RacineNode rootAVL;
    private int taille;

    /**
     * Constructeur d'un arbre vide
     */
    public ABR() {
        this.rootAVL = null;
        this.taille = 0;
    }

    /**
     * Retourne la hauteur d'un nœud
     */
    private int hauteur(RacineNode noeud) {
        return (noeud == null) ? 0 : noeud.getHauteur();
    }

    /**
     * Calcule le facteur d'équilibre d'un nœud
     */
    private int facteurEquilibre(RacineNode noeud) {
        return (noeud == null) ? 0 : hauteur(noeud.getGauche()) - hauteur(noeud.getDroit());
    }

    /**
     * Met à jour la hauteur d'un nœud
     */
    private void mettreAJourHauteur(RacineNode noeud) {
        if (noeud != null) {
            noeud.setHauteur(1 + Math.max(hauteur(noeud.getGauche()), hauteur(noeud.getDroit())));
        }
    }

    /**
     * Rotation droite
     */
    private RacineNode rotationDroite(RacineNode y) {
        RacineNode x = y.getGauche();
        RacineNode T2 = x.getDroit();

        // Effectuer la rotation
        x.setDroit(y);
        y.setGauche(T2);

        // Mettre à jour les hauteurs
        mettreAJourHauteur(y);
        mettreAJourHauteur(x);

        return x;
    }

    /**
     * Rotation gauche
     */
    private RacineNode rotationGauche(RacineNode x) {
        RacineNode y = x.getDroit();
        RacineNode T2 = y.getGauche();

        // Effectuer la rotation
        y.setGauche(x);
        x.setDroit(T2);

        // Mettre à jour les hauteurs
        mettreAJourHauteur(x);
        mettreAJourHauteur(y);

        return y;
    }

    /**
     * Équilibre un nœud après insertion/suppression
     */
    private RacineNode equilibrer(RacineNode noeud) {
        if (noeud == null) return null;

        // Mettre à jour la hauteur
        mettreAJourHauteur(noeud);

        // Calculer le facteur d'équilibre
        int balance = facteurEquilibre(noeud);

        // Cas Gauche-Gauche
        if (balance > 1 && facteurEquilibre(noeud.getGauche()) >= 0) {
            return rotationDroite(noeud);
        }

        // Cas Droite-Droite
        if (balance < -1 && facteurEquilibre(noeud.getDroit()) <= 0) {
            return rotationGauche(noeud);
        }

        // Cas Gauche-Droite
        if (balance > 1 && facteurEquilibre(noeud.getGauche()) < 0) {
            noeud.setGauche(rotationGauche(noeud.getGauche()));
            return rotationDroite(noeud);
        }

        // Cas Droite-Gauche
        if (balance < -1 && facteurEquilibre(noeud.getDroit()) > 0) {
            noeud.setDroit(rotationDroite(noeud.getDroit()));
            return rotationGauche(noeud);
        }

        return noeud;
    }

    
    
    /**
     * Insère une racine dans l'arbre AVL
     * Complexité: O(log n)
     */
    public void inserer(String racine) {
        if (racine == null || racine.trim().isEmpty()) {
            throw new IllegalArgumentException("La racine ne peut pas être vide");
        }
        this.rootAVL = insererRecursif(this.rootAVL, racine.trim());
    }

    /**
     * Méthode récursive d'insertion
     */
    private RacineNode insererRecursif(RacineNode noeud, String racine) {
        // Insertion standard dans un ABR
        if (noeud == null) {
            taille++;
            return new RacineNode(racine);
        }

        int comparaison = racine.compareTo(noeud.getRacine());

        if (comparaison < 0) {
            noeud.setGauche(insererRecursif(noeud.getGauche(), racine));
        } else if (comparaison > 0) {
            noeud.setDroit(insererRecursif(noeud.getDroit(), racine));
        } else {
            // La racine existe déjà, on incrémente sa fréquence
            noeud.incrementerFrequence();
            return noeud;
        }

        // Équilibrer le nœud
        return equilibrer(noeud);
    }

    /**
     * Recherche une racine dans l'arbre
     * Complexité: O(log n)
     * @return Le nœud contenant la racine, ou null si non trouvé
     */
    public RacineNode rechercher(String racine) {
        if (racine == null || racine.trim().isEmpty()) {
            return null;
        }
        return rechercherRecursif(this.rootAVL, racine.trim());
    }

    /**
     * Méthode récursive de recherche
     */
    private RacineNode rechercherRecursif(RacineNode noeud, String racine) {
        // Cas de base: nœud null ou racine trouvée
        if (noeud == null || noeud.getRacine().equals(racine)) {
            return noeud;
        }

        // Recherche dans le sous-arbre approprié
        int comparaison = racine.compareTo(noeud.getRacine());
        if (comparaison < 0) {
            return rechercherRecursif(noeud.getGauche(), racine);
        } else {
            return rechercherRecursif(noeud.getDroit(), racine);
        }
    }

    /**
     * Vérifie si une racine existe dans l'arbre
     * @return true si la racine existe
     */
    public boolean existe(String racine) {
        return rechercher(racine) != null;
    }

    /**
     * Ajoute un mot dérivé à une racine existante
     */
    public boolean ajouterMotDerive(String racine, String mot, String scheme) {
        RacineNode noeud = rechercher(racine);
        if (noeud == null) {
            return false;
        }
        noeud.ajouterMotDerive(new MotDerive(mot, scheme));
        return true;
    }

    /**
     * Récupère tous les mots dérivés d'une racine
     */
    public List<MotDerive> getMotsDerivesDeRacine(String racine) {
        RacineNode noeud = rechercher(racine);
        if (noeud == null) {
            return new ArrayList<>();
        }
        return noeud.getMotsDerivesValides();
    }

    /**
     * Affichage infixe (ordre alphabétique)
     */
    public void afficherInfixe() {
        System.out.println("\n=== Liste des racines (ordre alphabétique) ===");
        afficherInfixeRecursif(rootAVL);
        System.out.println("Total: " + taille + " racines");
    }

    private void afficherInfixeRecursif(RacineNode noeud) {
        if (noeud != null) {
            afficherInfixeRecursif(noeud.getGauche());
            System.out.println(noeud);
            afficherInfixeRecursif(noeud.getDroit());
        }
    }

    /**
     * Collecte toutes les racines dans une liste
     */
    public List<String> getToutesLesRacines() {
        List<String> racines = new ArrayList<>();
        collecterRacines(this.rootAVL, racines);
        return racines;
    }

    private void collecterRacines(RacineNode noeud, List<String> liste) {
        if (noeud != null) {
            collecterRacines(noeud.getGauche(), liste);
            liste.add(noeud.getRacine());
            collecterRacines(noeud.getDroit(), liste);
        }
    }

    /**
     * Retourne la taille de l'arbre
     */
    public int getTaille() {
        return taille;
    }

    /**
     * Vérifie si l'arbre est vide
     */
    public boolean estVide() {
        return rootAVL == null;
    }

    /**
     * Affiche les statistiques de l'arbre
     */
    public void afficherStatistiques() {
        System.out.println("\n=== Statistiques de l'arbre AVL ===");
        System.out.println("Nombre de racines: " + taille);
        System.out.println("Hauteur de l'arbre: " + hauteur(rootAVL));
        System.out.println("Arbre équilibré: " + estEquilibre(rootAVL));
    }

    /**
     * Vérifie si l'arbre est équilibré
     */
    private boolean estEquilibre(RacineNode noeud) {
        if (noeud == null) return true;
        int balance = facteurEquilibre(noeud);
        return Math.abs(balance) <= 1 &&
                estEquilibre(noeud.getGauche()) &&
                estEquilibre(noeud.getDroit());
    }
}