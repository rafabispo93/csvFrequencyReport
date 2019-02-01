/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author rafael
 */
public class ReportGenerator {
    
    boolean firstTime = true;
    HashMap<String, String>  data = new HashMap<String, String>();
    List<String> headersList = new ArrayList<String>();
    List<HashMap<String, String>> finalData = new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> structuredData;
    List<HashMap<String, String>> csvData = new ArrayList();
    
    /**
     * Method that reads the csv and convert to a a Java data structure to be analysed
     * @param inputFilePath the path for the csv file
     */
    public void processInputFile(String inputFilePath) throws IOException {
        
        FileReader csvDataReader = new FileReader(inputFilePath); // opening the csv file
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withQuote(null); // not considering commas inside quotation marks as csv columns splitters 
        CSVParser parser = new CSVParser(csvDataReader, csvFileFormat);
        List<CSVRecord> recordsList = StreamSupport.stream(parser.spliterator(), false).collect(Collectors.toList()); // generating a List with each row from Csv file
        
        // For each row from csv is created a hashMap with the header from each column as key and its value 
        for (int x=0; x < recordsList.size(); ++x) {
            Map<String, String> myMap = new HashMap<String, String>();
            if(!this.structureData(recordsList.get(x)).isEmpty()) {
                myMap.putAll((HashMap<String, String>) this.structureData(recordsList.get(x)));
                csvData.add((HashMap<String, String>) myMap);
            }
            
        }
        this.generateReport(csvData);
        
        

    }
    
    
    /**
     * Method that reads each csv row and transform it into a HashMap
     * @param record a row from the read csv
     */
    private HashMap<String, String> structureData(CSVRecord record) {
        
        // identify the header of each column
        if (this.firstTime == true) {
            for(int counter = 0; counter < record.size(); ++counter) {
               this.headersList.add(record.get(counter));
            }
            this.firstTime = false;   

        } else {
            //For each header identified associates it with its value            
            for(int counter = 0; counter < record.size(); ++counter) {
                if (counter < this.headersList.size()) {
                    String keyName = this.headersList.get(counter);
                    this.data.put(keyName,record.get(counter));
                        if (!this.finalData.contains(this.data)) {
                            this.finalData.add(this.data);
                        }
                }


            }
        }
        if (this.finalData.size() > 0){
            return this.finalData.get(0);
        }
        Map<String, String> myMap = new HashMap<String, String>();
        return (HashMap<String, String>) myMap;
        
    }
    
    /**
     * Method that generates a report of the values frequency in the csv
     * @param structuredData the csv row structured in a hashMap
     */
    private void generateReport(List<HashMap<String, String>> structuredData) throws IOException {
        
        HashMap<String, Integer>  map = new HashMap<String, Integer>();
        
        //This part of the code counts how many times a value appears inside the csv
        for (HashMap<String, String> record: structuredData) {
            for(int counter = 0; counter < this.headersList.size(); counter++) {
                if(map.containsKey(record.get(this.headersList.get(counter)))) {
                    int previousValue = map.get(record.get(this.headersList.get(counter)));
                    previousValue++;
                    map.replace(record.get(this.headersList.get(counter)), previousValue);
                    
                } else {
                    map.putIfAbsent(record.get(this.headersList.get(counter)), 1);
                }
               
                
           }
        }
        
        // This part of the code orders the hashMap into a decreasing order
        Map<String, Integer> decreasingMap = map
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (element1, element2) -> element2,
                LinkedHashMap::new));
        
        this.csvWriter((HashMap<String, Integer>) decreasingMap);
        
    }
    
    /**
     * Method that generates a csv file
     * @param csv a hashMap that will be transformed into a csv file
     */
    public static void csvWriter(HashMap<String, Integer> csv) throws IOException {
        String separator = System.getProperty("line.separator");

        try (Writer writer = new FileWriter("frequencyReport.csv")) {
          for (Map.Entry<String, Integer> entry :csv.entrySet()) {
            writer.append(entry.getKey())
                  .append(',')
                  .append(entry.getValue().toString())
                  .append(separator);
          }
        } catch (IOException ex) {
          ex.printStackTrace(System.err);
        }
    }
    
}
