package projet;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;

import java.io.*;

import org.json.JSONException;

public class Customer {
	public static void main(final String[] args) {
		ArangoDB arangoDB = new ArangoDB.Builder().build();

		//Create database, if is not existing
		String dbName = "projet";
		String collectionName = "customer";
		try {
			arangoDB.createDatabase(dbName);
			System.out.println("Database created: " + dbName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
		}

		//Create the collection if is not existing
		try {
			CollectionEntity customerCollection = arangoDB.db(dbName).createCollection(collectionName);
			System.out.println("Collection created: " + customerCollection.getName());
		} catch (ArangoDBException arangoDBException) {
			System.err.println("Failed to create collection: " + collectionName + "; " + arangoDBException.getMessage());
		}

		File customersFile = new File("Data/Customer/person_0_0.csv");
		File customerFileJSON = new File("Data/Customer/customer.json");
		if(!customerFileJSON.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(customersFile));
				String attribute = reader.readLine();
				String line = reader.readLine();
				System.out.println("**********CONVERSION EN COURS**********");
				while (line != null) {
					TransformFalseCSVToJSON.writeJSON(TransformFalseCSVToJSON.jsonTransformation(attribute, line), customerFileJSON);
					line = reader.readLine();
				}
				reader.close();

			} catch (FileNotFoundException notFoundException) {
				notFoundException.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} catch (JSONException jsonException) {
				jsonException.printStackTrace();
			}
		}
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.directory(new File("Data/Customer"));
			processBuilder.command("cmd.exe", "/c", "arangoimport --file \"customer.json\" --type json --collection \"customer\" --server.database \"projet\" --server.password \"\"");
			Process process = processBuilder.start();
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
			toAdd.addAttribute("id", "789098");
			toAdd.setKey("789098");
			toAdd.addAttribute("firstName", "Nina");
			toAdd.addAttribute("lastName", "Singlan");
			toAdd.addAttribute("gender", "female");
			toAdd.addAttribute("birthday", "1998-02-09");
			toAdd.addAttribute("creationDate", "2020-05-31T22:43:26.134+0000");
			toAdd.addAttribute("locationIP", "192.160.128.234");
			toAdd.addAttribute("browserUsed", "Chrome");
			toAdd.addAttribute("place", 611);
			arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
			System.out.println("Document inserted");
			String keyOfObject = toAdd.getKey();

			//Modification
			BaseDocument toUpdate = arangoDB.db(dbName).collection(collectionName).getDocument(keyOfObject,
					BaseDocument.class);
			toUpdate.updateAttribute("firstName", "Renée");
			System.out.println("Document modified");

			//Suppression.
			arangoDB.db(dbName).collection(collectionName).deleteDocument(keyOfObject);

			//Insertion d'un nouveau document!
			BaseDocument otherToAdd = new BaseDocument();
			toAdd.addAttribute("id", "789078");
			toAdd.setKey("789078");
			toAdd.addAttribute("firstName", "Romain");
			toAdd.addAttribute("lastName", "Michelucci");
			toAdd.addAttribute("gender", "male");
			toAdd.addAttribute("birthday", "1997-08-01");
			toAdd.addAttribute("creationDate", "2020-05-29T22:43:22.134+0000");
			toAdd.addAttribute("locationIP", "192.160.121.234");
			toAdd.addAttribute("browserUsed", "Firefox");
			toAdd.addAttribute("place", 802);
			arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
			System.out.println("Document inserted");
			String otherKeyOfObject = toAdd.getKey();

			//Modification
			BaseDocument otherToUpdate = arangoDB.db(dbName).collection(collectionName).getDocument(otherKeyOfObject,
					BaseDocument.class);
			toUpdate.updateAttribute("place", 301);
			System.out.println("Document modified");

			System.exit(0);

		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
