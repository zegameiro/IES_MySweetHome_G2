FROM node:16.9.0-alpine3.12 as build
WORKDIR /app

EXPOSE 3000

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json .
COPY package-lock.json .
COPY postcss.config.js .
COPY tailwind.config.js .
COPY yarn.lock .

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json .
COPY package-lock.json .
COPY postcss.config.js .
COPY tailwind.config.js .
COPY yarn.lock .

ENV REACT_APP_SERVER_URL http://deti-ies-17.ua.pt:3000

RUN npm install

COPY . .

RUN npm run build

FROM nginx:1.25.3

WORKDIR /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
