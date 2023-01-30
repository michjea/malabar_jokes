# malabar_jokes

Jokes website using Spring

## How to install and run

### Run database with docker

```bash
docker run --name mysql -p3306:3306  -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql
```

### Run with maven

```bash
mvn spring-boot:run
```

### Run tests

```bash
mvn test
```
