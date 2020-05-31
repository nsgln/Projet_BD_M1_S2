ATTENTION :
Pour que les lignes de commandes s'executent bien faites attention au paramètre --file
Soit vous rajoutez le nom de dossier qui contient le fichier soit vous mettez les fichiers au même niveau que le arangoimport.exe (ce que je fais et c'est le paramètre actuel)

NOTES :
- Les fichiers de data ont tous été modifiés (à l'exception de Order) par de petits programmes java donc pour éviter de vous les faire générer je vous les fournit dans les dossiers correspondants.
- Le fichier java contient les programmes créés permettant de convertir, ajouter la _key ou encore merge les données en un format reconnaissable pour arangoDB

Voici les lignes d'import :

Partie Order :
//ORDER
arangoimport --server.database "multiModelDB" --file "Order.json" --type json --collection "Order" --create-collection true --ignore-missing

Partie Product :

//PRODUCT
arangoimport --server.database "multiModelDB" --file "Product.csv" --type csv --collection "Product" --create-collection true --ignore-missing

Partie SoicalNetwork :

/* VERTICES */
        //POST : 
arangoimport --server.database "multiModelDB" --file "post_0_0.json" --type json --collection "Post" --create-collection true --ignore-missing
        //PERSON : 
arangoimport --server.database "multiModelDB" --file "person_0_0.json" --type json --collection "Person" --create-collection true --ignore-missing
        //TAG : 
arangoimport --server.database "multiModelDB" --file "tag_0_0.json" --type json --collection "Tag" --create-collection true --ignore-missing

        /* EDGES */
        //HAS_TAG: 
arangoimport --server.database "multiModelDB" --file "post_hasTag_tag_0_0.json" --type json --collection "post_hasTag_tag" --create-collection true --create-collection-type edge --ignore-missing
        //HAS_INTEREST : 
arangoimport --server.database "multiModelDB" --file "person_hasInterest_tag_0_0.json" --type json --collection "person_hasInterest_tag" --create-collection true --create-collection-type edge --ignore-missing
        //HAS_CREATOR : 
arangoimport --server.database "multiModelDB" --file "post_hasCreator_person_0_0.json" --type json --collection "post_hasCreator_person" --create-collection true --create-collection-type edge --ignore-missing
        //KNOWS : 
arangoimport --server.database "multiModelDB" --file "person_knows_person_0_0.json" --type json --collection "person_knows_person" --create-collection true --create-collection-type edge --ignore-missing
