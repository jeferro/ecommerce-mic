
# Design


## Structure

The project consists of the following folders:

* APIs: 
  * Rest: openapi contracts
  * Avro: event contracts
* Code: source code of the Spring Boot microservice
* Docker: Docker container definitions
* Docs: documentation


## Source Code Package Structure

* name of the bounded context/

  * name of the module (functional grouping)/
  
    * name of the aggregate root/
    
      * domain/
        * repositories/
        * exceptions/
        * events/
        * inputs/
        * models/

      * application/
        * params/
          * Parameters of the use cases
        * Use cases of domain 

      * infrastructure/
        * use_cases/
          * params/
            * Parameters of the use cases
          * Use case of the administration operation

        * adapters/
          * (name of system)_(technology)/
            * dtos/
            * daos/
            * mappers/
            * services/
            * Adapters primaries or secondaries

## Design Pattern

The design and architecture patterns applied in the project are as follows:

* Domain Drive Design:
  * Value Object
  * Entity
  * Aggregate
  * Factory
  * Repository
  * Modules
  * Anti corruption Layer
  * Standard Types

* Hexagonal Architecture:
  * Port Adapters

* Others
  * Use Case Bus
