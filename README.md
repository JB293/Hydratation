# 💧 HydroFlow

HydroFlow est une application Android moderne de suivi d'hydratation quotidienne conçue avec **Jetpack Compose** et **Material 3**. 

*Note : Le projet est actuellement en cours de développement, mais une version fonctionnelle locale est disponible.*

---

## 🚀 Comment le projet a été configuré pour tourner en local

Pour faire fonctionner l'application sur des appareils plus anciens (compatibilité minimale fixée à l'**API 24 / Android 7.0**) tout en contournant les contraintes de dépendances récentes d'Android Studio, plusieurs ajustements techniques ont été faits :

### 1. Gestion de la compatibilité des Dates (API 24+)
Afin de ne pas forcer l'utilisation de l'API 26 (requise nativement par `java.time`), l'application utilise l'ancienne API de gestion de temps pour formater l'historique d'eau consommée :
* Les timestamps de l'historique sont gérés sous forme de `Long` (millisecondes).
* L'affichage utilise `java.text.SimpleDateFormat` combiné à `java.util.Locale.getDefault()`.
* **Optimisation Jetpack Compose :** Le formateur de date a été encapsulé dans un bloc `remember` pour éviter les pertes de performances et les saccades lors du défilement de l'historique (`LazyColumn`).

### 2. Résolution des conflits de dépendances (AAR Metadata Errors)
Les versions récentes des bibliothèques Jetpack (`androidx.core` version 1.19.0+ et `lifecycle` version 2.11.0+) imposent d'utiliser le SDK Android 37 et le plugin Gradle 9.x. 

Pour compiler l'application localement sur un environnement standard avec un SDK de compilation fixé à **36**, une stratégie de contraintes de dépendances a été forcée dans le fichier `build.gradle.kts` :
```kotlin
constraints {
    implementation("androidx.core:core:1.15.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
}
```
Cela permet de forcer l'utilisation de versions stables et compatibles avec Gradle 8.x en écrasant les dépendances transitives conflictuelles.

---

## 🛠️ État actuel du projet (En cours)

### Ce qui fonctionne :
* **Interface Graphique :** Intégration complète du thème sombre personnalisé Material 3 (`HydrationTheme`).
* **Cercle de progression :** Animation fluide (via `animateFloatAsState`) indiquant le pourcentage d'eau consommé par rapport à l'objectif quotidien.
* **Historique des logs :** Affichage en temps réel des apports d'eau dans une `LazyColumn` avec formatage de l'heure.
* **Boutons d'action :** Bouton pour ajouter un apport de 250ml et un bouton pour réinitialiser (Reset) la progression.

### Reste à finaliser :
* [ ] Connexion complète avec la base de données locale (Room) pour persister l'historique après la fermeture de l'application.
* [ ] Gestion du système de Streak (calcul automatique du nombre de jours consécutifs validés).
