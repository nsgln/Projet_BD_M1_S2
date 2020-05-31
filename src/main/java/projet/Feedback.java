package projet;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.CollectionEntity;

import java.io.*;
import java.util.ArrayList;

import com.arangodb.entity.DocumentCreateEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class Feedback {

    public static void toJSON(File feedbackFile, File feedbackFileJSON) {
        if (!feedbackFileJSON.exists()) {
            try {
                final String SEPARATEUR = "\\|";
                String attributes = "asin|PersonId|feedback";
                String[] jsonAttributes = attributes.split(SEPARATEUR);
                String[] objectsFromLine;
                BufferedReader reader = new BufferedReader(new FileReader(feedbackFile));
                BufferedWriter write = new BufferedWriter(new FileWriter(feedbackFileJSON, true));
                System.out.println("**********CONVERSION EN COURS**********");
                String line = reader.readLine();
                String asin = "";
                boolean debut = false;
                if (line != null) {
                    debut = true;
                    objectsFromLine = line.split(SEPARATEUR);
                    asin = objectsFromLine[0];
                    JSONObject newJSONObject = new JSONObject();
                    newJSONObject.put(jsonAttributes[1], objectsFromLine[1]);
                    newJSONObject.put(jsonAttributes[2], objectsFromLine[2]);
                    write.write("{\"_key\":\"" + objectsFromLine[0] + "\", \"" + jsonAttributes[0] + "\":\"" + objectsFromLine[0] + "\", \"values\":[" +  newJSONObject);
                    line = reader.readLine();
                }
                while (line != null) {
                    objectsFromLine = line.split(SEPARATEUR);
                    JSONObject newJSONObject = new JSONObject();
                    newJSONObject.put(jsonAttributes[1], objectsFromLine[1]);
                    newJSONObject.put(jsonAttributes[2], objectsFromLine[2]);
                    if (objectsFromLine[0].equals(asin)) {
                        write.write(", " + newJSONObject);
                    } else {
                        asin = objectsFromLine[0];
                        write.write("]}");
                        write.newLine();
                        write.write("{\"_key\":\"" + objectsFromLine[0] + "\", \"" + jsonAttributes[0] + "\":\"" + objectsFromLine[0] + "\", \"values\":[" +  newJSONObject);
                    }
                    line = reader.readLine();

                }
                if (debut) {
                    write.write("]}");
                }
                write.newLine();
                write.flush();
                write.close();
                reader.close();

            } catch (FileNotFoundException notFoundException) {
                notFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (JSONException jsonException){
            jsonException.printStackTrace();
        }

        }
    }
    public static void main(final String[] args) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Create database, if is not existing
        String dbName = "projet";
        String collectionName = "feedback";
        try {
            arangoDB.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (ArangoDBException e) {
            System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
        }

        //Create the collection if is not existing
        try {
            CollectionEntity feedbackCollection = arangoDB.db(dbName).createCollection(collectionName);
            System.out.println("Collection created: " + feedbackCollection.getName());
        } catch (ArangoDBException arangoDBException) {
            arangoDB.db(dbName).collection(collectionName).drop();
            System.err.println("Failed to create collection: " + collectionName + "; " + arangoDBException.getMessage());
        }

        File feedbackFile = new File("Data/Feedback/feedback.csv");
        File feedbackFileJSON = new File("Data/Feedback/feedback.json");
        if (!feedbackFileJSON.exists()) {
            toJSON(feedbackFile, feedbackFileJSON);
        }
        System.out.println("**********CONVERSION TERMINEE**********");
        ArrayList keys = new ArrayList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(feedbackFileJSON));
            String lineRead;

            System.out.println("**********READING FILE**********");

            while ((lineRead = reader.readLine()) != null) {
                ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);
                DocumentCreateEntity<String> entity = collection.insertDocument(lineRead);
                String key = entity.getKey();
                keys.add(key);
                System.out.println(key);
            }
            reader.close();
            System.exit(0);

        } catch (FileNotFoundException notFoundException) {
            notFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
