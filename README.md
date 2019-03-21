# PROJECT
This project includes both the employee and the event backend-services.

## Requirements
- Install [Docker](https://docs.docker.com/) and start the docker deamon
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Import the project (using the default Gradle Wrapper and Java SDK 1.8)

## Database 
Even though Docker is great for running databases in a development environment, I chosen not to use it and I contented myself using
the in-memory-database `H2`for convenience reasons. 

## Start the rabbitmq server
Both the employee and the event backend-services share messages over a queue provider through [rabbitmq](https://www.rabbitmq.com/).
After moving to the project root folder, be sure to run the rabbitmq instance by using following command

```sh
$ docker-compose up
``` 
before doing anything else. It will run the commands in `docker-compose.yml`, downloading a rabbitmq image and running the related container.
After starting the rabbitmq instance, check if you can reach [http://localhost:15672/](http://localhost:15672) with with `rabbitmq` and `rabbitmq`.

**Note:** the rabbitmq credentials can be adjusted in the respectively `yml` files of each service

## Start the the employee and the event backend-services
There is no start order as to which service should be start first

**Note:** the respectively service port can be found and adjusted in the `yml` file of each service

### Start the the employee service
- To start the employee service, run the `empoyeeservice` bootRun task. The employee service will run at the port 8081.
- All employee related endpoints can be reached using <http://localhost:8081/api/v1/employees/...>
- All department related endpoints can be reached using <http://localhost:8081/api/v1/departments/...>
- You can inspect the Swagger API here <http://localhost:8081/swagger-ui.htm>

### Start the the employee-event service 
- To start the employee-event service, run the `eventservice` bootRun task. The employee-event will run at the port 8083. 
- All employee events related endpoints can be reached using <http://localhost:8083/events/v1/employees/...>
- You can inspect the Swagger API here <http://localhost:8083/swagger-ui.htm>