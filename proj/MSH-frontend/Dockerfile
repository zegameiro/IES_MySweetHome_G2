

# Use an official Node.js runtime as a build image
FROM node:21-alpine3.17 as build-stage

# Set the working directory
WORKDIR /app

# Copy package.json and yarn.lock to the working directory
COPY package.json .
COPY yarn.lock .

# Install Node.js and Yarn dependencies
RUN apk add --no-cache --virtual .gyp python3 make g++ \
    && yarn \
    && apk del .gyp

# Copy the rest of the application code
COPY . .

# Build the application
RUN yarn build

# Use an official Nginx runtime as the production image
FROM nginx:stable-alpine as production-stage

# Copy the built app from the build stage to the Nginx web server directory
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expose the port the app will run on
EXPOSE 80

# Command to run the application
CMD ["nginx", "-g", "daemon off;"]

