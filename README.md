## Delete lock file of Kafka server
Script to delete lock file due to bug in integrated Kafka server when stopping and running the project.

```
#!/bin/bash
rm -rf ./target/lagom-dynamic-projects/lagom-internal-meta-project-kafka/target/kafka_data
```

Add to your run/debug configuration (sbt Task) an external tool before launch.
For example, one can have the following parameters: Program to run `C:\Program Files\Git\bin\sh.exe`,
as arguments `-l "cleanKafka.sh"` and for the working directory, your `test-lagom` git folder. 