# News-Management-System

## About
RESTfull API web-service that implementing functionality for working with the news management system.

## Technologies
+ Java 17
+ Gradle 7.2
+ Tomcat 9.0.63
+ Spring (Boot, Web, Data, Cloud, AOP) 
+ MongoDB 5.0
+ Mapstruct 1.5.2
+ Lombok 1.18.20
+ Docker (Docker-compose) 20.10.17
+ Hazelcast 5.1.2
+ WireMock
+ Mock
+ JUnit 5
+ SL4J

## Instructions
1) Run command "./gradlew bootJar" in app folder ("News-Management-System").
2) Start Docker Desktop (Windows, Mac, Linux) or Docker Engine (Linux). 
3) In "compose" folder run "docker-compose build" to build images, if it's necessary. Then run "docker-compose up -d" to start docker containers detached.
4) Wait until all containers get started and application is ready to use.

## Tests
- Before running service integration tests outside of docker container run "java -jar wiremock-studio-2.32.0-18.jar" to start standalone wiremock application
(Running in container is not supported yet).
- "wiremock-studio-*.jar" might be loaded from GitHub with errors due to big file size. In this situation download it manually from: https://wiremock.org/studio/docs/getting-started/desktop/ to "wiremock" folder.

## Properties
- application.yml (web):
  - spring.profiles.active (dev,prod) – Changes active profile. Leads to different config parameters.
  - spring.config.import: - Address for Spring Cloud Config server (protocol, ip, port)
- application.yml (config-server):
  - spring.cloud.config.server.native.search-locations – Path to folder with client configs
- nms-application-*.yml (config-server):
  - spring.data.mongodb.host – MongoDb host address (ip)
  - spring.data.mongodb.port – MongoDb port
  - spring.data.mongodb.database – MongoDb database name that will be used
  - spring.data.mongodb.auto-index-creation – Enables auto index creation (Required for Fulltext search)
  - logging.file.name – Logs file name
  - logging.level.by.stas.nms.debug – Log level for logging aspects
  - hazelcast.server.host – Hazelcast server address (ip)
  - hazelcast.server.port – Hazelcast server port
  - hazelcast.cluster.name – Hazelcast claster name to use
  - hazelcast.map.ttl – TimeToLive in seconds for cache map
  
<h4>
  <p>It's strongly recommended to be accurate with property changes</p>
  <p>(Especially with ports, hosts, names)</p>
</h4>

## Entity fields
### News:
+ <b>id</b> (ObjectId)
+ <b>date</b> (ISO 8601)
+ <b>title</b> (string 2-20 characters)
+ <b>text</b> (string >1 characters)
+ <b>comments</b> (list)

### Comments:
+ <b>id</b> (ObjectId)
+ <b>date</b> (ISO 8601, LocalDateTime)
+ <b>text</b> (string >1 characters)
+ <b>username</b> (string 5-25 characters.Example: "valid.username_12")
+ <b>id_news</b> (long)

## API

### <font size="+1"><u>News</u></font>:
##### GET:
+ <b>/news?page={page}&limit={limit}</b> — Get all news ({page} & {limit} - optional int values. Pages start counting from 0. Default values page=0 and limit=10)
+ <b>/news/{id}</b> — Get News entity with comments by id ({id} - news id).
+ <b>/news/search?term={term}&page={page}&limit={limit}</b> — Get all news using fulltext search ({term} - term for fulltext search).
##### POST:
+ <b>/news</b> — Create new News entity. (Body: News entity)
##### PATCH:
+ <b>/news/{id}</b> — Update existing News entity. ({id} - news id. Body: any field from News entity)
##### DELETE:
+ <b>/news/{id}</b> — Delete existing News entity. ({id} - news id)

### <font size="+1"><u>Comments</u></font>:
##### GET:
+ <b>/comments?page={page}&limit={limit}</b> — Get all comments ({page} & {limit} - optional int values. Pages start counting from 0. Default values page=0 and limit=10).
+ <b>/comments/{id}</b> — Get Comment entity by id ({id} - comment id).
+ <b>/comments/search?term={term}&page={page}&limit={limit}</b> — Get all comments using fulltext search ({term} - term for fulltext search).
##### POST:
+ <b>/comments</b> — Create new Comment entity. (Body: Comment entity)
##### PATCH:
+ <b>/comments/{id}</b> — Update existing Comment entity. ({id} - comment id. Body: any field from Comment entity)
##### DELETE:
+ <b>/news/{id}</b> — Delete existing Comment entity. ({id} - comment id)