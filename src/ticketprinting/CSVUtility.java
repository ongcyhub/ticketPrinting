/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketprinting;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author OngCY
 */
public class CSVUtility {

    static String seatsA[] = new String[(Integer.parseInt(TicketPrinting.readConfig("catANum")))];
    static String seatsB[] = new String[(Integer.parseInt(TicketPrinting.readConfig("catBNum")))];
    static int lastIndex = 0;
    private static final Logger csvlogger = LogManager.getLogger(CSVUtility.class);

    /**
     * *
     * Read CSV file from data folder and split the string by the delimitor
     * character for processing.
     *
     * @param option
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void readCSV(int option) throws FileNotFoundException, IOException {
        String line = "";
        BufferedReader fileReader = new BufferedReader(new FileReader(TicketPrinting.readConfig("sourcelocation")));
        fileReader.readLine();
        while ((line = fileReader.readLine()) != null) {
            String[] rawValue = line.split(TicketPrinting.readConfig("separator"));
            csvlogger.debug(rawValue[2] + rawValue[0]);
            genSeatNumList(rawValue[2], rawValue[0]);
        }
        if (option == 1) {
            csvlogger.info("Proceed to generate sequential CSV file");
            System.out.println("Proceed to generate sequential CSV file");
            printSequentialTicket();
        } else {
            csvlogger.info("Proceed to generate cut stack CSV file");
            System.out.println("Proceed to generate cut stack CSV file");
            printCutStackTicket();
        }
    }

    /**
     * *
     * Generate seat number range to populate the 2 arrays for ticket generation
     *
     * @param rawRange
     * @param rowChar
     * @param Range
     */
    private static void genSeatNumList(String rawRange, String rowChar) {
        String test[] = rawRange.split("-");
        //get the upper amd lower range of the numbers in order to populate the array of row characters
        int lowerRange = Integer.parseInt(test[0]);
        int upperRange = Integer.parseInt(test[1]);

        if (rowChar.substring(0, 1).equalsIgnoreCase("A")) {
            for (int a = lowerRange - 1; a < upperRange; a++) {
                seatsA[a] = rowChar;
            }

        } else {
            for (int b = lowerRange - 1; b < upperRange; b++) {
                seatsB[b] = rowChar;
            }
        }

    }

    /**
     * *
     * Generate sequential ticket
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void printSequentialTicket() throws FileNotFoundException, IOException {
        File filePath = new File(TicketPrinting.readConfig("generateLocation") + ticketConfig.resultPrefix + ticketConfig.resultExt);

        try {
            if (filePath.exists() == false) {
                filePath.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            csvlogger.debug("seat A : " + seatsA.length);
            for (int valueIndex = 0; valueIndex < seatsA.length; valueIndex++) {
                out.append(seatsA[valueIndex]);
                out.append(TicketPrinting.readConfig("separator"));
                out.append(String.valueOf(valueIndex + 1));
                out.append(ticketConfig.systemCRLF);
                out.flush();
            }
            csvlogger.debug("seat B : " + seatsB.length);
            for (int valueIndex = 0; valueIndex < seatsB.length; valueIndex++) {
                out.append(seatsB[valueIndex]);
                out.append(TicketPrinting.readConfig("separator"));
                out.append(String.valueOf(valueIndex + 1));
                out.append(ticketConfig.systemCRLF);
                out.flush();
            }

        } catch (Exception e) {
            csvlogger.error(e.getMessage());
        }
        csvlogger.info("Sequential CSV File generation complete");
        System.out.println("Sequential CSV File generation complete");

    }

    /**
     * *
     * generate cut stack ticket sequence.
     *
     * @throws IOException
     */
    private static void printCutStackTicket() throws IOException {

        File filePath = new File(TicketPrinting.readConfig("generateLocation") + ticketConfig.resultPrefix + "1" + ticketConfig.resultExt);
        int pageCount;
        int sum = 0;

        try {
            if (filePath.exists() == false) {
                filePath.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            for (pageCount = 0; pageCount < Integer.parseInt(TicketPrinting.readConfig("catAPages")); pageCount++) {
                sum = pageCount;
                out.append("Page " + (pageCount + 1));
                out.append(ticketConfig.systemCRLF);
                for (int recordCount = 0; recordCount < 7; recordCount++) {

                    out.append(seatsA[sum + 1]);
                    out.append(TicketPrinting.readConfig("separator"));
                    out.append(String.valueOf(sum + 1));
                    out.append(ticketConfig.systemCRLF);
                    sum = sum + 11;
                    out.flush();

                }
            }
            for (pageCount = 0; pageCount < Integer.parseInt(TicketPrinting.readConfig("catBPages")); pageCount++) {
                sum = pageCount;
                out.append("Page " + (pageCount + 1));
                out.append(ticketConfig.systemCRLF);
                for (int recordCount = 0; recordCount < 7; recordCount++) {
                    out.append(seatsB[sum + 1]);
                    out.append(TicketPrinting.readConfig("separator"));
                    out.append(String.valueOf(sum + 1));
                    out.append(ticketConfig.systemCRLF);
                    sum = sum + 11;
                    out.flush();

                }

            }
        } catch (Exception e) {
            csvlogger.error(e.getMessage());
        }
        csvlogger.info("Cut Stack CSV File generation complete");
        System.out.println("Cut Stack CSV File generation complete");

    }
}
