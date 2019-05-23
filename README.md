# Dojo Hepia

## RUN ME - DojoHepia

### prerequisites

<p>MAVEN</p>
<p>DOCKER</p>
<p>DOCKER-COMPOSE</p>
<p>ANGULAR CLI</p>

### Temporary user ids :

| Username | Password | Privileges |
|----------|----------|------------|
| shodai   | admin   | shodai     |


<p>When connected to shodai account, you can generate tokens to create further sensei in "Sponsorship" (location : left nav bar)</p>

<p>All created account without token will automatically be ranked as "monji"</p>
<b>Users privileges hierarchy</b>

<p>shodai>sensei>monji</p>

#### 1. Mongo Database

##### 1.1 Create container

>./mongodb/
```
docker-compose up -d
```

#### 2. Gateway
>./gateway/
```
mvn package
mvn exec:java
```
#### 3. Client

>./client/
```
npm install
ng serve --open
```

#### 4. Compilateur

##### 4.1 Pull java container

```
docker pull freakency/java:1.0
```
##### 4.2 Pull python container

```
docker pull freakency/python:1.0
```
##### 4.3 Compile and run

>./compilation/
```
mvn package
mvn exec:java
```



## Vocabulaire
| Art - Martial | Dojo Hepia       |
|---------------|------------------|
| Dojo          | Le site internet |
| Shodai        | Fondateur        |
| Shihan        | Administrateur   |
| Sensei        | Professeur       |
| Sempaï        | Modérateur       |
| Monji         | Elève            |
| Kata          | Exercice         |

## But du projet
<div style="text-align: justify;">
Dojo hepia, est une outil d’apprentissage de programmation en ligne. Cette plateforme, spécialement construite pour Hepia, permet aux professeurs de publier des programmes sur des points importants de la programmation (tel que les listes, les pointeurs ou encore les tableaux), et de les scinder en une série d’exercice.

Dojo hepia se voit donc doté d’un outil de programmation en ligne qui permet à un  élève de de tester son raisonnement en direct, et de valider un exercice de cette façon.

Le but de cette plateforme est de proposer un outil pédagogique en ligne, simple d’utilisation.
</div>
## Pourquoi ?
Parler de la valeur pédago ajoutée et de la plateforme de code et de compilation online




