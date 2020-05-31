package projet;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Vendor {
	public static void main(final String[] args) {
		ArangoDB arangoDB = new ArangoDB.Builder().build();

		//Create database, if is not existing
		String dbName = "projet";
		String collectionName = "vendor";
		try {
			arangoDB.createDatabase(dbName);
			System.out.println("Database created: " + dbName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
		}

		//Create the collection if is not existing
		try {
			CollectionEntity vendorCollection = arangoDB.db(dbName).createCollection(collectionName);
			System.out.println("Collection created: " + vendorCollection.getName());
		} catch (ArangoDBException arangoDBException) {
			System.err.println("Failed to create collection: " + collectionName + "; " + arangoDBException.getMessage());
		}

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(new File("Data/Vendor"));
		processBuilder.command("cmd.exe", "/c", "arangoimport --file \"Vendor.csv\" --type csv --collection \"vendor\" --server.database \"projet\" --server.password \"\"");
		Process process = null;
		try {
			process = processBuilder.start();
			StringBuilder output = new StringBuilder();
			BufferedReader readerProcess = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = readerProcess.readLine()) != null) {
				output.append(line + "\n");
			}
			readerProcess.close();
			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
				System.out.println("Success!");
			} else {
				System.out.println("Trouble");
			}

			//Mise à jour des données en Java.
			//Insertion d'un nouveau document!
			BaseDocument toAdd = new BaseDocument();
			toAdd.addAttribute("Vendor", "Nike");
			toAdd.addAttribute("Country", "France");
			toAdd.addAttribute("Industry", "Sports");
			arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
			System.out.println("Document inserted");
			String keyOfObject = toAdd.getKey();

			//Modification
			BaseDocument toUpdate = arangoDB.db(dbName).collection(collectionName).getDocument(keyOfObject,
					BaseDocument.class);
			toUpdate.updateAttribute("Country", "Brazil");
			System.out.println("Document modified");

			//Suppression.
			arangoDB.db(dbName).collection(collectionName).deleteDocument(keyOfObject);
			System.out.println("Document deleted.");

			System.exit(0);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (InterruptedException interruptedException){
			interruptedException.printStackTrace();
		}
	}
}
