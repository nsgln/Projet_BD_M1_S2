package projet;

import java.io.*;
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

        String from_path_product = "Data/Graph&JSON/Product/Product.csv";
        String from_path_brand= "Data/Graph&JSON/Product/BrandByProduct.csv";

        String to_path = "Data/Graph&JSON/Product/NewProduct.csv";

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

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File("Data/Graph&JSON/Product"));
            processBuilder.command("cmd.exe", "/c", "arangoimport --file \"NewProduct.csv\" --type csv --collection \"product\" --server.database \"projet\" --server.password \"\" --create-collection true");
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
        }catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}


