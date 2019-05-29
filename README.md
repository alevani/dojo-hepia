# Dojo Hepia

## DojoHepia-v0.5 update
- A sensei is now able to 
	- Kata 
		- Disable
		- Edit
		- Delete
	- Program
		- Delete
		- Edit

- Users can see theirs done and ongoing katas
- Markdown support added for kata instructions
- New display in kata-displayer
- Database improvement
- Bug fixed
	- Click tag


## RUN ME - DojoHepia

### prerequisites

<p>MAVEN</p>
<p>DOCKER</p>
<p>DOCKER-COMPOSE</p>
<p>ANGULAR CLI</p>

#### 1. Mongo Database

##### 1.1 Create the database container

>./mongodb/

```
docker-compose up -d
```

`If en error occurs, please log  into docker hub (should not happen)`

```
docker login
```

> **PLEASE NOTE**: The database will initialize itself with data

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

#### 4. Compiler

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

### Already registered users :

| Username | Password | Privileges |
|----------|----------|------------|
| shodai   | admin   | shodai     |
| noob   | noob   | monji     |

<b>Users privileges hierarchy</b>
<p>shodai>sensei>monji</p>

#### Create a Sensei account

If you want to generate an account with Sensei privileges, please do the following steps

1. Login with the 'shodai' account
2. Go to Subscription (on the side navigation menu)
3. Hit 'Generate a token', and copy it
4. Logout from your current account
5. Click on 'Create a account'
6. Check the 'I have a token' box
7. Past the copied token into the newly generated input box
8. A new account with Sensei has been created, you can now use it

> **PLEASE NOTE**: All created account without token will automatically be ranked as "monji"

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




