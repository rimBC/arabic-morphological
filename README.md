# Moteur de Recherche Morphologique Arabe

## Description

Système de gestion et d'analyse morphologique pour la langue arabe basé sur le modèle **racine-schème**. Ce projet implémente un **Arbre Binaire de Recherche (ABR)** optimisé pour le stockage et la recherche efficace de racines arabes trilitères et de leurs dérivés morphologiques.

### Objectifs

- Gestion efficace des racines arabes trilitères
- Recherche avec complexité O(log n)
- Association racine-dérivés morphologiques
- Génération automatique de dérivés (à venir)
- Validation morphologique (à venir)
- Table de hachage pour les schèmes (à venir)

---

## Démarrage Rapide

### Prérequis

- **Java JDK 8** ou supérieur
- Support **UTF-8** pour les caractères arabes
- Git (optionnel, pour cloner le projet)

### Installation

```bash
# Cloner le repository
git clone https://github.com/votre-username/arabic-morphological-engine.git
cd arabic-morphological-engine

# Compiler le projet
javac -encoding UTF-8 *.java

# Exécuter l'application
java ArabicRootBSTDemo
```

**Ou utiliser le script fourni :**

```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

---

## Structure du Projet

```
arabic-morphological/
├── src/
│   ├── models/
│   │   ├── RacineNode.java      
│   │   └── Scheme.java          
│   ├── structures/
│   │   ├── ABR.java        
│   │   └── HashTable.java    
│   ├── utils/
│   │   ├── ChargeurDonnees.java  
│   │   └── MoteurMorphologique.java      
│   ├── Main.java               
│   └── MainSwing.java          
├── data/
│   └── racines.txt                # Fichier de racines arabes
└── README.md                      # Ce fichier
```

---

## Utilisation

### Interface en Ligne de Commande

L'application offre un menu interactif :

```
╔════════════════════════════════════════════════════╗
║                   MAIN MENU                        ║
╠════════════════════════════════════════════════════╣
║ 1. Gestion des racines                             ║
║ 2. Gestion des schèmes                             ║
║ 3. Générer des mots dérivés                        ║
║ 4. Valider un mot morphologiquement                ║
║ 5. Décomposer un mot  (trouver racine + schème)    ║
║ 6. Afficher les dérivés d'une racine               ║
║ 7. Afficher les statistiques                       ║
║ 8. Rechercher une racine                           ║
║ 0. Quitter                                         ║
╚════════════════════════════════════════════════════╝
```

### Exemples d'Utilisation

#### 1. Charger des racines depuis un fichier

Au démarrage, le programme charge automatiquement les racines depuis `arabic_roots.txt` :

```
Loading roots from file...
Successfully loaded 20 roots from file.
```

#### 2. Insérer une nouvelle racine

```
Choix : 1
Enter Arabic root (3 letters): فهم
✓ Root 'فهم' inserted successfully.
```

#### 3. Rechercher une racine

```
Choix : 2
Enter Arabic root to search: كتب
✓ Root 'كتب' FOUND in the tree!
  Number of derivatives: 3
  Search time: 0.0012 ms
```

#### 4. Ajouter des dérivés

```
Choix : 4
Enter the root: كتب
Enter the derivative word: كاتب
✓ Derivative 'كاتب' added to root 'كتب'.
```

#### 5. Voir tous les dérivés

```
Choix : 5
Enter the root: كتب
Derivatives of root 'كتب':
  1. كاتب (écrivain)
  2. مكتوب (écrit)
  3. كتاب (livre)
  4. مكتبة (bibliothèque)
```

---
## 🔧 Schèmes Implémentés

| Schème | Type | Description |
|--------|------|-------------|
| فاعل | Nom d'agent | Celui qui fait l'action |
| مفعول | Nom de patient | Celui qui subit l'action |
| افتعل | Verbe forme VIII | Action intensive |
| تفعيل | Masdar | Nom d'action |
| مفعل | Nom de lieu | Lieu de l'action |


**Règles :**
- Exactement **3 caractères arabes** par racine
- Encodage **UTF-8** obligatoire
- Une racine par ligne
- Commentaires avec `#`
- Lignes vides ignorées

---
## Performance et Complexité

### Complexité Algorithmique

