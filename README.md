# PROJECT
This project includes both the backend services.

## Requirements
- Install [Docker](https://docs.docker.com/) and start the docker deamon
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Import the project (using the default Gradle Wrapper and Java SDK 1.8)

## The database 
Even though Docker is great for running databases in a development environment, I chose not to use it and I contented myself using
the in-memory-database `H2` for convenience reasons. 

## The rabbitmq server
Both the employee and the event backend-services share messages over a queue provider through [rabbitmq](https://www.rabbitmq.com/).
Go to the project root folder and before doing anything else, be sure that your port 15672 has not been allocated. Then run the rabbitmq instance by using the following command

```sh
$ docker-compose up
``` 
It will run the commands in `docker-compose.yml`, download a rabbitmq image (rabbitmq:3-management) and get the related container running.
After starting the rabbitmq instance, check if you can reach [http://localhost:15672/](http://localhost:15672) with the credentials `rabbitmq` and `rabbitmq`.

**Note:** The rabbitmq credentials can be found and adjusted in the respectively `yml` file of each service

## The employee service and the employee event service
Since starting the employee event service will create all needed queues and exchanges on the rabbitmq server, it might appear more preferable to start the employee event service first.   

**Note:** The respectively service port can also be found and adjusted in the `yml` file of each service. 
By default, the employee service will be running at port 8081 and the employee event service will be running at port 8083. 

### The employee event service 
**Reminder:** Starting the employee event service will get all needed queues and exchanges on the rabbitmq server automatically created
- To start the employee event service, run the `eventservice` bootRun task.
- Once started, all employee events related endpoints can be triggered using <http://localhost:8083/api/v1/employees/>
- The Swagger API can be inspected here <http://localhost:8083/swagger-ui.html>

### The employee service
- To start the employee service, run the `empoyeeservice` bootRun task.
- Once started, all employee related endpoints can be reached using <http://localhost:8081/api/v1/employees/> and all department related endpoints can be triggered using <http://localhost:8081/api/v1/departments/>
- The Swagger API can be inspected here <http://localhost:8081/swagger-ui.html>

## Testing
Feel free to use [Postman](https://www.getpostman.com/) to test the APIs.