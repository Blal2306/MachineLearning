package wekatest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Set;
import java.util.TreeSet;

public class Driver {
    
    //to hold the key products
    static Set<Integer> keyProducts = new TreeSet<>();
    
    //this will hold the IDs for key products
    static int[] ids = new int[100];
    
    //table to hold the final result
    static int[][] finalRes = new int[100][29];
    
    //table for overall quantity
    static int[] overall = new int[29];
    
    public static void main(String[] args) throws Exception
    {
        //remove the noise from the input file
        removeNoise.run();
        
        //get key product id's
        getKeyProductIDs();
        
        //generate weka files for all key products 
        for(int x: keyProducts)
        {
            buildWekaFileForProduct wekaFile = new buildWekaFileForProduct();
            wekaFile.run(x);
        }
        
        //*** BUILD THE FINAL OUTPUT TABLE ****
        int c = 0;
        int o = 0;
        for(int t: keyProducts)
        {
            predictor p = new predictor();
            p.run(t);
            int[] res = p.getOuput();
            
            //insert the id of the current product
            ids[c] = t;
            
            
            for(int j: res)
            {
                finalRes[c][o] = j;
                o++;
            }
            c++;
            o = 0;
        }
        
        //**** END OF FINAL OUTPUT TABLE BUILD ****//
        
        //**** CALCULATE THE OVERALL SUM **** //
        int counter = 0;
        
        //iterate over the columns (0 - 28)
        for(int i = 0; i < 29; i++)
        {
            int sum = 0;
            //iterate over the product ids (0 - 100)
            for(int j = 0; j < finalRes.length; j++)
            {
                sum = sum +finalRes[j][i];
            }
            overall[i] = sum;
        }
        //**** END OF OVERALL QUANTITY CALCULATION ***
        
        //*** START OF FINAL OUTPUT FILE CREATION ****//
        PrintWriter writer = null;
        writer = writer = new PrintWriter("output.txt", "UTF-8");
        //output the overall quantity prediction
        writer.print("0 ");
        for(int i = 0; i < overall.length; i++)
        {
            writer.print(overall[i]+" ");
        }
        writer.println();
        
        //ouput the prediction for each individual product
        for(int i = 0; i < finalRes.length; i++)
        {
            writer.print(ids[i]+" ");
            for(int j = 0; j < finalRes[i].length; j++)
            {
                writer.print(finalRes[i][j]+" ");
            }
            writer.println();
        }
        writer.close();
        System.out.println("Output file created -----> output.txt");
        //**** END OF OUTPUT FILE CREATION****//
    }
    public static void getKeyProductIDs()
    {
        String fileName = "key_product_IDs.txt";
        String line = null;
        try 
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) 
            {
                line = line.trim();
                int id = Integer.parseInt(line);
                keyProducts.add(id);
            }   
            bufferedReader.close(); 
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Couldn't open the file ...");             
        }
        catch(IOException ex) 
        {
            System.out.println("Couldn't read the file ...");
        }
    }
}
