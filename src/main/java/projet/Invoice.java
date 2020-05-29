package projet;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.Collection;

public class Invoice {
	public static void main(final String[] args){
		ArangoDB arangoDB = new ArangoDB.Builder().build();

		//Create database, if is not existing
		String dbName = "projet";
		String collectionName = "invoice";
		try{
			arangoDB.createDatabase(dbName);
			System.out.println("Database created: " + dbName);
		}catch (ArangoDBException e){
			System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
		}

		//Create the collection if is not existing
		try{
			CollectionEntity invoiceCollection = arangoDB.db(dbName).createCollection(collectionName);
			System.out.println("Collection created: " + invoiceCollection.getName());
		}catch (ArangoDBException arangoDBException){
			System.err.println("Failed to create collection: " + collectionName + "; " + arangoDBException.getMessage());
		}

		File xmlFile = new File("Data/Invoice/Invoice.xml");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
			String xmlString = "";
			String lineRead;

			System.out.println("**********READING FILE**********");

			while((lineRead = reader.readLine()) != null){
				if(!lineRead.equals("<Invoices>")){
					if(!lineRead.equals("    </Invoice.xml>")){
						xmlString += lineRead;
					}else{
						xmlString += lineRead;
						JSONObject jsonObject = XML.toJSONObject(xmlString);
						System.out.println("ON CONVERTIT EN JSON");

						ArangoCollection collection = arangoDB.db(dbName).collection(collectionName);
						DocumentCreateEntity<String> entity = collection.insertDocument(
								jsonObject.toString());
						String key = entity.getKey();
						System.out.println(key);
						xmlString = "";
					}
				}
			}
			System.exit(0);

		}catch (FileNotFoundException notFoundException){
			notFoundException.printStackTrace();
		}catch (IOException ioException){
			ioException.printStackTrace();
		}catch (JSONException jsonException){
			jsonException.printStackTrace();
		}
	}
}
