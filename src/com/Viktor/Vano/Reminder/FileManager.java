package com.Viktor.Vano.Reminder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    public static String readOrCreateFile(String filename)
    {
        String fileSeparator = System.getProperty("file.separator");
        filename = "res" + fileSeparator + filename;
        File file = new File(filename);

        try
        {
            //Create the file
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            BufferedReader reader;
            String data = null;
            try{
                reader = new BufferedReader(new FileReader(filename));
                data = reader.readLine();
                reader.close();
                System.out.println("Reading successful.");
            }catch (Exception e){
                e.printStackTrace();
            }

            if(data==null && filename.contains("port.dat"))
            {
                data="7775";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("IP.dat"))
            {
                data="127.0.0.1";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("message.dat"))
            {
                data="Reminder message.";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("days.dat"))
            {
                data="0";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("hours.dat"))
            {
                data="0";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("minutes.dat"))
            {
                data="0";
                writeToFile(filename, data);
            }else if(data==null && filename.contains("seconds.dat"))
            {
                data="0";
                writeToFile(filename, data);
            }

            return data;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeToFile(String filename, String data)
    {
        File file;
        if(filename.contains("res"))
            file = new File(filename);
        else
        {
            String fileSeparator = System.getProperty("file.separator");
            file = new File("res" + fileSeparator +filename);
        }

        try
        {
            //Create the file
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            //Write Content
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
            System.out.println("File write successful.");
            return true;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return false;
        }
    }

    public static void createDirectoryIfNotExist(String directoryName)
    {
        File file = new File(directoryName);
        if(file.mkdir())
            System.out.println("New directory \"" + directoryName + "\" was created.");
        else
            System.out.println("Directory \"" + directoryName + "\" already exists.");
    }
}
