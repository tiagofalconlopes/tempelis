/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author tiagofalcon
 */
public class ReadFiles {
    // Create an array list to store file names.
    // This will be returned in the end for the user.
    ArrayList<String> outFiles;
    
    public ReadFiles(){
        this.outFiles  = new ArrayList<String>();
    }
    
    public void readAndWriteFiles() {
        
        // Using try catch to select files and avoid IO exceptions
        try {
            // Create an object to choose file
            JFileChooser chosen = new JFileChooser();
            chosen.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // Set to allow select multiple files
            chosen.setMultiSelectionEnabled(true);
            // Set filter of file types
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text & CSV files", "txt", "csv");
            chosen.setFileFilter(filter);
            // Declare an integer variable (to get the output) of the chosen file
            int dialogRes;
            dialogRes = chosen.showOpenDialog(null);
            // Test the file
            if(dialogRes == JFileChooser.APPROVE_OPTION){
                // Get the file  to be reader. Use array to set multiple files.
                File[] myFiles = chosen.getSelectedFiles();
                
                // Ask for the pattern to be found
                Pattern p = Pattern.compile(JOptionPane.showInputDialog("Choose a pattern to find:"), 
                        Pattern.CASE_INSENSITIVE);
                
                // Read and iterate through the files
                for (File myFile : myFiles) {
                    FileReader inFile = new FileReader(myFile);
                    // Create an empty string to store the lines
                    try (BufferedReader buffr = new BufferedReader(inFile)) {
                        // Create an empty string to store the lines
                        String eachLine = null;
                        eachLine = (String)buffr.readLine();
                        // Test the lines and search for the patern
                        while (eachLine != null) {
                            if (p.matcher(eachLine).find()) {
                                outFiles.add(myFile.getName());
                                break;
                            }
                            eachLine = buffr.readLine();
                        }
                        // Close the buffer in the end
                    }
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        // Only save if outFiles >= 1
        
        if(outFiles.size() >=1){
            File novelFileName = new File(JOptionPane.showInputDialog("Type the output file name:"));
            FileWriter writingToFile = null;
            BufferedWriter buffw = null;

            try{
                writingToFile = new FileWriter(novelFileName);
                buffw = new BufferedWriter(writingToFile);
                for(int i = 0; i < outFiles.size(); i++){
                    buffw.write(outFiles.get(i) + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    buffw.close();
                    writingToFile.close();
                    // Make sure Array List is empty.
                    outFiles.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "File saved!");
        }
        else{
            JOptionPane.showMessageDialog(null, "There is no file with the required pattern!");
        }
        
    }
}
