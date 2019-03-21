# PROJECT
This project includes both the backend services.

## Requirements
- Install [Docker](https://docs.docker.com/) and start the docker deamon
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Import the project (using the default Gradle Wrapper and Java SDK 1.8)

## Database 
Even though Docker is great for running databases in a development environment, I chose not to use it and I contented myself using
the in-memory-database `H2` for convenience reasons. 

## Start the rabbitmq server
Both the employee and the event backend-services share messages over a queue provider through [rabbitmq](https://www.rabbitmq.com/).
Go to the project root folder and before doing anything else, be sure to run the rabbitmq instance by using following command

```sh
$ docker-compose up
``` 
It will run the commands in `docker-compose.yml`, downloading a rabbitmq image and running the related container.
After starting the rabbitmq instance, check if you can reach [http://localhost:15672/](http://localhost:15672) with `rabbitmq` and `rabbitmq`.

**Note:** the rabbitmq credentials can be found and adjusted in the respectively `yml` file of each service

## Start the the employee service and the employee event services
There is no start order as to which service should be start first

**Note:** the respectively service port can also be found and adjusted in the `yml` file of each service. 
By default, the employee service will be running at port 8081 and the employee event service will be running at port 8083. 

### The employee service
- To start the employee service, run the `empoyeeservice` bootRun task.
- All employee related endpoints can be reached using <http://localhost:8081/api/v1/employees/>
- All department related endpoints can be reached using <http://localhost:8081/api/v1/departments/>
- You can inspect the Swagger API here <http://localhost:8081/swagger-ui.htm>

### The employee-event service 
- To start the employee-event service, run the `eventservice` bootRun task.
- All employee events related endpoints can be reached using <http://localhost:8083/api/v1/employees/>
- You can inspect the Swagger API here <http://localhost:8083/swagger-ui.htm>

You can use [Postman](https://www.getpostman.com/) to test the API