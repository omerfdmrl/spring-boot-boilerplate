.PHONY: jar clear run build test clean

jar:
	./mvnw -N io.takari:maven:wrapper

clear:
	./mvnw liquibase:update -Dliquibase.clearCheckSums=true

run:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

build:
	./mvnw clean package

test:
	./mvnw test

clean:
	./mvnw clean

docker-build:
	docker build -t your-image-name:latest .

postgre:
	docker exec -it domainxbackend-postgres-1 psql -U youruser -d yourdb

redis:
	docker exec -it domainxbackend-redis-1 redis
