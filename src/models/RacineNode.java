package models;

import java.util.ArrayList;
import java.util.List;

/**
 * hey lbnet avl tal3it ABR juste far9 bin niveau wel tahtou ka3ba
* ka fazt equilibrer keyini cas particulier mil ABR
 *
 *
 * Représente un nœud de l'arbre binaire de recherche contenant une racine arabe.
 * Chaque nœud stocke la racine, sa fréquence et la liste des mots dérivés validés.
 */
public class RacineNode {
    private String racine;              // Racine trilitère arabe (ex: "كتب")
    private int frequence;              // Fréquence d'utilisation de la racine
    private List<MotDerive> motsDerivesValides;  // Liste des mots dérivés validés
    private RacineNode gauche;          // Sous-arbre gauche
    private RacineNode droit;           // Sous-arbre droit
    private int hauteur;                // Hauteur du nœud (pour AVL) kol insert nsajil hauteur!

    /**
     * Constructeur pour créer un nouveau nœud de racine
     * @param racine La racine arabe trilitère
     */
    public RacineNode(String racine) {
        this.racine = racine;
        this.frequence = 1;
        this.motsDerivesValides = new ArrayList<>();
        this.gauche = null;
        this.droit = null;
        this.hauteur = 1;
    }

    // Getters et Setters
    public String getRacine() {
        return racine;
    }

    public void setRacine(String racine) {
        this.racine = racine;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public void incrementerFrequence() {
        this.frequence++;
    }

    public List<MotDerive> getMotsDerivesValides() {
        return motsDerivesValides;
    }

    public void setMotsDerivesValides(List<MotDerive> motsDerivesValides) {
        this.motsDerivesValides = motsDerivesValides;
    }

    public RacineNode getGauche() {
        return gauche;
    }

    public void setGauche(RacineNode gauche) {
        this.gauche = gauche;
    }

    public RacineNode getDroit() {
        return droit;
    }

    public void setDroit(RacineNode droit) {
        this.droit = droit;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    /**
     * Ajoute un mot dérivé validé à la liste
     * @param motDerive Le mot dérivé à ajouter
     */
    public void ajouterMotDerive(MotDerive motDerive) {
        // Vérifier si le mot n'existe pas déjà
        for (MotDerive md : motsDerivesValides) {
            if (md.getMot().equals(motDerive.getMot())) {
                md.incrementerFrequence();
                return;
            }
        }
        motsDerivesValides.add(motDerive);
    }

    /**
     * Classe interne représentant un mot dérivé
     */
    public static class MotDerive {
        private String mot;
        private String scheme;
        private int frequence;

        public MotDerive(String mot, String scheme) {
            this.mot = mot;
            this.scheme = scheme;
            this.frequence = 1;
        }

        public String getMot() {
            return mot;
        }

        public String getScheme() {
            return scheme;
        }

        public int getFrequence() {
            return frequence;
        }

        public void incrementerFrequence() {
            this.frequence++;
        }

        @Override
        public String toString() {
            return mot + " (" + scheme + ") - Fréq: " + frequence;
        }
    }

    @Override
    public String toString() {
        return "Racine: " + racine + " | Fréquence: " + frequence +
                " | Dérivés: " + motsDerivesValides.size();
    }
}