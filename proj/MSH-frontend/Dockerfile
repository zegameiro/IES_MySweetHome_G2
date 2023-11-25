FROM node:16.9.0-alpine3.12

WORKDIR /msh-frontend

COPY package.json /msh-frontend/
COPY package-lock.json /msh-frontend/
COPY postcss.config.js /msh-frontend/
COPY tailwind.config.js /msh-frontend/
COPY yarn.lock /msh-frontend/

COPY . .

RUN yarn

EXPOSE 3000

