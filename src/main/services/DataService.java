package main.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.models.Company;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class DataService {

    public Map<String, String> createMap(String url, List<String> keys, List<String> values) {
        if (keys.size() != values.size())
            throw new RuntimeException("--- Lists for "+url+" have different sizes ---");
        Iterator<String> keysIterator = keys.iterator();
        Iterator<String> valuesIterator = values.iterator();
        Map<String, String> createdMap = new HashMap<>();
        while(keysIterator.hasNext() && valuesIterator.hasNext())
            createdMap.put(
                    keysIterator.next(),
                    valuesIterator.next()
            );
        return createdMap;
    }

    public void saveDataInFile(List<Company> companies) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File("databasefile.csv"))) {

            StringBuilder sb = new StringBuilder();
            sb.append("Company Name,");
            sb.append("P/E Map,");
            sb.append("ROA Map");
            sb.append('\n');

            writer.write(sb.toString());

            for (Company company : companies)
                writer.write(convertObjectToCSVString(company));
            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private String convertObjectToCSVString(Company company) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        sb.append(company.getName());
        sb.append(",");
        sb.append(gson.toJson(company.getPricesPerEarning()));
        sb.append(",");
        sb.append(gson.toJson(company.getReturnsOfAssets()));
        sb.append("\n");
        return sb.toString();
    }

    public List<Company> getDataFromFile() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("databasefile.csv"));
        List<Company> companies = new ArrayList<>();
        String row;
        csvReader.readLine();
        while ((row = csvReader.readLine()) != null) {
            String[] attributeVals = row.split(",");
            companies.add(convertAttributesToObject(attributeVals));
        }
        csvReader.close();
        return companies;
    }

    private Company convertAttributesToObject(String[] attributes) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return Company.builder()
                .name(attributes[0])
                .pricesPerEarning(gson.fromJson(attributes[1], type))
                .returnsOfAssets(gson.fromJson(attributes[2], type))
                .build();
    }
}
