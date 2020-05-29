package projet;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
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
				System.exit(0);
			} else {
				System.out.println("Trouble");
			}
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
