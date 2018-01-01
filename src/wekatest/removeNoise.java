package wekatest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class removeNoise 
{
    //this contains customer ids and quantity each cusotmer bought
    static Map<Integer, Integer> data = new TreeMap<Integer, Integer>();
    
    //set of customer id's who bought less than 2 items
    static Set<Integer> noise = new HashSet<>();
    
    public static void run() 
    {
        readFile();
        
        //insert the customer in to the set who bought less than
        //two items
        for(Map.Entry<Integer, Integer> entry : data.entrySet())
        {
            int customer = entry.getKey();
            int quantity = entry.getValue();
            if(quantity < 2)
            {
                noise.add(customer);
            }
        }
        
        //print after removing noise
        outputFile();
    }
    //this will build the customer/quantity hashmap
    private static void readFile()
    {
        String fileName = "trade_info_training.txt";
        String line = null;
        try 
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) 
            {
                line = line.trim();
                String[] temp = line.split("\t");
                //get the id of the customer
                int id = Integer.parseInt(temp[1]);
                int quantity = Integer.parseInt(temp[3]);
                
                //if the id already exists in the tree map
                if(data.containsKey(id))
                {
                    int prevQ = data.get(id);
                    prevQ = prevQ+quantity;
                    data.put(id, prevQ);
                }
                else
                {
                    data.put(id, quantity);
                }
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
    //this will recreate the file after removing the noise
    private static void outputFile()
    {
        String outputFileName = "trade_info_training_noise_removed.txt";
        String fileName = "trade_info_training.txt";
        String line = null;
        try 
        {
            //create the file for output
            PrintWriter writer = null;
            writer = writer = new PrintWriter(outputFileName, "UTF-8");
            
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) 
            {
                line = line.trim();
                String[] temp = line.split("\t");
                
                //get the product id
                int product_id = Integer.parseInt(temp[0]);
                
                //get the buyer id
                int customer_id = Integer.parseInt(temp[1]);
                
                //get the trade time
                int day = Integer.parseInt(temp[2]);
                
                //get the quantity of the product
                int quantity = Integer.parseInt(temp[3]);
                
                //get the trade price
                double price = Double.parseDouble(temp[4]);
                
                if(!noise.contains(customer_id))
                {
                    writer.println(product_id+"\t"+customer_id+"\t"+day+"\t"+quantity+"\t"+price);
                }
                
            }   
            bufferedReader.close(); 
            writer.close();
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

