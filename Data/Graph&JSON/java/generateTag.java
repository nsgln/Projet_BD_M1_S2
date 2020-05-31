import javafx.scene.effect.SepiaTone;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class generateTag {

    //Méthode permettant de transformer le format fourni (ie avec les séparateurs |) en un format reconnaissable pour arangoDB
    private static String getIDs(String line) {
        final String SEPARATEUR = "\\|";
        String[] objectsFromLine = line.split(SEPARATEUR);
        return objectsFromLine[1];
    }

    //on convertit en JSON
    private static JSONObject convertToJSON(String id) {

        JSONObject newJSONObject = new JSONObject();
        newJSONObject.put("_key", id);
        newJSONObject.put("id", id);

        return newJSONObject;

    }

    //on écrit dans un nouveau fichier
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

    //L'idée est de générer le fichier TAG inexistant à partir des ids des relations (edges)
    public static void main(String[] args) {

        BufferedReader reader;

        String TAG = "tag_0_0";
        ArrayList<String> tag_ids = new ArrayList<String>();


        //d'abord interest
        String HAS_INTEREST = "person_hasInterest_tag_0_0";
        try{
            reader = new BufferedReader(new FileReader("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\SocialNetwork\\" + HAS_INTEREST + ".csv"));
            String line = reader.readLine();
            String attributes = line;
            line = reader.readLine();
            System.out.println("Enregistrement des ids... 1/2");
            while(line != null){
                String id = getIDs(line);
                if(!tag_ids.contains(id)) tag_ids.add(id);
                line = reader.readLine();
            }

            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        String HAS_TAG = "post_hasTag_tag_0_0";

        try{
            reader = new BufferedReader(new FileReader("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\SocialNetwork\\" + HAS_TAG + ".csv"));
            String line = reader.readLine();
            String attributes = line;
            line = reader.readLine();
            System.out.println("Enregistrement des ids... 2/2");
            while(line != null){
                String id = getIDs(line);
                if(!tag_ids.contains(id)) tag_ids.add(id);
                line = reader.readLine();
            }

            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Conversion en cours");
        for(int i=0; i< tag_ids.size(); i++) {
            writeJSON(convertToJSON(tag_ids.get(i)), TAG);
        }
        System.out.println("Conversion finie !");
    }
}
