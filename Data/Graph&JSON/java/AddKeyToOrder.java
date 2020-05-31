import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class AddKeyToOrder {



        //on écrit dans un nouveau fichier
        private static void writeJSONwithKey(String key, JSONObject object, String nameOfFile) {

            try {
                BufferedWriter write=new BufferedWriter(new FileWriter("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\NEWDATA\\Order\\" + nameOfFile + ".json", true));
                object.put("_key", key);
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


            String ORDER = "Order";
            BufferedReader br = null;
            JSONParser parser = new JSONParser();
            try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader("C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\Order\\" + ORDER + ".json"));
                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println("Record:\t" + sCurrentLine);
                    Object obj;
                    try {
                        obj = parser.parse(sCurrentLine);
                        JSONObject jsonObject = (JSONObject) obj;
                        writeJSONwithKey((String) jsonObject.get("OrderId"), jsonObject, ORDER);

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

