[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/G801g4IR)
## Gestionnaire de Fichiers en Commandes en Ligne avec Annotations

FRATCZAK, Théo, 21915913, 22

TOUMA, Charbel, 21916905, 11

https://github.com/ensYeh/miniprojet-grp-11_22

L'objet de ce mini-projet est de développer un https://fr.wikipedia.org/wiki/Gestionnaire_de_fichier[gestionnaire de fichiers] en commande en ligne. Le gestionnaire de fichier s'appuie sur le système de fichier de votre système d'exploitation. Le gestionnaire de fichier doit offrir la possibilité d'annoter les éléments contenus dans un répertoire (i.e.fichiers, répertoires).
Vous devrez respecter les contraintes fonctionnelles et techniques mentionnées ci-dessous.

#### [IMPORTANT]
Vous respecterez les contraintes suivantes:

* Ce mini-projet est à réaliser en Java par groupe de 2 étudiants.
  - les 2 étudiants doivent coopérer à travers un même git. Un des deux étudiants devra permettre à l'autre d'utiliser son git.
* Il devra comporter une documentation dans ce git.
  - La documentation devra décrire l'usage de l'application (_manuel utilisateur_) ainsi que la conception du jeu (_manuel technique_).
  - La documentation doit être un fichier .md ou .adoc et accessible depuis le `git`
* Vous utiliserez `git` en effectuant des commits réguliers comportant des messages informatifs. L'usage des "pull requests" est également fortement conseillé, montrant ainsi votre collaboration au sein du groupe. 
* Le _build_ sera assuré par Maven et plus précisément Maven wrapper (déjà intégré dans le projet).
Aucune manipulation en dehors de Maven ne devra être nécessaire.
* La version de Java à utiliser est la https://adoptium.net/[version 17].
* Le _build_ devra intégrer `checkstyle` pour la vérification des règles de codages Google.
Le projet devra donc les respecter.
`checkstyle` devra être exécuté automatiquement durant la phase `validate` du cycle de vie par défaut.
* Des tests unitaires https://junit.org/junit5/docs/current/user-guide/[JUnit 5] (version https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter/5.9.1[_5.9.1_]) devront être disponibles pour la plupart des méthodes développées.
* Un outil de https://fr.wikipedia.org/wiki/Couverture_de_code[_Code Coverage_] devra être intégré au _build_
* Les fonctionnalités du langage Java devront être utilisées au mieux (POO, exceptions, librairie de collections, I/O, …).
* L'application devra pouvoir être exécutée à partir d'un `jar` incluant toutes les dépendances.

== Description du gestionnaire de fichiers
* L'interface proposera une visualisation du contenu d'un répertoire en mode texte. A chaque élément du répertoire (**ER**), on associera un numero (**NER**) permettant de le designer lors de l'utilisation d'une des commandes définies ci-dessous.

* Les actions de l'utilisateur seront saisies au clavier sous la forme `[<NER>] [<commande>] [<nom>]`. Les crochets signifient "optionnel" 
  - Par exemples:
   ** `3 cut` ; efface le troisième fichier.
   ** `3`  ; l'utilisateur désigne le troisième élément du répertoire.
  - Si l'utilisateur ne place pas de NER, c'est le dernier NER utilisé qui sera utilisé pour une commande qui en nécessite. Exemple `visu`
  - Les commandes du gestion de fichiers à implémenter sont:
    - `[<NER>] copy`
    - `past`  ; si l’élément existe, alors le nom du nouvel élément sera concaténé avec "-copy"
    - `[<NER>] cut`
    - `..` ; pour remonter d'un cran dans le système de fichiers
    - `[<NER>] .` ; pour entrer dans un répertoire à condition que le NER désigne un répertoire. Exemple "4 ."
    - `mkdir <nom>` ; pour créer un répertoire
    - `[<NER>] visu` ; permet de voir le contenu d'un fichier texte. Si le fichier n'est pas de type texte, vous afficherez sa taille.
    - `find <nom fichier>` ; Recherche dans toutes les sous répertoires du répertoire courant, le(s) fichier(s) et les affiches.

* L'application permettra à l'utilisateur d'annoter un ER
  - l'annotation consiste à ajouter ou retirer un texte associé à un ER. 
  - Par exemples:
       ** 3 + "ceci est un texte" ; le texte est ajouté ou concaténé au texte existant sur l'ER
       ** 3 -  ; retire tout le texte associé à l'ER 3



### Description de l'application à réaliser

* L'interface proposera une visualisation du répertoire courant en mode texte en quatre parties. La partie:
  . présente le cheminement depuis la racine du système de fichier de votre système de fichier.
  . affiche la note associée à l'élément courant NER si elle existe. L'élément courant correspond à la désignation du NER par l'utilisateur ou désigné dans la dernière commande utilisée. Exemple `3 copy`, 3 devient l'élément courant.
  . affiche les ER du répertoire avec leurs NER
  . présente un prompt invitant l'utilisateur à saisir une des commandes présentées ci-dessus.

