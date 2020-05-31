package projet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TransformFalseCSVToJSON {
	//Méthode permettant d'écrire le nouveau fichier avec le format json
	public static void writeJSON(JSONObject object, File toWrite) {
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(toWrite, true));
			write.write(object.toString());
			write.newLine();
			write.flush();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Méthode permettant de transformer le format fourni (ie avec les séparateurs |) en un format reconnaissable pour arangoDB
	public static JSONObject jsonTransformation(String attributes, String line) throws JSONException {
		final String SEPARATEUR = "\\|";
		String[] jsonAttributes = attributes.split(SEPARATEUR);
		String[] objectsFromLine = line.split(SEPARATEUR);

		JSONObject newJSONObject = new JSONObject();
		for(int i=0; i<jsonAttributes.length; i++) {
			newJSONObject.put(jsonAttributes[i], objectsFromLine[i]);
			if(jsonAttributes[i].equals("id")){
				newJSONObject.put("_key", objectsFromLine[i]);
			}
		}

		return newJSONObject;
	}
}
