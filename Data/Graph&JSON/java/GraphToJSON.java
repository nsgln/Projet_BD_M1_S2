import org.json.simple.JSONObject;

import java.io.*;

public class GraphToJSON {

    //Méthode permettant de transformer le format fourni (ie avec les séparateurs |) en un format reconnaissable pour arangoDB
    private static JSONObject jsonTransformation(String attributes, String line, boolean edge) {
        final String SEPARATEUR = "\\|";
        String[] jsonAttributes = attributes.split(SEPARATEUR);
        String[] objectsFromLine = line.split(SEPARATEUR);

        JSONObject newJSONObject = new JSONObject();

        if(edge) {
            //Collections à modifier à la main
            newJSONObject.put("_from", "Post/" + objectsFromLine[0]);
            newJSONObject.put("_to", "Tag/" +objectsFromLine[1]);
            newJSONObject.put("vertex", objectsFromLine[0]);
            if(jsonAttributes.length>2) {
                for (int i = 2; i < jsonAttributes.length; i++) {
                    newJSONObject.put(jsonAttributes[i], objectsFromLine[i]);
                }
            }
        }
        else {
            //la clé est utile pour les collections edge !
            newJSONObject.put("_key", objectsFromLine[0]);
            for (int i = 0; i < jsonAttributes.length; i++) {
                newJSONObject.put(jsonAttributes[i], objectsFromLine[i]);
            }
        }

        return newJSONObject;
    }

    //Méthode permettant d'écrire le nouveau fichier avec le format json
    private static void writeJSON(JSONObject object, String nameOfFile) {
        try {
            BufferedWriter write=new BufferedWriter(new FileWriter("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\NEWDATA\\SocialNetwork\\" + nameOfFile + ".json", true));
            write.write(object.toString());
            write.newLine();
            write.flush();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        BufferedReader reader;
        String POST = "post_0_0";
        String PERSON = "person_0_0";
        String P_KNOWS_P = "person_knows_person_0_0";

        String HAS_CREATOR = "post_hasCreator_person_0_0";

        String TAG = "tag_0_0";
        String HAS_INTEREST = "person_hasInterest_tag_0_0";
        String HAS_TAG = "post_hasTag_tag_0_0";

         try{

            reader = new BufferedReader(new FileReader("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\SocialNetwork\\" + HAS_INTEREST + ".csv"));
            String line = reader.readLine();
            String attributes = line;
            line = reader.readLine();
            System.out.println("Conversion en cours...");
            while(line != null){
                writeJSON(jsonTransformation(attributes, line, true), HAS_INTEREST);
                line = reader.readLine();
            }
            System.out.println("Conversion finie !");
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
