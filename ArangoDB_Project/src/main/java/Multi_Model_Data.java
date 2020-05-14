import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class Multi_Model_Data {

    public static void main(final String[] args) throws IOException, ParseException {

        final ArangoDB arangoDB = new ArangoDB.Builder().build();

        //créer base de données
        final String dbName = "multiModelDB";
        try{
            arangoDB.createDatabase(dbName);
            System.out.println("Base de donéées créee :" + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Echec de création de base de données " + dbName + "; " + e.getMessage());
        }

        //Collections
        String collectionName = "Order";
        try {
            CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(collectionName);
            System.out.println("Collection créee: " + myArangoCollection.getName());

        } catch (ArangoDBException e) {
            System.err.println("Echec de création de la collection: " + collectionName + "; " + e.getMessage());
        }

        //importing Document via cmd
        //arangoimport --file "Order.json" --type jsonl --collection Order
        //ou via l'interface Web Collections > Order > Upload documents from JSON file (button top bar)

        //read the document imported
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionName).getDocument("orderKey",
                    BaseDocument.class);
            System.out.println("Key: " + myDocument.getKey());
            System.out.println("Attribute :" + myDocument.getAttribute("OrderTime"));
        } catch (ArangoDBException e) {
            System.err.println("Failed to get document: myKey; " + e.getMessage());
        }


    }




}
