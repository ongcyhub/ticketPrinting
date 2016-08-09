/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketprinting;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import ticketprinting.CSVUtility;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author OngCY
 */
public class TicketPrinting {

    /**
     * @param args the command line arguments
     */
    static final String configLocation = ticketConfig.configFile;
    private static final Logger logger = LogManager.getLogger(TicketPrinting.class);
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        CSVUtility c = new CSVUtility();
        logger.info("Enter program");
        System.out.println("Welcome to Ticket Printing System. Please enter your choice: ");
        System.out.println("1. Sequential Order Printing");
        System.out.println("2. Cut stack order printing");
        System.out.println("3. Quit");
        System.out.print("Please enter your choice: ");
        Scanner input = new Scanner(System.in);
        try {
            int choice = input.nextInt();
            logger.info("User Select :" + choice);
            if (choice == 1) {
                
                c.readCSV(choice);
            } else if (choice == 2) {
                c.readCSV(choice);
            } else if (choice == 3) {
                System.out.println("Bye");
            } else {
                System.out.println("Option unavailable");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    public static String readConfig(String cLocation) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            
            input = new FileInputStream(configLocation);
            prop.loadFromXML(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }
        }
        return prop.getProperty(cLocation);
    }
    
}
