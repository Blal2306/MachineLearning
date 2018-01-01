package wekatest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.PropertyPath.Path;

public class buildWekaFileForProduct 
{
    //array for the days and the quantity
    int[] quantity = new int[119];
    
    //given the product this will create the file for weka
    public void run(int id)
    {
        readFile(id);
        
        //create a directory
        File dir = new File("wekaFiles");
        if(!dir.exists())
            dir.mkdir();
        
        //create the file for output
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter("wekaFiles\\product"+id+".arff", "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(buildWekaFileForProduct.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(buildWekaFileForProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        writer.println("@relation prediction\n");
        writer.println("@attribute time numeric");
        writer.println("@attribute q numeric\n");
        writer.println("@data");
        
        
        
        for(int i = 0; i < quantity.length; i++)
        {
            if(quantity[i] == 0)
            {
                writer.println(i+","+1);
            }
            else
            {
                writer.println(i+","+quantity[i]);
            }
        }
        writer.close();
    }
    public void readFile(int idIn)
    {
        String fileName = "trade_info_training_noise_removed.txt";
        String line = null;
        try 
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) 
            {
                line = line.trim();
                String[] temp = line.split("\t");
                
                //get the product id
                int id = Integer.parseInt(temp[0]);
                
                //get the time
                int time = Integer.parseInt(temp[2]);
                
                //get the quantity
                int quan = Integer.parseInt(temp[3]);
                
                //only looking at product id one
                if(id == idIn)
                {
                    quantity[time] = quantity[time] + quan;
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
}

