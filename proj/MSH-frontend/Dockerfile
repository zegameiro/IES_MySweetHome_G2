FROM node:16.9.0-alpine3.12

WORKDIR /app

COPY package.json .
COPY package-lock.json .
COPY postcss.config.js .
COPY tailwind.config.js .
COPY yarn.lock .

RUN yarn

COPY . .

EXPOSE 3000

CMD ["yarn", "dev"]

