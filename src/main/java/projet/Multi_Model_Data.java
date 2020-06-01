package projet;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Multi_Model_Data {


    public static void main(final String[] args) {

        final ArangoDB arangoDB = new ArangoDB.Builder().build();

        /* CREATION DE LA BASE DE DONNEES */
        final String dbName = "projet";
        /*
        try{
            arangoDB.createDatabase(dbName);
            System.out.println("Base de donéées créee :" + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Echec de création de base de données " + dbName + "; " + e.getMessage());
        }
        */

        /* _______3.2 Modélisation et chargement de données partie JSON________ */

        //Liste des collections
        String collectionOrderName = "Order";
        String collectionProductName = "Product";

        //Importation des données en ligne de commandes
        //arangoimport --server.database "multiModelDB" --file "Order.json" --type jsonl --collection "Order" --create-collection true --ignore-missing
        //OU via l'interface Web Collections > Order > Upload documents from JSON file (button top bar)
        //Un document par ligne json (jsonl)

        //arangoimport --server.database "multiModelDB" --file "Product.csv" --type csv --collection "Product" --create-collection true --ignore-missing

        /* 4.2 Maj de données partie JSON */

        System.out.println("Collection ORDER :");

        /* LECTURE D'UN DOCUMENT */

        //lire un document en récupérant la _key = OrderId
        String myKey = "016f6a4a-ec18-4885-b1c7-9bf2306c76d6";
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionOrderName).getDocument(myKey,
                    BaseDocument.class);
            System.out.println("\nLecture d'un document importé: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("OrderId: " + myDocument.getAttribute("OrderId"));
            System.out.println("PersonId: " + myDocument.getAttribute("PersonId"));
            System.out.println("OrderDate: " + myDocument.getAttribute("OrderDate"));
            System.out.println("TotalPrice: " + myDocument.getAttribute("TotalPrice"));
            System.out.println("Orderline: " + myDocument.getAttribute("Orderline"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document: " + e.getMessage());
        }

        /* EXEMPLE DE MODIFICATIONS D'UN DOCUMENT */

        //Ajout et modification d'attributs
        try {
            //on lit le document
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionOrderName).getDocument(myKey, BaseDocument.class);
            //on ajoute un attribut
            myDocument.addAttribute("OrderType", "Sport");
            //on modifie un attribut
            myDocument.updateAttribute("OrderDate", "2021-05-08");
            //on modifie le document
            arangoDB.db(dbName).collection(collectionOrderName).updateDocument(myDocument.getKey(), myDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }
        //Vérification des modifications apportées sur le document
        try {
            BaseDocument myUpdatedDocument = arangoDB.db(dbName).collection(collectionOrderName).getDocument(myKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document après modification: ");
            System.out.println("Key: " + myUpdatedDocument.getKey());
            System.out.println("OrderDate: " + myUpdatedDocument.getAttribute("OrderDate"));
            System.out.println("OrderType: " + myUpdatedDocument.getAttribute("OrderType"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de récupération du document: " + e.getMessage());
        }

        /* CREATION D'UN DOCUMENT */

        //Création d'un document de la forme JSON
        BaseDocument myJSON = new BaseDocument();
        String myPersonalKey = "arg24ar4-aze5-4ed5-7g82-bbcd15295fd7";
        myJSON.setKey(myPersonalKey);
        myJSON.addAttribute("OrderId", "arg24ar4-aze5-4ed5-7g82-bbcd15295fd7");
        myJSON.addAttribute("PersonId", "14578632459875");
        myJSON.addAttribute("OrderDate", "2059-08-01");
        myJSON.addAttribute("TotalPrice", "500.98");

        JSONArray array = new JSONArray();
        JSONObject firstProduct = new JSONObject();
        firstProduct.put("productId", "5468");
        firstProduct.put("asin", "V456456ADA");
        firstProduct.put("title", "Boring Flamethrower");
        firstProduct.put("price", "300.15");
        firstProduct.put("brand", "Boring");
        array.add(firstProduct);
        JSONObject secondProduct = new JSONObject();
        secondProduct.put("productId", "5544");
        secondProduct.put("asin", "V456456KJKJ43");
        secondProduct.put("title", "Space Car Miniature");
        secondProduct.put("price", "200.83");
        secondProduct.put("brand", "SpaceX");
        array.add(secondProduct);

        myJSON.addAttribute("Orderline", array);
        try {
            arangoDB.db(dbName).collection(collectionOrderName).insertDocument(myJSON);
            System.out.println("\nDocument créé dans la collection order avec la _key " + myPersonalKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        //Lecture du document Order créé
        try {
            BaseDocument myNewDocument = arangoDB.db(dbName).collection(collectionOrderName).getDocument(myPersonalKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myNewDocument.getKey());
            System.out.println("OrderId: " + myNewDocument.getAttribute("OrderId"));
            System.out.println("PersonId: " + myNewDocument.getAttribute("PersonId"));
            System.out.println("OrderDate: " + myNewDocument.getAttribute("OrderDate"));
            System.out.println("TotalPrice: " + myNewDocument.getAttribute("TotalPrice"));
            System.out.println("Orderline: " + myNewDocument.getAttribute("Orderline"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document: " + e.getMessage());
        }

        /* SUPPRESSION D'UN DOCUMENT */

        //Suppression d'un document (celui créé)
        try {
            arangoDB.db(dbName).collection(collectionOrderName).deleteDocument(myPersonalKey);
            System.out.println("\nSuppression du document créé de _key " + myPersonalKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }


        System.out.println("\n\nCollection PRODUCT :");

        /* CREATION D'UN DOCUMENT */

        //Création d'un document type Product
        BaseDocument myProductJSON = new BaseDocument();
        String myPersonalProductKey = "B00006IK87";
        myProductJSON.setKey(myPersonalProductKey);
        myProductJSON.addAttribute("asin", "B00006IK87");
        myProductJSON.addAttribute("imgUrl", "https://cnet4.cbsistatic.com/img/DXdF_hHVb1lm-KRgGZo-1_Bfloo=/940x0/2018/01/26/acff5cc9-37d3-4986-a720-9acf1214ca39/boringcompanyflamethrower.jpg");
        myProductJSON.addAttribute("price", "200.99");
        myProductJSON.addAttribute("title", "Boring FlameThrower");
        try {
            arangoDB.db(dbName).collection(collectionProductName).insertDocument(myProductJSON);
            System.out.println("\nDocument créé dans la collection Product de _key " + myPersonalProductKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        /* EXEMPLE DE MODIFICATIONS D'UN DOCUMENT */

        //Modification d'attribut
        try {
            //on lit le document
            BaseDocument myProductDocument = arangoDB.db(dbName).collection(collectionProductName).getDocument(myPersonalProductKey, BaseDocument.class);
            //on modifie un attribut
            myProductDocument.updateAttribute("price", "250.58");
            //on modifie le document
            arangoDB.db(dbName).collection(collectionProductName).updateDocument(myProductDocument.getKey(), myProductDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }

        /* LECTURE D'UN DOCUMENT */

        //Lecture du document après modification
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionProductName).getDocument(myPersonalProductKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document modifié: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("asin: " + myDocument.getAttribute("asin"));
            System.out.println("imgUrl: " + myDocument.getAttribute("imgUrl"));
            System.out.println("price: " + myDocument.getAttribute("price"));
            System.out.println("title: " + myDocument.getAttribute("title"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        /* SUPPRESSION D'UN DOCUMENT */

        try {
            System.out.println("\nSuppression du document de _key " + myPersonalProductKey);
            arangoDB.db(dbName).collection(collectionProductName).deleteDocument(myPersonalProductKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }


        /* 3.5 Modélisation et chargement de données partie Graphes */

        //Travail en amont
        //Création d'une classe java generateTag qui permet de générer la Tag_0_0.json à partir des IDs de HasInterest et HasTag
        //Création d'une classe java GraphToJSON qui permet de modifier le format des fichiers fournis pour ArangoDB

        //Création de la structure de données en graphe en ligne de commande :

        /* VERTICES */
        //POST : arangoimport --server.database "multiModelDB" --file "post_0_0.json" --type json --collection "Post" --create-collection true --ignore-missing
        //PERSON : arangoimport --server.database "multiModelDB" --file "person_0_0.json" --type json --collection "Person" --create-collection true --ignore-missing
        //TAG : arangoimport --server.database "multiModelDB" --file "tag_0_0.json" --type json --collection "Tag" --create-collection true --ignore-missing

        /* EDGES */
        //HAS_TAG: arangoimport --server.database "multiModelDB" --file "post_hasTag_tag_0_0.json" --type json --collection "post_hasTag_tag" --create-collection true --create-collection-type edge --ignore-missing
        //HAS_INTEREST : arangoimport --server.database "multiModelDB" --file "person_hasInterest_tag_0_0.json" --type json --collection "person_hasInterest_tag" --create-collection true --create-collection-type edge --ignore-missing
        //HAS_CREATOR : arangoimport --server.database "multiModelDB" --file "post_hasCreator_person_0_0.json" --type json --collection "post_hasCreator_person" --create-collection true --create-collection-type edge --ignore-missing
        //KNOWS : arangoimport --server.database "multiModelDB" --file "person_knows_person_0_0.json" --type json --collection "person_knows_person" --create-collection true --create-collection-type edge --ignore-missing

       /* CREATION DU GRAPHE
        ArangoDB arango = new ArangoDB.Builder().build();
        ArangoDatabase db = arango.db("multiModelDB");

        EdgeDefinition edgeDefinition = new EdgeDefinition()
                .collection("person_knows_person")
                .from("Person")
                .to("Person");
        EdgeDefinition edgeDefinition2 = new EdgeDefinition()
                .collection("person_hasInterest_tag")
                .from("Person")
                .to("Tag");
        EdgeDefinition edgeDefinition3 = new EdgeDefinition()
                .collection("post_hasTag_tag")
                .from("Post")
                .to("Tag");
        EdgeDefinition edgeDefinition4 = new EdgeDefinition()
                .collection("post_hasCreator_person")
                .from("Post")
                .to("Person");
        GraphEntity graph = db.createGraph(
                "SocialNetwork", Arrays.asList(edgeDefinition, edgeDefinition2, edgeDefinition3, edgeDefinition4), new GraphCreateOptions()
        );
        */


         /* ___________________________________________________________________________________________________________________________________________*/
        System.out.println("\n\nCollection POST :");

        String collectionPostName = "Post";
        //selection d'un id de Post car _key est associé à id
        String myPostKey = "1236950581248";
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPostName).getDocument(myPostKey,
                    BaseDocument.class);
            System.out.println("\nLecture d'un document importé: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("imageFile: " + myDocument.getAttribute("imageFile"));
            System.out.println("length: " + myDocument.getAttribute("length"));
            System.out.println("locationIP: " + myDocument.getAttribute("locationIP"));
            System.out.println("language: " + myDocument.getAttribute("language"));
            System.out.println("browserUsed: " + myDocument.getAttribute("browserUsed"));
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("creationDate: " + myDocument.getAttribute("creationDate"));
            System.out.println("content: " + myDocument.getAttribute("content"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }


        //Création d'un document type Post
        BaseDocument myPostJSON = new BaseDocument();
        String myPersonalPostKey = "45687";
        myPostJSON.setKey(myPersonalPostKey);
        myPostJSON.addAttribute("id","45687");
        myPostJSON.addAttribute("creationDate", "2020-05-31T13:45:12.542+0000");
        myPostJSON.addAttribute("content", "About Niagara falls, I love them !");
        myPostJSON.addAttribute("imageFile", "photoNiagara.png");
        myPostJSON.addAttribute("length", "25");
        myPostJSON.addAttribute("language", "en");
        myPostJSON.addAttribute("locationIP", "12.123.54.125");
        myPostJSON.addAttribute("browserUsed", "Opera");
        try {
            arangoDB.db(dbName).collection(collectionPostName).insertDocument(myPostJSON);
            System.out.println("\nDocument créé dans la collection Post de _key " + myPersonalPostKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPostName).getDocument(myPersonalPostKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("imageFile: " + myDocument.getAttribute("imageFile"));
            System.out.println("length: " + myDocument.getAttribute("length"));
            System.out.println("locationIP: " + myDocument.getAttribute("locationIP"));
            System.out.println("language: " + myDocument.getAttribute("language"));
            System.out.println("browserUsed: " + myDocument.getAttribute("browserUsed"));
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("creationDate: " + myDocument.getAttribute("creationDate"));
            System.out.println("content: " + myDocument.getAttribute("content"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }


        //Modification d'attribut
        try {
            //on lit le document
            BaseDocument myPostDocument = arangoDB.db(dbName).collection(collectionPostName).getDocument(myPersonalPostKey, BaseDocument.class);
            //on modifie un attribut -> fr -> en
            myPostDocument.updateAttribute("language", "fr");
            myPostDocument.updateAttribute("content", "Au sujet des chutes du Niagara, je les adore !");
            //on modifie le document
            arangoDB.db(dbName).collection(collectionPostName).updateDocument(myPostDocument.getKey(), myPostDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }

        //Lecture du document après modification
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPostName).getDocument(myPersonalPostKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document modifié: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("language: " + myDocument.getAttribute("language"));
            System.out.println("content: " + myDocument.getAttribute("content"));


        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        try {
            arangoDB.db(dbName).collection(collectionPostName).deleteDocument(myPersonalPostKey);
            System.out.println("\nDocument supprimé de _key " + myPersonalPostKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }

        System.out.println("\n\nCollection PERSON: ");


        String collectionPersonName = "Person";
        //selection d'un id de Person car _key est associé à id
        String myPersonKey = "8491";
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPersonName).getDocument(myPersonKey,
                    BaseDocument.class);
            System.out.println("\nLecture d'un document importé: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("birthday: " + myDocument.getAttribute("birthday"));
            System.out.println("firstName: " + myDocument.getAttribute("firstName"));
            System.out.println("lastName: " + myDocument.getAttribute("lastName"));
            System.out.println("gender: " + myDocument.getAttribute("gender"));
            System.out.println("browserUsed: " + myDocument.getAttribute("browserUsed"));
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("creationDate: " + myDocument.getAttribute("creationDate"));
            System.out.println("locationIP: " + myDocument.getAttribute("locationIP"));
            System.out.println("place: " + myDocument.getAttribute("place"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        BaseDocument myPersonJSON = new BaseDocument();
        String myPersonalPersonKey = "100000000001";
        myPersonJSON.setKey(myPersonalPersonKey);
        myPersonJSON.addAttribute("id","100000000001");
        myPersonJSON.addAttribute("creationDate", "2020-05-31T18:45:12.542+0000");
        myPersonJSON.addAttribute("place", "159");
        myPersonJSON.addAttribute("gender", "male");
        myPersonJSON.addAttribute("firstName", "Maxime");
        myPersonJSON.addAttribute("lastName", "Carrez");
        myPersonJSON.addAttribute("locationIP", "1.193.52.185");
        myPersonJSON.addAttribute("browserUsed", "Opera");
        myPersonJSON.addAttribute("birthday", "1997-02-25");
        try {
            arangoDB.db(dbName).collection(collectionPersonName).insertDocument(myPersonJSON);
            System.out.println("\nDocument créé dans la collection Person de _key " + myPersonalPersonKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPersonName).getDocument(myPersonalPersonKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("birthday: " + myDocument.getAttribute("birthday"));
            System.out.println("firstName: " + myDocument.getAttribute("firstName"));
            System.out.println("lastName: " + myDocument.getAttribute("lastName"));
            System.out.println("gender: " + myDocument.getAttribute("gender"));
            System.out.println("browserUsed: " + myDocument.getAttribute("browserUsed"));
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("creationDate: " + myDocument.getAttribute("creationDate"));
            System.out.println("locationIP: " + myDocument.getAttribute("locationIP"));
            System.out.println("place: " + myDocument.getAttribute("place"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        //Modification d'attribut
        try {
            //on lit le document
            BaseDocument myPersonDocument = arangoDB.db(dbName).collection(collectionPersonName).getDocument(myPersonalPersonKey, BaseDocument.class);
            //on modifie un attribut -> fr -> en
            myPersonDocument.updateAttribute("place", "101");
            myPersonDocument.updateAttribute("browserUsed", "Chrome");
            //on modifie le document
            arangoDB.db(dbName).collection(collectionPersonName).updateDocument(myPersonDocument.getKey(), myPersonDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }

        //Lecture du document après modification
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionPersonName).getDocument(myPersonalPersonKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document modifié: ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("place: " + myDocument.getAttribute("place"));
            System.out.println("browserUsed: " + myDocument.getAttribute("browserUsed"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        try {
            arangoDB.db(dbName).collection(collectionPersonName).deleteDocument(myPersonalPersonKey);
            System.out.println("\nDocument supprimé de _key " + myPersonalPersonKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }


        System.out.println("\n\nCollection TAG :");

        String collectionTagName = "Tag";

        BaseDocument myTagJSON = new BaseDocument();
        String myPersonalTagKey = "100006500001";
        myTagJSON.setKey(myPersonalTagKey);
        myTagJSON.addAttribute("id","100006500001");
        myTagJSON.addAttribute("title", "A lonely title");

        try {
            arangoDB.db(dbName).collection(collectionTagName).insertDocument(myTagJSON);
            System.out.println("\nDocument créé dans la collection Tag de _key " + myPersonalTagKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionTagName).getDocument(myPersonalTagKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("title: " + myDocument.getAttribute("title"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        BaseDocument mySecondTagJSON = new BaseDocument();
        String mySecondPersonalTagKey = "1111111111111";
        mySecondTagJSON.setKey(mySecondPersonalTagKey);
        mySecondTagJSON.addAttribute("id","1111111111111");
        mySecondTagJSON.addAttribute("title", "L'invasion des uns");

        try {
            arangoDB.db(dbName).collection(collectionTagName).insertDocument(mySecondTagJSON);
            System.out.println("\nDocument créé dans la collection Tag de _key " + mySecondPersonalTagKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionTagName).getDocument(mySecondPersonalTagKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("title: " + myDocument.getAttribute("title"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        //suppression du second
        try {
            arangoDB.db(dbName).collection(collectionTagName).deleteDocument(mySecondPersonalTagKey);
            System.out.println("\nDocument supprimé de _key " + mySecondPersonalTagKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }

        try {
            //on lit le document
            BaseDocument myTagDocument = arangoDB.db(dbName).collection(collectionTagName).getDocument(myPersonalTagKey, BaseDocument.class);
            //on modifie un attribut -> fr -> en
            myTagDocument.updateAttribute("id", "100005500001");
            myTagDocument.updateAttribute("title", "A very lonely title");
            //on modifie le document
            arangoDB.db(dbName).collection(collectionTagName).updateDocument(myTagDocument.getKey(), myTagDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionTagName).getDocument(myPersonalTagKey,
                    BaseDocument.class);
            System.out.println("\nLecture du document créé : ");
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("id: " + myDocument.getAttribute("id"));
            System.out.println("title: " + myDocument.getAttribute("title"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        //suppression du premier (créé et modifié)
        try {
            arangoDB.db(dbName).collection(collectionTagName).deleteDocument(myPersonalTagKey);
            System.out.println("\nDocument supprimé de _key " + myPersonalTagKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }


        arangoDB.shutdown();
    }


}
