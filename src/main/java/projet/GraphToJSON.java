package projet;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.HashMap;

public class GraphToJSON {

    //Méthode permettant de transformer le format fourni (ie avec les séparateurs |) en un format reconnaissable pour arangoDB
    private static JSONObject jsonTransformation(String attributes, String line, boolean edge, String from, String to) {
        final String SEPARATEUR = "\\|";
        String[] jsonAttributes = attributes.split(SEPARATEUR);
        String[] objectsFromLine = line.split(SEPARATEUR);

        JSONObject newJSONObject = new JSONObject();

        if(edge) {
            //Collections à modifier à la main
            newJSONObject.put("_from", from + "/" + objectsFromLine[0]);
            newJSONObject.put("_to", to + "/" +objectsFromLine[1]);
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
    private static void writeJSON(JSONObject object, String pathToWrite) {
        try {
            BufferedWriter write=new BufferedWriter(new FileWriter(pathToWrite, true));
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

        HashMap<Integer, String[]> hashMapPath = new HashMap<>();

        String from_pathInterest = "Data/Graph&JSON/SocialNetwork/unzip/person_hasInterest_tag_0_0/" + HAS_INTEREST + ".csv";
        String to_pathInterest = "Data/Graph&JSON/SocialNetwork/unzip/person_hasInterest_tag_0_0/" + HAS_INTEREST + ".json";
        String[] toAdd = {from_pathInterest, to_pathInterest, "Person", "Tag"};
        hashMapPath.put(0, toAdd);

        String from_pathTag = "Data/Graph&JSON/SocialNetwork/unzip/post_hasCreator_person_0_0/" + HAS_CREATOR + ".csv";
        String to_pathTag = "Data/Graph&JSON/SocialNetwork/unzip/post_hasCreator_person_0_0/" + HAS_CREATOR + ".json";
        String[] toAdd1 = {from_pathTag, to_pathTag, "Post", "Creator"};
        hashMapPath.put(1, toAdd1);

        String from_pathCreator = "Data/Graph&JSON/SocialNetwork/unzip/post_hasTag_tag_0_0/" + HAS_TAG + ".csv";
        String to_pathCreator = "Data/Graph&JSON/SocialNetwork/unzip/post_hasTag_tag_0_0/" + HAS_TAG + ".json";
        String[] toAdd2 = {from_pathCreator, to_pathCreator, "Post", "Tag"};
        hashMapPath.put(2, toAdd2);

        String from_pathKnow = "Data/Graph&JSON/SocialNetwork/" + P_KNOWS_P + ".csv";
        String to_pathKnow = "Data/Graph&JSON/SocialNetwork/" + P_KNOWS_P + ".json";
        String[] toAdd3 = {from_pathKnow, to_pathKnow, "Person", "Person"};
        hashMapPath.put(3, toAdd3);

        String from_pathPost = "Data/Graph&JSON/SocialNetwork/" + POST + ".csv";
        String to_pathPost = "Data/Graph&JSON/SocialNetwork/" + POST + ".json";
        String[] toAdd4 = {from_pathPost, to_pathPost, "", ""};
        hashMapPath.put(4, toAdd4);

        String from_pathPerson = "Data/Customer/" + PERSON + ".csv";
        String to_pathPerson = "Data/Graph&JSON/SocialNetwork/" + PERSON + ".json";
        String[] toAdd5 = {from_pathPerson, to_pathPerson, "", ""};
        hashMapPath.put(5, toAdd5);

         try{
             for(int i = 0; i < 6; i++) {
                 String[] strings = hashMapPath.get(i);
                 String from_path = strings[0];
                 String to_path = strings[1];
                 String from = strings[2];
                 String to = strings[3];

                 boolean bool;
                 if(i <= 3){
                     bool = true;
                 }else{
                     bool = false;
                 }
                 if(! new File(to_path).exists()) {
                     reader = new BufferedReader(new FileReader(from_path));
                     String line = reader.readLine();
                     String attributes = line;
                     line = reader.readLine();
                     System.out.println("Conversion en cours...");
                     while (line != null) {
                         writeJSON(jsonTransformation(attributes, line, bool, from, to), to_path);
                         line = reader.readLine();
                     }
                     System.out.println("Conversion finie !");
                     reader.close();
                 }

                 ProcessBuilder processBuilder = new ProcessBuilder();

                 switch (i){
                     case 0:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork/unzip/person_hasInterest_tag_0_0"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"person_hasInterest_tag_0_0.json\" --type json --collection \"person_hasInterest_tag\" --server.database \"projet\" --server.password \"\" --create-collection true --create-collection-type edge");
                         break;
                     case 1:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork/unzip/post_hasTag_tag_0_0"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"post_hasTag_tag_0_0.json\" --type json --collection \"post_hasTag_tag\" --server.database \"projet\" --server.password \"\" --create-collection true --create-collection-type edge");
                         break;
                     case 2:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork/unzip/post_hasCreator_person_0_0"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"post_hasCreator_person_0_0.json\" --type json --collection \"post_hasCreator_person\" --server.database \"projet\" --server.password \"\" --create-collection true --create-collection-type edge");
                         break;
                     case 3:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"person_knows_person_0_0.json\" --type json --collection \"person_knows_person\" --server.database \"projet\" --server.password \"\" --create-collection true --create-collection-type edge");
                         break;
                     case 4:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"post_0_0.json\" --type json --collection \"Post\" --server.database \"projet\" --server.password \"\" --create-collection true");
                         break;
                     case 5:
                         processBuilder.directory(new File("Data/Graph&JSON/SocialNetwork"));
                         processBuilder.command("cmd.exe", "/c", "arangoimport --file \"person_0_0.json\" --type json --collection \"Person\" --server.database \"projet\" --server.password \"\" --create-collection true");
                         break;
                 }
                 try {
                     Process process = processBuilder.start();
                     StringBuilder output = new StringBuilder();
                     BufferedReader readerProcess = new BufferedReader(
                           new InputStreamReader(process.getInputStream()));
                     String line1;
                     while ((line1 = readerProcess.readLine()) != null) {
                         output.append(line1 + "\n");
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
                 } catch (InterruptedException interruptedException) {
                     interruptedException.printStackTrace();
                 }
             }
             System.exit(0);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
