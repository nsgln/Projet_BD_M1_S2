import org.json.simple.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class AddBrandToProduct {

    //on ajoute dans une hashmap
    private static void getBrandsByAsin(HashMap<String, String> brandsbyAsin, String line) {
        final String SEPARATEUR = ",";
        String[] objectsFromLine = line.split(SEPARATEUR);
        brandsbyAsin.put(objectsFromLine[1], objectsFromLine[0]);
    }

    //méthode permettant d'écrire les données dans le csv en y ajoutant les brands
    private static void writeCSV(HashMap<String, String> brands, String[] attributes, String data, String writeTo, boolean first) throws IOException {

        FileWriter csvWriter = new FileWriter(writeTo, true);
        if(first) {
            csvWriter.append("_key");
            csvWriter.append(",");
            for(int i=0;i<4;i++) {
                csvWriter.append(attributes[i]);
                csvWriter.append(",");
            }
            csvWriter.append("brand");
            csvWriter.append("\n");
        }

        String[] tokens = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        csvWriter.append(tokens[0]);
        csvWriter.append(",");
        for(int i=0;i<4;i++) {
            csvWriter.append(tokens[i]);
            csvWriter.append(",");
        }
        if(brands.containsKey(tokens[0]))
        {
            csvWriter.append(brands.get(tokens[0]));
        }
        else
        {
            csvWriter.append("null");
        }
        csvWriter.append("\n");

        csvWriter.flush();
        csvWriter.close();
    }


    public static void main(String[] args) {
        BufferedReader reader;

        String from_path_product = "C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\Product\\Product.csv";
        String from_path_brand= "C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\DATA\\Product\\BrandByProduct.csv";

        String to_path = "C:\\Users\\Romain\\Desktop\\BD S2\\PROJET_NOSQL_2019_2020\\NEWDATA\\Product\\Product.csv";

        HashMap<String, String> brandsbyAsin= new HashMap<String, String>();

        try{

            reader = new BufferedReader(new FileReader(from_path_brand));
            String line = reader.readLine();
            System.out.println("lecture et sauvegarde en cours...");
            while(line != null){
                getBrandsByAsin(brandsbyAsin, line);
                line = reader.readLine();
            }
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(from_path_product));
            String row;
            row = csvReader.readLine();
            String[] attributes = row.split(",");
            boolean first = true;
            while ((row = csvReader.readLine()) != null) {
                writeCSV(brandsbyAsin, attributes, row, to_path, first);
                first = false;
            }
            csvReader.close();
            ;
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}


