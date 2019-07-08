# Dojo Hepia


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

#### 4. Compiler service

##### 4.1 Pull java container

```
docker pull freakency/java:1.0
```
##### 4.2 Pull python container

```
docker pull freakency/python:3.0
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


