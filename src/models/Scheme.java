package models;

/** kol wazn andou class fih nom pattern desc type
 * fi choix de conception mention enou 3malna this class to apply scheme and verify if word orginates from given scheme houni
 *
 * Représente un schème morphologique arabe avec sa règle de transformation.
 * Un schème définit comment les consonnes d'une racine sont insérées dans un pattern.
 */
public class Scheme {
    private String nom;                 // Nom du schème (ex: "فاعل", "مفعول")
    private String pattern;             // Pattern abstrait (ex: "فاعل" où ف-ع-ل sont des positions)
    private String description;         // Description du schème
    private TypeScheme type;            // Type de schème (nom, verbe, etc.)

    /**
     * he4i sna3 type esmou typeScheme stamlou fi type eli lfou9
     * Énumération des types de schèmes
     */
    public enum TypeScheme {
        NOM_AGENT,          // فاعل - celui qui fait l'action
        NOM_PATIENT,        // مفعول - celui qui subit l'action
        VERBE_FORME_VIII,   // افتعل
        MASDAR,             // تفعيل - nom d'action
        NOM_LIEU,           // مفعل - lieu de l'action
        ADJECTIF,           // فعيل - adjectif
        AUTRE
    }

    /**
     * Constructors
     */
    public Scheme(String nom, String pattern, String description, TypeScheme type) {
        this.nom = nom;
        this.pattern = pattern;
        this.description = description;
        this.type = type;
    }

    public Scheme(String nom, String pattern, TypeScheme type) {
        this(nom, pattern, "", type);
    }


    /**houni function eli genri word using wazn e4eka replaces hrouf
     *
     * Applique le schème à une racine trilitère pour générer un mot dérivé
     * @param racine La racine trilitère (3 consonnes)
     * @return Le mot dérivé généré
     */
    public String appliquerScheme(String racine) {
        if (racine == null || racine.length() != 3) {
            throw new IllegalArgumentException("La racine doit être trilitère (3 lettres)");
        }

        // Extraire les trois lettre de la racine
        char c1 = racine.charAt(0);
        char c2 = racine.charAt(1);
        char c3 = racine.charAt(2);

        // Remplacer les positions dans le pattern
        // juste pour la lisibilité ma3malnech boucle wahda so complexité 3n instead of n 5tr deja schemes 9sar
        String resultat = pattern;
        resultat = resultat.replace('ف', c1);
        resultat = resultat.replace('ع', c2);
        resultat = resultat.replace('ل', c3);

        return resultat;
    }

    /**
     * Vérifie si un mot correspond à ce schème appliqué à une racine donnée
     * @param mot Le mot à vérifier
     * @param racine La racine supposée
     * @return true si le mot correspond au schème + racine
     */
    public boolean correspondAuScheme(String mot, String racine) {
        if (mot == null || racine == null || racine.length() != 3) {
            return false;
        }

        String motTest = appliquerScheme(racine); //generate word from given racine
        return mot.equals(motTest);               //compare given word and the generated word. equals eli bil overide to compare schemes bch na3rfou bech n3aytou el this function
    }

    @Override
    public String toString() {
        return nom + " (" + pattern + ") - " + type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                                  //i4a nafs address deja nafs objet
        if (obj == null || getClass() != obj.getClass()) return false; //diff class or null object yraja3 false
        Scheme scheme = (Scheme) obj;                                  //cast now that we checked class
        return pattern.equals(scheme.pattern);                         //changed from ycompari names to ycompari pattern makes more sense ig
    }


    /**
     * hashCode() returns an integer that represents the object.
     * !! hot fi rapport he4i chnistamloha fil hash table hiya trepresenti el object by an int since nom unique hash unique*/

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    /**
     * hatit getters w setters houni bch nitlhew bil main code 9bal*/

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeScheme getType() {
        return type;
    }

    public void setType(TypeScheme type) {
        this.type = type;
    }
}