* Une interface spécifique pourra être proposée pour des situations particulières (visualiser le contenu d'un fichier, une aide, ...).

* Toutes les notes associées aux éléments du répertoire courant seront stockés dans un seul fichier appelé "notes" dans le répertoire courant. Ce fichier peut contenir des objets "sérialisés" ou d'autres formats.



### Références
* Article Wikipedia https://fr.wikipedia.org/wiki/Gestionnaire_de_fichiers[Gestionnaire de fichiers]

* Quelques bibliothèques :
http://fusesource.github.io/jansi/[JAnsi] (couleur dans un terminal),
https://github.com/jline/jline3[JLine] (gestion des saisies)

### Manuel utilisateur

**Savoir quelle commande taper pour un élément du répertoire désigné**
Une auto-complétion (TAB) est disponible et permet de connaître les commandes exactes que l'utilisateurs peut utiliser en fonction de l'élément courant ainsi que de la structure du prompt.
**Mises à jours du fichier des annotations à effectuer en fonction des types de commandes**
mkdir / past : ajout d'une note
cut : suppression d'une note 
cd / .. / . : mise à jour de l'annotation courante.

**Bibliothèques Java qui permette de prendre en charge la visualisation d'une image png si l'utilisateur veut l'afficher**
Il existe plusieurs bibliothèques permettant cela, tel que JavaFx, AWT, Java ImageI/O, dans notre cas nous avons choisi Swing javax. 

**Quelles sont les commandes qui seraient utiles de rajouter ?**
- **help** pour avoir une liste et une description de chaque commande avec leur utilisation.
- **exit** pour quitter le programme proprement sans provoquer d'erreur.

**Quelles améliorations peut on envisager pour rendre l'usage de l'interface clavier plus souples/efficaces pour l'utilisateur ?**
L'implémentation de l'auto-complétion permet d'aider l'utilisateur à trouver plus rapidement les commandes qu'il souhaite et peut utiliser dans le contexte, également à trouver plus rapidement un dossier pour se déplacer.

## Manuel technique
### Compiler le projet

##### Sous Windows, Mac et Linux
```bash
> ./mvnw clean package assembly:single
```

### Exécuter l'application
```bash
$ java -jar target/explorer-1.0-jar-with-dependencies.jar
```

### Liste des commandes prompt
```bash
$ prompt> help    # affiche la liste des commandes
```
```bash
$ prompt> <NER> .    # se rendre vers le dossier associé au ner
```
```bash
$ prompt> ..    # retourne dans le dossier parent
```
```bash
$ prompt> mkdir <NOM_FICHIER>    # créer un nouveau dossier
```
```bash
$ prompt> cd <CHEMIN>    # déplace vers le chemin
```
```bash
$ prompt> ls    # affiche la liste des fichiers courant
```
```bash
$ prompt> exit    # quitte l'execution de l'invite de commande.
```
```bash
$ prompt> <NER> copy    # met en copie de fichier assoscié au ner.
```
```bash
$ prompt> past    # créer un fichier de la copie.
```
```bash
$ prompt> <NER> visu    # visualise le contenu d'un fichier.
```
```bash
$ prompt> find <NOM_FICHIER>    # trouve le chemin du fichier dans le dossier ourant et ses enfants.
```
#### Rapport de couverture Jacoco
Afin d'obtenir le rapport de couvertures des tests, 
il faut se rendre dans le dossier **target** du projet, dedans se trouve le dossier **reports** contenant un fichier index.html, puis ouvrir ce fichier.

#### Bibliothèque utilisées
Dans ce projet, nous avons utilisé les bibliothèques suivantes : 
**javafx :** Visualisation d'image pour un fichier .png ou .jpg
**gson :** Gestion de fichier json.
**jline :** Prompt de commande personnalisable avec auto-complétion.
**jansi :** Affichage de texte en couleur dans le terminal. 
**junit-jupiter :** Effectuer des tests.
**jacoco :** Couverture des test dans le projet.
#### Rôles des classes

**Command.java :** Ensemble des commandes de structure uniforme ayant un nom et une execution, cette cette classe abstraite permet de rendre le code plus lisible et évite la surcharge de condition if else.

**CommandLine.java :** Classe principale executant le programme et liant chaque classe pour obtenir un programme uniforme.

**ElementRepertory.java :** Classe abstraite représentant un élément du répertoire et permettant la mise en forme des classes FileRef et Directory, cette class contient des informations et fonctions communes aux deux autres classes.

**FileRef.java :** Représente un fichier avec toutes les informations et fonctions necessaire. tel que la visualisation de fichier ou la suppression de fichier.

**Directory.java :** Représente un dossier avec toutes les informations et fonctions necessaire tel que le parcours de dossier, la recherche de fichier ou bien la création de dossier.

**Notes.java :** La classe Notes contient un ensemble d'instance Note, chaque Note est associé à un ElementRepertory (ER) et permet d'effectuer des actions sur le fichier notes.json.

**FileManagerException.java :** Courte classe représentant les exceptions du projet de gestion de fichier.

#### Traitement de gestion de commande
Afin de gérer l'entrée des commandes de l'utilisateur, nous utilisons un parser personnalisé qui récupère chaque mot de l'entrée et extrait les trois données suivantes :
- **Le ner** (placé en premier, ou non indiqué)
- **Le nom de la commande** (placé en premier si le ner n'est pas indiqué, sinon en deuxième)
- **L'arguement** (placé en deuxième si le ner n'est pas indiqué, sinon en troisième ou n'est pas indiqué)

**Exemple :**

```bash
prompt = "mkdir dossier"
promptSplit = ["mkdir", "dossier"] 
Le ner n'est pas indiqué (on peut vérifier en regardant si promptSplit[0] n'est pas un entier) 
donc :
nom_commande = promptSplit[0] = "mkdir"
argument = promptSplit[1] = "dossier"
```

##### Quelles améliorations peut-on envisager ?
- Création de fichier et écriture intéractive avec nano/vim.
- Déplacement de fichier avec mv.
- Recherche de modèle spécifié  avec grep.
- Interface grapghique.