#Mise à jour des données : Collection vendor

arangosh --server.database projet --server.password ""
arangosh > v1 = db._collection("vendor").insert({Vendor : "Adidas", Country : "France", Industry : "Sports"})
arangosh > db._update(v1, {Country : "Bulgaria"})
arangosh > db._remove(v1, {overwrite: true})