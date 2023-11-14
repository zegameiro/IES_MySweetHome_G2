// não é suposto correr isto, corram o bash 
db.createUser(
 {
  user: "msh_ies",
  pwd: "iesg2",
  roles: [ { role: "readWrite", db: "mysweethome" } ]
 }
)

db.sensordata.createIndex( { "timestamp": 1 }, { expireAfterSeconds: 60 } )
