import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class Multi_Model_Data {

    public static void main(final String[] args) throws IOException, ParseException {

        final ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Création d'une base de données
        final String dbName = "multiModelDB";
        try{
            arangoDB.createDatabase(dbName);
            System.out.println("Base de donéées créee :" + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Echec de création de base de données " + dbName + "; " + e.getMessage());
        }

        //Création d'une collection
        String collectionName = "Order";
        try {
            CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(collectionName);
            System.out.println("Collection créee: " + myArangoCollection.getName());

        } catch (ArangoDBException e) {
            System.err.println("Echec de création de la collection: " + collectionName + "; " + e.getMessage());
        }

        /* 3.2 Modélisation et chargement de données partie JSON */

        //importation des données via cmd
        //arangoimport --server.database "multiModelDB" --file "Order.json" --type jsonl --collection "Order"
        //arangoimport --server.database "multiModelDB" --file "Product.csv" --type csv --collection "Product"
        //ou via l'interface Web Collections > Order > Upload documents from JSON file (button top bar)
        //Un document par ligne json (jsonl)

        /* 4.2 Maj de données partie JSON */

        //lire le premier document de la liste (en récupérant la _key générée aléatoirement lors de l'importation des données
        String myKey = "446428";
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionName).getDocument(myKey,
                    BaseDocument.class);
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("OrderId: " + myDocument.getAttribute("OrderId"));
            System.out.println("PersonId: " + myDocument.getAttribute("PersonId"));
            System.out.println("OrderDate: " + myDocument.getAttribute("OrderDate"));
            System.out.println("TotalPrice: " + myDocument.getAttribute("TotalPrice"));
            System.out.println("Orderline: " + myDocument.getAttribute("Orderline"));

        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture de document : " + e.getMessage());
        }

        //EXEMPLES DE MODIFICATIONS D'UN DOCUMENT

        //Ajout et modification d'attributs
        try {
            //on lit le document
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionName).getDocument(myKey, BaseDocument.class);
            //on ajoute un attribut
            myDocument.addAttribute("OrderType", "Sport");
            myDocument.updateAttribute("OrderDate", "2021-05-08");

            //on modifie le document
            arangoDB.db(dbName).collection(collectionName).updateDocument(myDocument.getKey(), myDocument);
        } catch (ArangoDBException e) {
            System.err.println("Echec de lecture ou d'écriture de document : " + e.getMessage());
        }
        //Vérification des modifications apportées sur le document
        try {
            BaseDocument myUpdatedDocument = arangoDB.db(dbName).collection(collectionName).getDocument(myKey,
                    BaseDocument.class);
            System.out.println("Key: " + myUpdatedDocument.getKey());
            System.out.println("OrderDate: " + myUpdatedDocument.getAttribute("OrderDate"));
            System.out.println("OrderType: " + myUpdatedDocument.getAttribute("OrderType"));
        } catch (ArangoDBException e) {
            System.err.println("Echec de récupération du document: " + e.getMessage());
        }

        //Création d'un document de la forme JSON
        BaseDocument myJSON = new BaseDocument();
        String myPersonalKey = "myOrderKey";
        myJSON.setKey(myPersonalKey);
        myJSON.addAttribute("OrderId", "arg24ar4-aze5-4ed5-7g82-bbcd15295fd7");
        myJSON.addAttribute("PersonId", "14578632459875");
        myJSON.addAttribute("OrderDate", "2059-08-01");
        myJSON.addAttribute("TotalPrice", "500.98");
        myJSON.addAttribute("Orderline", "[ { ");


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
            arangoDB.db(dbName).collection(collectionName).insertDocument(myJSON);
            System.out.println("Document crée");
        } catch (ArangoDBException e) {
            System.err.println("Echec de la création de document " + e.getMessage());
        }

        //Suppression d'un document (celui crée)
        try {
            arangoDB.db(dbName).collection(collectionName).deleteDocument(myPersonalKey);
        } catch (ArangoDBException e) {
            System.err.println("Echec de la suppression du document " + e.getMessage());
        }


    }

}
