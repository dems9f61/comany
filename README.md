# PROJECT
This project includes both backend-services.

## Requirements
- Install [Docker](https://docs.docker.com/) and start the docker deamon
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Import the project (using the default Gradle Wrapper and Java SDK 1.8)

## The databases 
The employee-service manages employee and department entities which are in relation with each other. Because of that, I decided to go for a relational database and I chose [postgreSQL](https://www.postgresql.org/).
The event-service on the other hand manage only event entities, which are in relation with no entity. Thus I decided to go for a document-oriented database and I picked [mongoDB](https://www.mongodb.com/).     

## The message broker
Both the employee- and the event-services share messages over a queue provider through [rabbitmq](https://www.rabbitmq.com/).

## Running the databases and the message broker
To have the both the databases and the message broker running, a docker daemon must have been started. 
Go to the project root folder and before doing anything else, be sure that your ports 15672, 5672, 5432 and 27017 have not been allocated or map the exposed docker ports to others host ports in the in `docker-compose.yml`
located in the project root folder. 
Then use the following command

```sh
$ docker-compose up -d
``` 
It will run the commands in `docker-compose.yml`. This will download all needed images and get the related containers running.
After starting the rabbitmq instance, check if the containers have effectively been well started by using the following command

```sh
$ docker-compose ps
```

the expected state of each running container instance must be *Up*. Following containers must be running:
- a container running the postgreSQL-database by the name of *employee_service_postgredb*
- a container running the mongoDB-database by the name of *event_service_mongodb* 
- a container running the rabbitmq-message-broker by the name of *rabbitmq*    

**Note:** The rabbitmq and database credentials can be found and adjusted in the respectively `yml` file of each service

## The employee-service and the employee event-service
Since starting the employee event-service will create all needed queues and exchanges on the rabbitmq server, it might appear more preferable to start the employee event-service first.   

**Note:** Each service port can also be found and adjusted in the `yml` file of each service. 
By default, the employee-service will be running at port 8081 and the employee event-service will be running at port 8083. 

### The employee event-service 
**Reminder:** Starting the employee event-service will get all needed queues and exchanges on the rabbitmq server automatically created
- To start the employee event-service, run the `eventservice` bootRun task.
- Once started, all employee events related endpoints can be triggered using <http://localhost:8083/api/v1/events/>
- The Swagger API can be inspected here <http://localhost:8083/swagger-ui.html>

### The employee-service
- To start the employee-service, run the `empoyeeservice` bootRun task.
- Once started, all employee related endpoints can be reached using <http://localhost:8081/api/v1/employees/> and all department related endpoints can be triggered using <http://localhost:8081/api/v1/departments/>
- The Swagger API can be inspected here <http://localhost:8081/swagger-ui.html>

## Testing
Feel free to use [Postman](https://www.getpostman.com/) to test the APIs.