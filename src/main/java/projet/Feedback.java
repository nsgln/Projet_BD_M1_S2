package projet;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;

import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Feedback {

    public static void addFeedback(String dbName, String collectionName, String asin, String PersonId, String feedback) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Create database, if is not existing
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
            arangoDB.db(dbName).createCollection(collectionName);
        }

        BaseDocument toAdd = new BaseDocument();
        toAdd.setKey(asin);
        toAdd.addAttribute("asin", asin);
        ArrayList arrayToAdd = new ArrayList();
        BaseDocument toAddArray = new BaseDocument();
        toAddArray.addAttribute("PersonId", PersonId);
        toAdd.addAttribute("feedback", feedback);
        arrayToAdd.add(toAddArray);
        toAdd.addAttribute("values", arrayToAdd);
        ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);

        //Try if the asin is already in the collection
        try {
            collection.insertDocument(toAdd);
            System.out.println("Insert done : " + asin + " with PersonId : " + PersonId + " has been inserted");
        }
        catch (ArangoDBException e) {
            try {
                BaseDocument doc = collection.getDocument(asin, BaseDocument.class);
                ArrayList<BaseDocument> array = (ArrayList<BaseDocument>)toAdd.getAttribute("values");
                array.add(toAddArray);
                doc.updateAttribute("values", array);
                System.out.println("Insert done : " + asin + " with PersonId : " + PersonId + " has been inserted");
            }
            catch (ArangoDBException e2 ) {
                e2.printStackTrace();
            }
        }
    }

    public static void updateFeedback(String dbName, String collectionName, String asin, String PersonId, String feedback, String newPersonId, String newFeedback) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Create database, if is not existing
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
            arangoDB.db(dbName).createCollection(collectionName);
        }

        BaseDocument modif = new BaseDocument();
        modif.setKey(asin);
        modif.addAttribute("PersonId", newPersonId);
        modif.addAttribute("feedback", feedback);
        ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);

        //Find the feedback and modify it in the collection
        try {
            BaseDocument doc = collection.getDocument(asin, BaseDocument.class);
            ArrayList<BaseDocument> array = (ArrayList<BaseDocument>)doc.getAttribute("values");
            for (BaseDocument d : array) {
                if (d.getAttribute("PersonID").equals(PersonId) && d.getAttribute("feedback").equals(feedback)){
                    array.remove(d);
                    array.add(modif);
                }
            }
            doc.updateAttribute("values", array);
            System.out.println("Update done : " + asin + " with PersonId : " + PersonId + " has changed");
        }
        catch (ArangoDBException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFeedback(String dbName, String collectionName, String asin) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Create database, if is not existing
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
            arangoDB.db(dbName).createCollection(collectionName);
        }

        ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);

        //Find the feedback and delete it
        try {
            collection.deleteDocument(asin);
            System.out.println("Deletion done : " + asin);
        }
        catch (ArangoDBException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFeedback(String dbName, String collectionName, String asin, String PersonId, String feedback) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();

        //Create database, if is not existing
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
            arangoDB.db(dbName).createCollection(collectionName);
        }

        ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);

        //Find the feedback and delete it
        try {
            BaseDocument doc = collection.getDocument(asin, BaseDocument.class);
            ArrayList<BaseDocument> array = (ArrayList<BaseDocument>)doc.getAttribute("values");
            for (BaseDocument d : array) {
                if (d.getAttribute("PersonID").equals(PersonId) && d.getAttribute("feedback").equals(feedback)){
                    array.remove(d);
                }
            }
            doc.updateAttribute("values", array);
            System.out.println("Deletion done : " + asin + "with PersonId" + PersonId);
        }
        catch (ArangoDBException e) {
            e.printStackTrace();
        }
    }

    public static BaseDocument copieDoc(BaseDocument doc) {
        BaseDocument copie = new BaseDocument();
        copie.addAttribute("PersonId", doc.getAttribute("PersonId").toString());
        copie.addAttribute("feedback", doc.getAttribute("feedback").toString());
        return copie;
    }

    public static void toJSON(File feedbackFile, File feedbackFileJSON) {
        if (!feedbackFileJSON.exists()) {
            try {
                final String SEPARATEUR = "\\|";
                String[] objectsFromLine;
                JSONObject newJSONObject;
                JSONArray array = new JSONArray();
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
                    JSONObject val = new JSONObject();
                    val.put("PersonId", objectsFromLine[1]);
                    val.put("feedback", objectsFromLine[2]);
                    array.add(val);
                    line = reader.readLine();
                }
                while (line != null) {
                    objectsFromLine = line.split(SEPARATEUR);
                    JSONObject val = new JSONObject();
                    val.put("PersonId", objectsFromLine[1]);
                    val.put("feedback", objectsFromLine[2]);
                    if (objectsFromLine[0].equals(asin)) {
                        array.add(val);
                    } else {
                        newJSONObject = new JSONObject();
                        newJSONObject.put("_key", asin);
                        newJSONObject.put("_id", asin);
                        newJSONObject.put("asin", asin);
                        newJSONObject.put("values", array);
                        write.write(newJSONObject.toString());
                        write.newLine();
                        asin = objectsFromLine[0];
                        array = new JSONArray();
                        array.add(val);                    }
                    line = reader.readLine();

                }
                if (debut) {
                    newJSONObject = new JSONObject();
                    newJSONObject.put("_key", asin);
                    newJSONObject.put("_id", asin);
                    newJSONObject.put("asin", asin);
                    newJSONObject.put("values", array);
                    write.write(newJSONObject.toString());
                }
                write.newLine();
                write.flush();
                write.close();
                reader.close();

            } catch (FileNotFoundException notFoundException) {
                notFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
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
            arangoDB.db(dbName).createCollection(collectionName);
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
            JSONParser parser = new JSONParser();

            Object obj;
            Object ar;
            BaseDocument toAdd;
            BaseDocument inArray;
            ArrayList array;

            System.out.println("**********READING FILE**********");

            while ((lineRead = reader.readLine()) != null) {
                try {
                    obj = parser.parse(lineRead);
                    JSONObject jsonObject = (JSONObject) obj;
                    toAdd = new BaseDocument();
                    array = new ArrayList();

                    toAdd.setKey(jsonObject.get("_key").toString());
                    toAdd.addAttribute("asin", jsonObject.get("asin").toString());

                    ar = jsonObject.get("values");
                    JSONArray jasonAr = (JSONArray) ar;
                    for (Object inAr : jasonAr) {
                        JSONObject jasonInAr = (JSONObject) inAr;
                        inArray = new BaseDocument();
                        inArray.addAttribute("PersonId", jasonInAr.get("PersonId"));
                        inArray.addAttribute("feedback", jasonInAr.get("feedback"));
                        array.add(copieDoc(inArray));
                    }
                    toAdd.addAttribute("values", array);
                    ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);
                    collection.insertDocument(toAdd);
                    System.out.println("Document inserted : " + toAdd.getKey());
                    /*for (BaseDocument d : (ArrayList<BaseDocument>)toAdd.getAttribute("values")) {
                        System.out.println(d.getAttribute("PersonId"));
                    }*/
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
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
