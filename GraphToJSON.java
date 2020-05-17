import org.json.simple.JSONObject;

import java.io.*;

public class GraphToJSON {

    //Méthode permettant de transformer le format fourni (ie avec les séparateurs |) en un format reconnaissable pour arangoDB
    public static JSONObject jsonTransformation(String attributes, String line) {
        final String SEPARATEUR = "\\|";
        String[] jsonAttributes = attributes.split(SEPARATEUR);
        String[] objectsFromLine = line.split(SEPARATEUR);

        JSONObject newJSONObject = new JSONObject();
        for(int i=0; i<jsonAttributes.length; i++) {
            newJSONObject.put(jsonAttributes[i], objectsFromLine[i]);
        }

        return newJSONObject;
    }

    //Méthode permettant d'écrire le nouveau fichier avec le format json
    public static void writeJSON(JSONObject object, String nameOfFile) {
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
        try{

            reader = new BufferedReader(new FileReader("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\SocialNetwork\\" + POST + ".csv"));
            String line = reader.readLine();
            String attributes = line;
            line = reader.readLine();
            System.out.println("Conversion en cours...");
            while(line != null){
                writeJSON(jsonTransformation(attributes, line), POST);
                line = reader.readLine();
            }
            System.out.println("Conversion finie !");
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
