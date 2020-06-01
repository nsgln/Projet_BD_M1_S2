import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class AddKeyToOrder {


        //on écrit dans un nouveau fichier en y ajoutant la key = OrderId
        private static void writeJSONwithKey(String key, JSONObject object, String pathToWrite) {
            try {
                BufferedWriter write=new BufferedWriter(new FileWriter(pathToWrite, true));
                object.put("_key", key);
                write.write(object.toString());
                write.newLine();
                write.flush();
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //L'idée est d'ajouter le champ _key au fichier Order.json
        public static void main(String[] args) {

            String from_path = "C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\Order\\Order.json";
            String to_path = "C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\NEWDATA\\Order\\Order.json";

            BufferedReader br = null;
            JSONParser parser = new JSONParser();
            try {

                String sCurrentLine;
                System.out.println("Ecriture en cours...");
                br = new BufferedReader(new FileReader(from_path));
                while ((sCurrentLine = br.readLine()) != null) {
                    Object obj;
                    try {
                        obj = parser.parse(sCurrentLine);
                        JSONObject jsonObject = (JSONObject) obj;
                        writeJSONwithKey((String) jsonObject.get("OrderId"), jsonObject, to_path);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Ajout fini !");
        }
    }

