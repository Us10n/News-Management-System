# News-Management-System

## About
RESTfull API web-service that implementing functionality for working with the news management system.

## Technologies
+ Java 17
+ Gradle 7.2
+ Tomcat 9.0.63
+ Spring (Boot, Web, Data, Cloud, AOP)
+ MongoDB
+ Mapstruct 1.5.2
+ Lombok 1.18.20
+ Docker (Docker-compose) 20.10.17
+ Hazelcast 5.1.2
+ WireMock
+ Mock
+ JUnit
+ SL4J

## Instructions
1) Run "./gradlew bootJar" in app folders ("News-Management-System","nms-config-server").
2) In "compose" folder run "docker-compose up".
3) Wait till containers get started.
4) Application is ready to use.

## Entity fields
### News:
+ <b>id</b> (long)
+ <b>date</b> (ISO 8601)
+ <b>title</b> (string 2-20 characters)
+ <b>text</b> (string >1 characters)
+ <b>comments</b> (list)

### Comments:
+ <b>id</b> (long)
+ <b>date</b> (ISO 8601, LocalDateTime)
+ <b>text</b> (string >1 characters)
+ <b>username</b> (string 5-25 characters.Example: "valid.username_12")
+ <b>id_news</b> (long)

## API

### <font size="+1"><u>News</u></font>:
##### GET:
+ <b>/news?page={page}&limit={limit}</b> — Get all news ({page} & {limit} - optional int values. Page counting from 0. Default 0 and 10)
+ <b>/news/{id}</b> — Get single news with comments by id ({id} - news id. ObjectId type)
+ <b>/news/search?term={term}&page={page}&limit={limit}</b> — Get all news using fulltext search ({term} - term for fulltext search)
##### POST:
+ <b>/news</b> — Create new News entity. (Body: News entity)
##### PATCH:
+ <b>/news/{id}</b> — Update existing News entity. ({id} - news id. Body: any field from News entity)
##### DELETE:
+ <b>/news/{id}</b> — Delete existing News entity. ({id} - news id.)

### <font size="+1"><u>Comments</u></font>:
##### GET:
+ <b>/comments?page={page}&limit={limit}</b> — Get all comments ({page} & {limit} - optional int values. Page counting from 0. Default 0 and 10)
+ <b>/comments/{id}</b> — Get single comment by id ({id} - comment id)
+ <b>/comments/search?term={term}&page={page}&limit={limit}</b> — Get all comments using fulltext search ({term} - term for fulltext search)
##### POST:
+ <b>/comments</b> — Create new Comment entity. (Body: Comment entity. id_news required!)
##### PATCH:
+ <b>/comments/{id}</b> — Update existing Comment entity. ({id} - comment id. Body: any field from Comment entity)
##### DELETE:
+ <b>/news/{id}</b> — Delete existing Comments entity. ({id} - comment id.)