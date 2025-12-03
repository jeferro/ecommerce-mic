## Development



## Requirements

We need the next tools to develop application:

* [ASDF](https://asdf-vm.com/)
* [Task](https://taskfile.dev)
* Podman
* Podman compose


## Installation

We should install project environment:

~~~bash
task install
~~~


## Build

Execute below command to build project:

~~~bash
task build
~~~~


## Test

Execute below command to execute test:

~~~bash
task test
~~~~


## Execute

Before execute project, we need to start environment:

~~~bash
task env-up
~~~

Then we can use below command to execute project:

~~~bash
task start
~~~

After that, Kafka UI is running locally: http://localhost:9021/



## Execute Scripts

If we want execute some script, we should configure .env file previously. 
Copy the file "tools/scripts/.env.template" as "tools/scripts/.env" and set configurations.

