## Delete lock file of Kafka server during development
Script to delete lock file due to bug in integrated Kafka server when stopping and running the project.

```
#!/bin/bash
rm -rf ./target/lagom-dynamic-projects/lagom-internal-meta-project-kafka/target/kafka_data
```

Add to your run/debug configuration (sbt Task) an external tool before launch.
For example, one can have the following parameters: Program to run `C:\Program Files\Git\bin\sh.exe`,
as arguments `-l "cleanKafka.sh"` and for the working directory, your `test-lagom` git folder.


## How to use an external Kafka for development
In order to use an external Kafka server, you will need to disable the embedded Kafka server that comes with Lagom.

The environment variable `DISABLE_EMBEDDED_KAFKA` allows you to do so.
No specific value need to be set but you can do `DISABLE_EMBEDDED_KAFKA=TRUE`.

Then you must specify an endpoint for the broker. You may also do so by using an environment variable.
Here is an example `LAGOM_BROKER_KAFKA_BROKERS=192.168.1.5:9092`.

