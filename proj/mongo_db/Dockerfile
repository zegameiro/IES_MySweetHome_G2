FROM mongo:4.4.4

COPY init_db/init_db.sh /docker-entrypoint-initdb.d/init_db.sh
COPY init_db/init_db.js /docker-entrypoint-initdb.d/init_db.js

RUN chmod +x /docker-entrypoint-initdb.d/init_db.sh