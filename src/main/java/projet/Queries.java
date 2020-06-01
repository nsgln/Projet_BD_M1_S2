package projet;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.util.MapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Queries {
    // Query 1 la date est rentree en dur pour des raisons techiques
    // et la periode de temps est passee de 6 mois a 6 mois pour obtenir des resultats plus parlants.
    // La periode de temps est commentee pour les posts pour avoir des resultats
    // Le client considere est celui de cle 21990232560182.
    // La partie sur la categorie des produits n'est pas faite car pas comprise
    public static void querry1() {
        ArangoDB arango = new ArangoDB.Builder().build();
        ArangoDatabase db = arango.db("projet");
        try {
            System.out.println("Query 1 : BEGINNING");
            HashMap<String, Integer> tags = new HashMap<>();
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
                final String query5 = "FOR per in Person " +
                        "FILTER per._key == @key " +
                        "RETURN per";
                Map<String, Object> bindVars5 = new MapBuilder().put("key", aDocument.getKey()).get();
                final ArangoCursor<BaseDocument> cursor5 = db.query(query5, bindVars5, null, BaseDocument.class);
                cursor5.forEachRemaining(eDocument -> {
                    final String query6 = "FOR pHCp in post_hasCreator_person " +
                            "FILTER pHCp._to == @id " +
                            "RETURN pHCp";
                    Map<String, Object> bindVars6 = new MapBuilder().put("id", eDocument.getId()).get();
                    final ArangoCursor<BaseDocument> cursor6 = db.query(query6, bindVars6, null, BaseDocument.class);
                    cursor6.forEachRemaining(fDocument -> {
                        final String query7 = "FOR po in Post " +
                                "FILTER po._id == @post " + //AND po.CreateDate <= \"2020-06-01\" AND po.CreateDate >= DATE_SUBTRACT(\"2020-06-01\", 6, \"month\") " +
                                "RETURN po";
                        Map<String, Object> bindVars7 = new MapBuilder().put("post", fDocument.getAttribute("_from")).get();
                        final ArangoCursor<BaseDocument> cursor7 = db.query(query7, bindVars7, null, BaseDocument.class);
                        cursor7.forEachRemaining(gDocument -> {
                            System.out.println("    Post : ");
                            System.out.println("        key: " + gDocument.getKey());
                            System.out.println("        id: " + gDocument.getAttribute("id"));
                            System.out.println("        createDate: " + gDocument.getAttribute("createDate"));
                            System.out.println("        location: " + gDocument.getAttribute("location"));
                            System.out.println("        browserUsed: " + gDocument.getAttribute("browserUsed"));
                            System.out.println("        content: " + gDocument.getAttribute("content"));
                            System.out.println("        length: " + gDocument.getAttribute("length"));
                            final String query8 = "FOR pHt in post_hasTag_tag " +
                                    "FILTER pHt._from == @post " + //AND po.CreateDate <= \"2020-06-01\" AND po.CreateDate >= DATE_SUBTRACT(\"2020-06-01\", 6, \"month\") " +
                                    "RETURN pHt";
                            Map<String, Object> bindVars8 = new MapBuilder().put("post", gDocument.getId()).get();
                            final ArangoCursor<BaseDocument> cursor8 = db.query(query8, bindVars8, null, BaseDocument.class);
                            cursor8.forEachRemaining(hDocument -> {
                                final String query9 = "FOR t in Tag " +
                                        "FILTER t._id == @tag " + //AND po.CreateDate <= \"2020-06-01\" AND po.CreateDate >= DATE_SUBTRACT(\"2020-06-01\", 6, \"month\") " +
                                        "RETURN t";
                                Map<String, Object> bindVars9 = new MapBuilder().put("tag", hDocument.getAttribute("_to")).get();
                                final ArangoCursor<BaseDocument> cursor9 = db.query(query9, bindVars9, null, BaseDocument.class);
                                cursor9.forEachRemaining(iDocument -> {
                                    String tag = iDocument.getAttribute("id").toString();
                                    if(tags.containsKey(tag)) {
                                        int nb = tags.get(tag);
                                        tags.put(tag, nb + 1);
                                    }
                                    else {
                                        tags.put(tag, 1);
                                    }
                                });
                            });
                        });
                    });


                });

            });
            ArrayList<String> mostTags = new ArrayList<>();
            int max = 0;
            if (!tags.isEmpty()) {
                for (String key : tags.keySet()) {
                    if (tags.get(key) >= max) {
                        max = tags.get(key);
                    }
                }
                for (String key : tags.keySet()) {
                    if (tags.get(key) == max) {
                        mostTags.add(key);
                    }
                }
                for (String s : mostTags) {
                    System.out.println("Tag mostly used : " + s + " with " + max + " utilisations");
                }
            }
            System.out.println("Query 1 END");
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        querry1();
    }

    //Fonctionne pas, le run s'arrête pas
	private static ArangoCursor<BaseDocument> query4(ArangoDatabase db){
		String queryTheTwoPersons = "RETURN MERGE(FOR person IN person_0_0\n" +
				"FILTER person._id in(\n" +
				"FOR link IN person_knows_person_0_0\n" +
				"FILTER link.vertex in (\n" +
				"FOR inv IN @@collection\n" +
				"SORT inv.TotalPrice DESC\n" +
				"LIMIT 1\n" +
				"RETURN inv.PersonId)\n" +
				"RETURN link._to)\n" +
				"RETURN person, FOR person IN person_0_0\n" +
				"FILTER person._id in(\n" +
				"FOR link IN person_knows_person_0_0\n" +
				"FILTER link.vertex in (\n" +
				"FOR inv IN @@collection\n" +
				"SORT inv.TotalPrice DESC\n" +
				"LIMIT 1, 2\n" +
				"RETURN inv.PersonId)\n" +
				"RETURN link._to)\n" +
				"RETURN person)";
		ArangoCursor<BaseDocument> query = db.query(queryTheTwoPersons, new MapBuilder().put("@collection", "invoice").get(), new AqlQueryOptions(), BaseDocument.class);
		return query;
	}
}
