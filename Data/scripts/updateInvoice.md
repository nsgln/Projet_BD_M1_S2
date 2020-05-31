#Mise à jour des données : Collection invoice

arangosh --server.database projet --server.password ""
arangosh > v1 = db._collection("invoice").insert({OrderId : "2zc21ae0-38dc-4zb3-9876-36d89e6f4c06", _key : "2zc21ae0-38dc-4zb3-9876-36d89e6f4c06",
 PersonId : "10995116278711", OrderData : "2010-09-21", TotalPrice : 229.09, Orderline : {productId : 6802, asin : "B000WY8ZHO", 
 title : "Luminox Men's Navy Seal ColorMark Watch 3051", price : 229.09, brand : "Onda_(sportswear)"}})
arangosh > db._update(v1, {PersonId : "21990232561173"})
arangosh > db._remove(v1, {overwrite: true})
