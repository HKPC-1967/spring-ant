## Draft (You can ignore this part, it's just for record and reference)

### Docker commands
1. Build the docker image with no cache
```shell
docker build -t spring_ant_backend:0.0.1 --no-cache .
```

2. PostgreSQL： restart unless-stopped, log rotation with max-size of 10m and max-file of 3
> Note: POSTGRES_DB=postgres sets the name of the default database to "postgres"
```shell
docker run --name postgres18 --restart unless-stopped --log-driver json-file --log-opt max-size=10m --log-opt max-file=3 -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:18
```

3. Spring And Backend： restart unless-stopped, log rotation with max-size of 10m and max-file of 3
> Note: `--spring.profiles.active=dev` can also work, and it should be placed at the end of the docker command.

```shell
docker run --restart=unless-stopped --log-driver json-file --log-opt max-size=10m --log-opt max-file=3 -d --add-host host.docker.internal:host-gateway -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod --name spring_ant_backend spring_ant_backend:0.0.1
```

4. Save the docker image, and load it to another machine
```
docker save spring_ant_backend -o spring_ant_backend.tar
```
```
docker load -i spring_ant_backend.tar
``` 


### Simplest docker build without multi-stage
```
FROM eclipse-temurin:17-jre-alpine

# 將工作目錄設置為 /app
WORKDIR /app

# 複製應用 jar 到容器中
COPY target/spring_ant_backend-0.0.1-SNAPSHOT.jar  app.jar

# 暴露 8080 端口，這樣在運行容器時可以通過該端口訪問應用程序
EXPOSE 8080

# 定義執行命令，每當容器啟動時運行該命令
CMD ["java", "-jar", "app.jar"]
```


