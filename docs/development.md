## Development



## Requirements

We need the next tools to develop application:

* [ASDF](https://asdf-vm.com/)
* [Task](https://taskfile.dev)
* Docker
* Docker compose


## Installation

We should install project environment:

~~~bash
task install
~~~


## Execute

Before execute project, we need to start environment:

~~~bash
task env-start
~~~

Then we can use below command to execute project:

~~~bash
task start
~~~

After that, Kafka UI is running locally: http://localhost:9021/


## Test

Execute below command to execute test:

~~~bash
task test
~~~~
