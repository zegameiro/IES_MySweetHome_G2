FROM maven:3.8.5-openjdk-17

WORKDIR /msh-backend

COPY . .
COPY sensors /msh-backend/sensors

RUN cd /msh-backend/sensors && mvn install
RUN cd /msh-backend && mvn package

