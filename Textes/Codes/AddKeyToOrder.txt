package projet;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.CollectionEntity;
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

            String from_path = "Data/Graph&JSON/Order/Order/Order.json";
            String to_path = "Data/Graph&JSON/Order/Order/NewOrder.json";
            String collectionName = "order";

            try {
                ArangoDB arangoDB = new ArangoDB.Builder().build();
                CollectionEntity customerCollection = arangoDB.db("projet").createCollection(collectionName);
                System.out.println("Collection created: " + customerCollection.getName());
            } catch (ArangoDBException arangoDBException) {
                System.err.println("Failed to create collection: " + collectionName + "; " + arangoDBException.getMessage());
            }

            BufferedReader br = null;
            JSONParser parser = new JSONParser();
            try {
                if(!new File(to_path).exists()) {
                    String sCurrentLine;
                    System.out.println("Ecriture en cours...");
                    br = new BufferedReader(new FileReader(new File(from_path)));
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
                }
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(new File("Data/Graph&JSON/Order/Order"));
                processBuilder.command("cmd.exe", "/c", "arangoimport --file \"NewOrder.json\" --type json --collection \"order\" --server.database \"projet\" --server.password \"\"");
                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();
                BufferedReader readerProcess = new BufferedReader(
                      new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = readerProcess.readLine()) != null) {
                    output.append(line + "\n");
                }
                readerProcess.close();
                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    System.out.println(output);
                    System.out.println("Success!");
                } else {
                    System.out.println(output);
                    System.out.println("Trouble");
                }
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
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

