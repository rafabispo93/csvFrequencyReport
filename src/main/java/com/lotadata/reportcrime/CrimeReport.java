/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lotadata.reportcrime;
import data.ReportGenerator;
import java.io.IOException;

/**
 *
 * @author rafael
 */
public class CrimeReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ReportGenerator r = new ReportGenerator();
//        r.processInputFile("/home/rafael/Downloads/Sacramentorealestatetransactions.csv");
        r.processInputFile("/home/rafael/Documents/lotadata/crimeinfo20181219.csv"); // Pass here the path of a csv file to be analysed
        
    }
    
}
