# My Sweet Home
## Our repository for the project of the IES class of 2023/2024 in UA

<p align="center">
    <img src="https://i.imgur.com/y7WgwFp.jpg" height="300px">
</p>

&nbsp;

[![All Contributors](https://img.shields.io/badge/Contributors-4-brightgreen.svg?style=for-the-badge)](#contributors-)
[![PRs Welcome](https://img.shields.io/badge/Open%20Issues-8-orange.svg?style=for-the-badge)](http://makeapullrequest.com)
[![PRs Welcome](https://img.shields.io/badge/Closed%20Issues-57-blue.svg?style=for-the-badge)](http://makeapullrequest.com)

## Description
### My Sweet Home <br> Home Automation Management System
 - Our aim with My Sweet Home is to develop a system where a user can associate input devices in his house to certain actions.
 - This project involves several receivers (inputs, such as thermostats, daylight-sensors, biometric-sensors, etc...) to be associated to output actions defined by the user and executed by a set of output devices.
 - The actions would be tailor made for each specific user and would need to verify if, for example, a user's action would interfere with another user's preference, like if a user turns on music at 11:00h but another user which we know is inside the same house doesn't want to be bothered by music until later.
 - One example of a simple task would be to close the windows if the temperature got too cold for the specification of the users inside the house, or to turn pass the music streaming of a user's phone to the house's speakers after he arrives home.
 - In the front-end we would be able to set these acttion-reaction pairs.

## Architecture Diagram

<img src = "reports/Report_Diagrams/architecture_diagram.png">

## How to run

Our production environment is up and running most of the time at http://deti-ies-17.ua.pt ( you will need to be inside UA's network or with VPN for this to work )

If you wanna run the project locally, clone the main branch and do the following:

```bash
cd proj
docker compose up
```
You will need to have your localhost tcp port 3000 free for this to work or you may change this port in the MSH-frontend dockerfile.

Then, simply navigate to localhost:3000 to access the front-end application.

## Docker implementation
The project will run in a 4 container setup, all connected via a predefined set of ports and all managed by a single docker compose implementation.
The containers used are as follows:

- Message Broker container: RabbitMQ runs on ports 5672 and 15672

- Database container: MongoDB runs on port 27017;

- Backend container: The Spring Boot API runs on port 8080 along with some default sensors;

- Frontend container: React + Vite runs on port 3000 and the NGINX server proxy runs on port 80;

## API Documentation
    You can acess our api documentation while running the project locally:
    - http://localhost:8080/swagger-ui/index.html

    Or when the production environment is running:
    - http://deti-ies-17:8080/swagger-ui/index.html

## Sensor integration
One of the principles for this project is making sensor integration as simple as possible, with very few hard coded parts.
For this purpose, the communication between sensors and the relevant back-end processes are entirely made through RabbitMQ messages.
This allows our final sensor integration to be language agnostic and for all makes of sensors to be able to communicate to our service, as long as the sensor is capable of throwing out RabbitMQ messages.

Our default back-end comes with some extra java classes inside that are created on spring bootup and act just
like regular sensors, to make sure we always have some running.

To integrate outside sensors, we first need to send a register message to the RabbitMQ queue.
All messages should be sent to the "sensor_queue" queue, with an empty "exchange" parameter and "sensor_queue" as the routing key, and all message contents must be in s JSON string format.
The register message should have the following fields:
```
{
    "register_msg": "1",
    "device_id": -device_UUID-, 
    "device_category": -category_integer-, 
    "device_name": -external_device_name-
    "reading_type": -information_description-
}
```
Example:
```
{
    "register_msg": "1",
    "device_id": generateUUID(), 
    "device_category": 1, 
    "device_name": "Electricity Monitor"
    "reading_type": "Eletricity Usage"
}
```
---
After the register message is sent, we can send as much data messages as we want, as long as these fields are present:
```
{
    "device_id": -device_uuid-, 
    "timestamp": -EPOCH_integer-, 
    "sensor_information": -double_value-, 
    "unit": -string_unit-
}
```
Example:
```
{
    "device_id": self.uuid, 
    "timestamp": 1702750526, 
    "sensor_information": 0.2163785, 
    "unit": "Kwh"
}
```

The back-end is responsible for sensor handling in case of communication loss with the sensors and only provides real values to the front-end. 

## Bookmarks

### Github repository
- https://github.com/zegameiro/IES_MySweetHome_G2
### Project Specification Report
- https://uapt33090-my.sharepoint.com/:w:/g/personal/rodrigoaguiar96_ua_pt/Eclxg-hVsX1AjO4ZlODAlHwBhfHHpHcjRj9Xb4AU41awEA
### Github Project
- https://github.com/users/zegameiro/projects/1

### Figma Mockup
- https://www.figma.com/file/1usB0nu0174e6QMF6IsxAc/My-Sweet-Home---IES?type=design&node-id=0%3A1&mode=design&t=O570V5XX2cdaccRS-1
### Figma Prototype
- https://www.figma.com/proto/1usB0nu0174e6QMF6IsxAc/My-Sweet-Home---IES?page-id=0%3A1&type=design&node-id=36-4&viewport=243%2C-92%2C0.16&t=fd7dMR9t0w2jyHMB-1&scaling=scale-down&starting-point-node-id=36%3A4&mode=design


## Our Team ✨

<!-- ALL-CONTRIBUTORS-LIST:START -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>

<td align="center" width="150px;"></td>
<td align="center"><a href="https://github.com/P-Ramos16"><img src="https://avatars0.githubusercontent.com/P-Ramos16?v=3" width="150px;" alt="Ramos"/><br /><sub><b>Pedro Ramos</b><br><i>107348</i></sub></a><hr><b>Architect</b><br><a href="https://github.com/P-Ramos16" title="Code">💻</a> <a href="https://github.com/codesandbox/codesandbox-client/commits?author=CompuIves" title="Tests">⚠️</a> <a href="#tool-CompuIves" title="Tools">🔧</a></td>
    <td align="center"><a href="https://github.com/Dan1m4D"><img src="https://avatars0.githubusercontent.com/Dan1m4D?v=3" width="150px;" alt="Madureira"/><br /><sub><b>Daniel Madureira</b><br><i>107603</i></sub></a><hr><b>Team Manager</b><br><a href="https://github.com/Dan1m4D" title="Code">💻</a><a href="#design-CompuIves" title="Design">🎨</a><a href="#tool-CompuIves" title="Tools">🔧</a></td>
    <td align="center"><a href="https://github.com/zegameiro"><img src="https://avatars0.githubusercontent.com/zegameiro?v=3" width="150px;" alt="Gameiro"/><br /><sub><b>José Gameiro</b><br><i>108840</i></sub></a><hr><b>DevOps Master</b><br><a href="https://github.com/zegameiro" title="Code">💻</a><a href="#blog-CompuIves" title="Blogposts">📝</a><a href="#tool-CompuIves" title="Tools">🔧</a></td>
    <td align="center"><a href="https://github.com/FiNeX96"><img src="https://avatars0.githubusercontent.com/FiNeX96?v=3" width="150px;" alt="Aguiar"/><br /><sub><b>Rodrigo Aguiar</b><br><i>108969</i></sub></a><hr><b>Product Owner</b><br><a href="https://github.com/FiNeX96" title="Code">💻</a><a href="#tool-MergeMaestro" title="Tools">🔀</a><a href="#tool-CompuIves" title="Tools">🔧</a></td>
<td align="center" width="150px;"></td>
</tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->
