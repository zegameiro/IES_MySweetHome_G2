FROM maven:3.8.5-openjdk-17

WORKDIR /msh-backend

COPY . .
COPY sensors /msh-backend/sensors

COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "rabbitmq:5672", "--", "mvn", "spring-boot:run"]

RUN cd /msh-backend/sensors && mvn install
RUN cd /msh-backend && mvn package

