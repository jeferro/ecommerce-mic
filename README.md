# Ecommerce

Application for the management of products and their ratings.

There are 2 user profiles:
* Administrators are responsible for the management of products.
* Users can consult the information of a product and rate it.


## Development



### Requirements

We need the next tools to develop application:

* [ASDF](https://asdf-vm.com/)
* [Task](https://taskfile.dev)
* Docker
* Docker compose


### Installation

We should install project environment:

~~~bash
task install
~~~


### Build

Execute below command to build project:

~~~bash
task build
~~~~


### Test

Execute below command to execute test:

~~~bash
task test
~~~~


### Execute

Before execute project, we need to start environment:

~~~bash
task env-up
~~~

Then we can use below command to execute project:

~~~bash
task start
~~~

After that, Kafka UI is running locally: http://localhost:9021/


### Execute Scripts

If we want execute some script, we should configure .env file previously.
Copy the file "tools/scripts/.env.template" as "tools/scripts/.env" and set configurations.



## Shared library

The project is developed as a multi-module to simplify development, since the shared library should be in a separate repository to be reused in several projects.


##
# Shared library

The project is developed as a multi-module to simplify development, since the shared library should be in a separate repository to be reused in several projects.


## Components

* Auth: authentication, authorization and database audition
* DDD: basic classes required for the application of DDD in the project
* Locale: Add localization of the data
* Mappers: Basic classes of all mappers
* Time: Service to calculate current instant and test it Components

* Auth: authentication, authorization and database audition
* DDD: basic classes required for the application of DDD in the project
* Locale: Add localization of the data
* Mappers: Basic classes of all mappers
* Time: Service to calculate current instant and test it