package projet;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;

public class Queries {
	public static void main(String[] args){
		ArangoDB arango = new ArangoDB.Builder().build();
		ArangoDatabase db = arango.db("projet");

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
