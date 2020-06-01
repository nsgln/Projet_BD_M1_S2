package projet;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.*;

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

		//Attention! Il faut ajouter ce fichier localement! Il est trop gros pour GitHub!
		File xmlFile = new File("Data/Invoice/Invoice.xml");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
			String xmlString = "";
			String lineRead;

			System.out.println("**********READING FILE**********");

			int n = 0;
			while((lineRead = reader.readLine()) != null){
				if(!lineRead.equals("<Invoices>")){
					if(!lineRead.equals("    </Invoice.xml>")){
						xmlString += lineRead;
					}else{
						xmlString += lineRead;
						JSONObject jsonObject = XML.toJSONObject(xmlString);
						JSONObject jsonObject1 = jsonObject.getJSONObject("Invoice.xml");
						BaseDocument toAdd = new BaseDocument();
						toAdd.addAttribute("PersonId", jsonObject1.getString("PersonId"));
						toAdd.addAttribute("TotalPrice", jsonObject1.getDouble("TotalPrice"));
						toAdd.addAttribute("OrderDate", jsonObject1.get("OrderDate"));
						toAdd.addAttribute("OrderId", jsonObject1.getString("OrderId"));
						toAdd.setKey(jsonObject1.getString("OrderId"));
						ArrayList<HashMap<String, Object>> orderLines = new ArrayList<>();
						try {
							JSONArray orderlines = jsonObject1.getJSONArray("Orderline");
							for (int i = 0; i < orderlines.length(); i++) {
								HashMap<String, Object> orderLine = new HashMap<>();
								JSONObject orderline = orderlines.getJSONObject(i);
								orderLine.put("productId", orderline.get("productId"));
								orderLine.put("price", orderline.get("price"));
								orderLine.put("asin", orderline.get("asin"));
								orderLine.put("title", orderline.get("title"));
								orderLine.put("brand", orderline.get("brand"));
								orderLines.add(orderLine);
							}
						}catch(JSONException notJSONArray){
							HashMap<String, Object> orderLine = new HashMap<>();
							JSONObject orderline = jsonObject1.getJSONObject("Orderline");
							orderLine.put("productId", orderline.get("productId"));
							orderLine.put("price", orderline.get("price"));
							orderLine.put("asin", orderline.get("asin"));
							orderLine.put("title", orderline.get("title"));
							orderLine.put("brand", orderline.get("brand"));
							orderLines.add(orderLine);
						}
						toAdd.addAttribute("Orderline", orderLines);
						System.out.println("ON CONVERTIT EN JSON");

						arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
						xmlString = "";
					}
				}
			}
			reader.close();

			//Mise à jour des données en Java.
			//Insertion d'un nouveau document!
			String xmlDocToAdd = "<Invoice.xml>"
					+ "<OrderId>2zc21ab0-38dc-4zb3-9195-36d89e6f4c06</OrderId>"
					+ "<PersonId>8796093032615</PersonId>"
					+ "<OrderDate>2020-04-17</OrderDate>"
					+ "<TotalPrice>641.93</TotalPrice>"
					+ "<Orderline>"
					+ "<productId>4856</productId>"
					+ "<asin>B001QD48GC</asin>"
					+ "<title>FreeForm Hideaway Home Gym</title>"
					+ "<price>641.93</price>"
					+ "<brand>ASICS</brand>"
					+ "</Orderline>"
					+"</Invoice.xml>";

		JSONObject jsonDocToAdd = null;
		try {
			jsonDocToAdd = XML.toJSONObject(xmlDocToAdd);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String keyOfObject = "";
			try {
				JSONObject jsonObject1 = jsonDocToAdd.getJSONObject("Invoice.xml");
				BaseDocument toAdd = new BaseDocument();
				toAdd.addAttribute("PersonId", jsonObject1.getString("PersonId"));
				toAdd.addAttribute("TotalPrice", jsonObject1.getDouble("TotalPrice"));
				toAdd.addAttribute("OrderDate", jsonObject1.get("OrderDate"));
				toAdd.addAttribute("OrderId", jsonObject1.getString("OrderId"));
				toAdd.setKey(jsonObject1.getString("OrderId"));
				ArrayList<HashMap<String, Object>> orderLines = new ArrayList<>();
				try {
					JSONArray orderlines = jsonObject1.getJSONArray("Orderline");
					for (int i = 0; i < orderlines.length(); i++) {
						HashMap<String, Object> orderLine = new HashMap<>();
						JSONObject orderline = orderlines.getJSONObject(i);
						orderLine.put("productId", orderline.get("productId"));
						orderLine.put("price", orderline.get("price"));
						orderLine.put("asin", orderline.get("asin"));
						orderLine.put("title", orderline.get("title"));
						orderLine.put("brand", orderline.get("brand"));
						orderLines.add(orderLine);
					}
				}catch(JSONException notJSONArray){
					HashMap<String, Object> orderLine = new HashMap<>();
					JSONObject orderline = jsonObject1.getJSONObject("Orderline");
					orderLine.put("productId", orderline.get("productId"));
					orderLine.put("price", orderline.get("price"));
					orderLine.put("asin", orderline.get("asin"));
					orderLine.put("title", orderline.get("title"));
					orderLine.put("brand", orderline.get("brand"));
					orderLines.add(orderLine);
				}
				toAdd.addAttribute("Orderline", orderLines);

				arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
				System.out.println("Document inserted");
				keyOfObject = toAdd.getKey();
			} catch (ArangoDBException e) {
				System.err.println("Failed to create document. " + e.getMessage());
			}

			//Si le document a été inséré.
			if(!keyOfObject.equals("")) {
				//On le modifie.
				BaseDocument documentToUpdate = arangoDB.db(dbName).collection(collectionName).getDocument(keyOfObject,
						BaseDocument.class);
				ArrayList<HashMap<String, Object>> orderlines = (ArrayList<HashMap<String, Object>>) documentToUpdate.getAttribute("Orderline");
				HashMap<String, Object> orderlineToAdd = new HashMap<>();
				orderlineToAdd.put("productId", 7603);
				orderlineToAdd.put("price", 199.0);
				orderlineToAdd.put("asin", "B0017SC9H6");
				orderlineToAdd.put("title", "Zero Tolerance Combat Folding Knife");
				orderlineToAdd.put("brand", "Tramontana_(sports_car)");
				Double previousTotal = (Double)documentToUpdate.getAttribute("TotalPrice");
				documentToUpdate.updateAttribute("TotalPrice", previousTotal + 199.0);
				ArrayList<HashMap<String, Object>> totalOrderline = new ArrayList<>();
				totalOrderline.addAll(orderlines);
				totalOrderline.add(orderlineToAdd);
				documentToUpdate.addAttribute("Orderline", totalOrderline);
				System.out.println("Document modified.");

				//Suppression du document inséré.
				try {
					arangoDB.db(dbName).collection(collectionName).deleteDocument(keyOfObject);
					System.out.println("Document deleted.");
				} catch (ArangoDBException e) {
					System.err.println("Failed to delete document. " + e.getMessage());
				}
			}

			//Insertion d'un nouveau document!
			String otherXmlDocToAdd = "<Invoice.xml>"
					+ "<OrderId>2zc21ae0-38dc-4zb3-9876-36d89e6f4c06</OrderId>"
					+ "<PersonId>10995116278711</PersonId>"
					+ "<OrderDate>2010-09-21</OrderDate>"
					+ "<TotalPrice>229.09</TotalPrice>"
					+ "<Orderline>"
					+ "<productId>6802</productId>"
					+ "<asin>B000WY8ZHO</asin>"
					+ "<title>Luminox Men's Navy Seal ColorMark Watch 3051</title>"
					+ "<price>229.09</price>"
					+ "<brand>Onda_(sportswear)</brand>"
					+ "</Orderline>"
					+"</Invoice.xml>";

			JSONObject otherJsonDocToAdd = null;
			String otherKeyOfObject = "";

			try {
				otherJsonDocToAdd = XML.toJSONObject(otherXmlDocToAdd);
				JSONObject jsonObject1 = otherJsonDocToAdd.getJSONObject("Invoice.xml");
				BaseDocument toAdd = new BaseDocument();
				toAdd.addAttribute("PersonId", jsonObject1.getString("PersonId"));
				toAdd.addAttribute("TotalPrice", jsonObject1.getDouble("TotalPrice"));
				toAdd.addAttribute("OrderDate", jsonObject1.get("OrderDate"));
				toAdd.addAttribute("OrderId", jsonObject1.getString("OrderId"));
				toAdd.setKey(jsonObject1.getString("OrderId"));
				ArrayList<HashMap<String, Object>> orderLines = new ArrayList<>();
				try {
					JSONArray orderlines = jsonObject1.getJSONArray("Orderline");
					for (int i = 0; i < orderlines.length(); i++) {
						HashMap<String, Object> orderLine = new HashMap<>();
						JSONObject orderline = orderlines.getJSONObject(i);
						orderLine.put("productId", orderline.get("productId"));
						orderLine.put("price", orderline.get("price"));
						orderLine.put("asin", orderline.get("asin"));
						orderLine.put("title", orderline.get("title"));
						orderLine.put("brand", orderline.get("brand"));
						orderLines.add(orderLine);
					}
				}catch(JSONException notJSONArray){
					HashMap<String, Object> orderLine = new HashMap<>();
					JSONObject orderline = jsonObject1.getJSONObject("Orderline");
					orderLine.put("productId", orderline.get("productId"));
					orderLine.put("price", orderline.get("price"));
					orderLine.put("asin", orderline.get("asin"));
					orderLine.put("title", orderline.get("title"));
					orderLine.put("brand", orderline.get("brand"));
					orderLines.add(orderLine);
				}
				toAdd.addAttribute("Orderline", orderLines);

				arangoDB.db(dbName).collection(collectionName).insertDocument(toAdd);
				System.out.println("Document inserted");
				otherKeyOfObject = toAdd.getKey();
			} catch (JSONException e) {
				System.err.println("Failed to create document. " + e.getMessage());
			}

			//Si le document a été inséré.
			if(!otherKeyOfObject.equals("")) {
				//On le modifie.
				BaseDocument otherDocumentToUpdate = arangoDB.db(dbName).collection(collectionName).getDocument(otherKeyOfObject,
						BaseDocument.class);
				otherDocumentToUpdate.updateAttribute("PersonId", "21990232561173");

				System.out.println("Document modified.");

				//Suppression du document inséré.
				try {
					arangoDB.db(dbName).collection(collectionName).deleteDocument(otherKeyOfObject);
					System.out.println("Document deleted.");
				} catch (ArangoDBException e) {
					System.err.println("Failed to delete document. " + e.getMessage());
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
