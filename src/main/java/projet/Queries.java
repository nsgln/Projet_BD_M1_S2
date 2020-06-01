package projet;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.AqlQueryOptions;
import com.arangodb.util.MapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Queries {
    public static void main(String[] args) {
        ArangoDB arango = new ArangoDB.Builder().build();
        ArangoDatabase db = arango.db("projet");

        // Query 1 la date est rentree en dur pour des raisons techiques
        // et la periode de temps est passee de 6 mois a 6 mois pour obtenir des resultats plus parlants.
        // Le client considere est celui de cle 21990232560182.
        // La partie sur la categorie des produits n'est pas faite car pas comprise
        try {
            String query = "FOR c IN customer FILTER c._key == @key RETURN c";
            Map<String, Object> bindVars = new MapBuilder().put("key", "21990232560182").get();
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("key: " + aDocument.getKey());
                System.out.println("fisrtName: " + aDocument.getAttribute("firstName"));
                System.out.println("lastName: " + aDocument.getAttribute("lastName"));
                System.out.println("gender: " + aDocument.getAttribute("gender"));
                System.out.println("birthday: " + aDocument.getAttribute("birthday"));
                System.out.println("createDate: " + aDocument.getAttribute("createDate"));
                System.out.println("location: " + aDocument.getAttribute("location"));
                System.out.println("browserUsed: " + aDocument.getAttribute("browserUsed"));
                System.out.println("place: " + aDocument.getAttribute("place"));
                System.out.println("Orders :");
                final String query2 = "FOR o in Order " +
                        "FILTER o.PersonId == @key AND o.OrderDate <= \"2020-06-01\" AND o.OrderDate >= DATE_SUBTRACT(\"2020-06-01\", 6, \"month\") " +
                        "RETURN o";
                Map<String, Object> bindVars2 = new MapBuilder().put("key", aDocument.getKey()).get();
                final ArangoCursor<BaseDocument> cursor2 = db.query(query2, bindVars2, null, BaseDocument.class);
                cursor2.forEachRemaining(bDocument -> {
                    System.out.println("    Order : ");
                    System.out.println("        key: " + bDocument.getKey());
                    System.out.println("        OrderId: " + bDocument.getAttribute("OrderId"));
                    System.out.println("        PersonId: " + bDocument.getAttribute("PersonId"));
                    System.out.println("        OrderDate: " + bDocument.getAttribute("OrderDate"));
                    System.out.println("        TotalPrice: " + bDocument.getAttribute("TotalPrice"));
                    System.out.println("        OrderLines: ");
                    final ArrayList<HashMap> array = (ArrayList<HashMap>)bDocument.getAttribute("Orderline");
                    for (HashMap h : array) {
                        System.out.println("            Orderline");
                        System.out.println("                productId: " + h.get("productId"));
                        System.out.println("                asin: " + h.get("asin"));
                        System.out.println("                title: " + h.get("title"));
                        System.out.println("                price: " + h.get("price"));
                        System.out.println("                brand: " + h.get("brand"));
                    }
                    final String query3 = "FOR i in invoice FILTER i._key == @key RETURN i";
                    Map<String, Object> bindVars4 = new MapBuilder().put("key", bDocument.getKey()).get();
                    final ArangoCursor<BaseDocument> cursor3 = db.query(query3, bindVars4, null, BaseDocument.class);
                    cursor3.forEachRemaining(cDocument -> {
                        System.out.println("        Invoice : ");
                        System.out.println("            key: " + cDocument.getKey());
                        System.out.println("            OrderId: " + cDocument.getAttribute("OrderId"));
                        System.out.println("            PersonId: " + cDocument.getAttribute("PersonId"));
                        System.out.println("            OrderDate: " + cDocument.getAttribute("OrderDate"));
                        System.out.println("            TotalPrice: " + cDocument.getAttribute("TotalPrice"));
                        System.out.println("            OrderLines: ");
                        final ArrayList<HashMap> array2 = (ArrayList<HashMap>) cDocument.getAttribute("Orderline");
                        for (HashMap h : array2) {
                            System.out.println("                Orderline");
                            System.out.println("                    productId: " + h.get("productId"));
                            System.out.println("                    asin: " + h.get("asin"));
                            System.out.println("                    title: " + h.get("title"));
                            System.out.println("                    price: " + h.get("price"));
                            System.out.println("                    brand: " + h.get("brand"));
                        }
                    });
                    for (HashMap h : array) {
                        final String asin3 = (String) h.get("asin");
                        final String query4 = "FOR f in feedback FILTER f._key == @asin RETURN f";
                        //"FILTER i._key == @key AND i.OrderDate <= \"2020-06-01\" AND i.OrderDate >= DATE_SUBTRACT(\"2020-06-01\", 6, \"month\") " +
                        //"RETURN i";
                        Map<String, Object> bindVars3 = new MapBuilder().put("asin", asin3).get();
                        final ArangoCursor<BaseDocument> cursor4 = db.query(query4, bindVars3, null, BaseDocument.class);
                        cursor4.forEachRemaining(dDocument -> {
                            System.out.println("        Feedback : ");
                            System.out.println("            key: " + dDocument.getKey());
                            System.out.println("            asin: " + dDocument.getAttribute("asin"));
                            final ArrayList<HashMap> array3 = (ArrayList<HashMap>) dDocument.getAttribute("values");
                            for (HashMap h3 : array3) {
                                if (h3.get("PersonId").equals(bDocument.getAttribute("PersonId"))) {
                                    System.out.println("                productId: " + h3.get("PersonId"));
                                    System.out.println("                asin: " + h3.get("feedback"));
                                }
                            }
                        });
                    }

                });

            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }


	/* Exemple query java
	ArangoDB arango = new ArangoDB.Builder().build();
	ArangoDatabase db = arango.db("myDB");
	ArangoCursor<BaseDocument> cursor = db.query(
  		"FOR i IN @@collection RETURN i"
  		new MapBuilder().put("@collection", "myCollection").get(),
  		new AqlQueryOptions(),
  		BaseDocument.class);
  	*/
    }
}