| Opération | Complexité |
|-----------|-----------|
| Insertion racine | O(log n) |
| Recherche racine | O(log n) |
| Insertion schème | O(1) |
| Recherche schème | O(1) |



---

### Complexité Spatiale

- **Arbre** : O(n) pour n racines
- **Dérivés** : O(d) où d = nombre total de dérivés
- **Total** : O(n + d)

---

## Fonctionnalités Détaillées

### Fonctionnalités Actuelles

1. **Gestion des Racines**
    - Chargement automatique depuis fichier
    - Insertion dynamique de nouvelles racines
    - Validation stricte (3 lettres arabes uniquement)
    - Recherche ultra-rapide
    - AVL:
      Auto-équilibrage
      Garantie O(log n) dans tous les cas
      Rotations automatiques


2. **Gestion des Dérivés**
    - Association automatique racine-dérivés
    - Stockage sans doublons
    - Affichage organisé par racine

3. **Interface Utilisateur**
    - Menu interactif en ligne de commande
    - Messages clairs et informatifs
    - Support complet UTF-8

4. **Performance**
    - Tests de performance intégrés
    - Statistiques détaillées
    - Mesure du temps de recherche

   
#### Phase 2 : Table de Hachage pour les Schèmes
```java
PatternHashTable patterns = new PatternHashTable();
patterns.insert("فاعل", new Pattern("فاعل"));
patterns.insert("مفعول", new Pattern("مفعول"));
```

#### Phase 3 : Génération Morphologique
```java
String derivative = generator.generate("كتب", "فاعل");
// Résultat : "كاتب"
```

#### Phase 4 : Validation Morphologique
```java
boolean valid = validator.validate("كاتب", "كتب");
// Résultat : true (كاتب dérive de كتب avec schème فاعل)
```


---

## Tests

### Tests Fonctionnels

```bash
# Tous les tests passent 
✓ Chargement de 20+ racines
✓ Insertion de nouvelles racines
✓ Recherche existante/non-existante
✓ Ajout de dérivés multiples
✓ Affichage ordonné
✓ Gestion des doublons
✓ Validation des racines
```

---


### Problème : Caractères arabes affichés incorrectement

**Solution :**
1. Vérifiez que le fichier est encodé en UTF-8
2. Compilez avec : `javac -encoding UTF-8 *.java`
3. Assurez-vous que votre terminal supporte UTF-8

### Problème : "FileNotFoundException" pour arabic_roots.txt

**Solution :**
```bash
# Vérifier que le fichier existe
ls -l arabic_roots.txt

# S'assurer d'être dans le bon répertoire
pwd

# Utiliser un chemin absolu si nécessaire
String path = "/chemin/absolu/vers/arabic_roots.txt";

```
---



## Documentation

### Fichiers de Documentation

- **README.md** (ce fichier) - Vue d'ensemble et guide rapide
- **RAPPORT_MP_Algo.pdf** - Documentation technique détaillée

### Ressources Externes

- [Unicode Arabic Block](https://unicode.org/charts/PDF/U0600.pdf)
- [Java Documentation](https://docs.oracle.com/javase/8/docs/)
- [Introduction à la morphologie arabe](https://fr.wikipedia.org/wiki/Morphologie_de_l%27arabe)

---

## Licence

MIT License — feel free to use for educational purposes.

---

## Auteurs

Ce projet a été réalisé dans le cadre académique du mini-projet d'Algorithmique.

**Année universitaire :** 2025-2026  
**Enseignants :** Narjes Ben Hariz, Sahbi Bahroun   
**Etudiantes :** Rim Ben Chaalia, Islem Bouchouicha, Nada Mokrane


---

## Fonctionnalités Clés

- **Performance** : Recherche en O(log n)
- **Unicode** : Support complet de l'arabe
- **Documentation** : Complète et détaillée
- **Tests** : Couverts et validés
- **Interface** : Intuitive et claire
- **Extensible** : Prêt pour évolutions futures

---

## Apprentissages

Ce projet permet de maîtriser :
- Structures de données avancées (arbres)
- Algorithmes de recherche et tri
- Gestion de l'Unicode
- Programmation orientée objet
- Git et GitHub
- Documentation technique

---

**Dernière mise à jour :** Fevrier 2026

---