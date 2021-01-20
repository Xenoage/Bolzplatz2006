/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.languagecreator;

import java.io.File;
import java.util.List;

import org.w3c.dom.*;

import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This program creates language xml files
 * with the given id (e.g. "fr") in the
 * subfolder "output",
 * based on the given language (e.g. "de").
 * All entries are set to "?".
 * A third language can be given (e.g.
 * again "fr"), and the entries are copied
 * from this language.
 * This is useful to update a language:
 * Create a new language with the same id,
 * use a complete language as the foundation,
 * and then just search for the "?"
 * entries and fill them.
 *
 * @author Andreas Wenger
 */
public class LanguageCreator
{
  
  /**
   * Start the program with these parameters:
   * new language id, base language id,
   * optional: copy language id
   */
  public static void main(String args[])
  {
    System.out.println("\nBP2K6 LANGUAGE CREATOR");
    System.out.println("************************");
    System.out.println("\n(c) 2006 by Andreas Wenger, Xenoage Software\n");
    
    //4 arguments?
    if (args.length < 3)
    {
      System.out.println("2 parameters must be given:");
      System.out.println("- new language id    (e.g. \"fr\", will be written in subdirectory \"output\")");
      System.out.println("- base language id   (e.g. \"de\", whose vocabulary IDs are used.");
      System.out.println("3rd optional parameter:");
      System.out.println("- copy language id   (e.g. \"fr\", whose vocabulary entries are copied, if available.");
      
      return;
    }
    String newLangID = args[0];
    String baseLangID = args[1];
    String copyLangID = null;
    if (args.length == 3)
      copyLangID = args[2];
    
    //clear output directory
    String[] filesToClear = new File("output").list();
    if (filesToClear != null)
    {
      for (int i = 0; i < filesToClear.length; i++)
        new File("output/" + filesToClear[i]).delete();
    }
    
    //create output directory, if it does not exist
    if (!new File("output").exists())
      new File("output").mkdir();
    
    //copy base files into output directory
    String[] filesToCopy = new File(
      "../../data/languages/" + baseLangID).list(FileUtils.getXMLFilter());
    if (filesToCopy == null)
    {
      System.out.println("Error: Base language \"" + baseLangID + "\" not found!");
      return;
    }
    for (int i = 0; i < filesToCopy.length; i++)
    {
      try
      {
        FileUtils.copy(
          new File("../../data/languages/" + baseLangID + "/" + filesToCopy[i]),
          new File("output/" + filesToCopy[i]));
      }
      catch (Exception ex)
      {
        System.out.println("Error: Could not copy file:\n" +
          "../../data/languages/" + baseLangID + "/" + filesToCopy[i]);
        return;
      }
    }
    
    //if copy language is set, load it
    boolean useCopyLanguage = (copyLangID != null);
    if (useCopyLanguage)
      Language.loadLanguageFromPath(
        copyLangID, "../../data/languages/" + copyLangID);
    
    //each file: replace id and values
    for (int iFile = 0; iFile < filesToCopy.length; iFile++)
    {
      try
      {
        //read file into memory
        Document doc = XMLReader.readFile("output/" + filesToCopy[iFile]);
        Node root = doc.getFirstChild();
        
        //set id
        XMLWriter.addAttribute(root, "id", newLangID);
        
        //replace values by "?", if no copy language is set,
        //or by the value of the copy language
        List<Element> elements = XMLReader.elements(root, "entry");
        for (int iEntry = 0; iEntry < elements.size(); iEntry++)
        {
          Node entry = elements.get(iEntry);
          String newValue = "?";
          if (useCopyLanguage)
          {
            String copyValue = Language.getWithNull(
              XMLReader.readAttributeValue(entry, "id"));
            if (copyValue != null)
              newValue = copyValue;
          }
          XMLWriter.addAttribute(entry, "value", newValue);
        }
        
        //save file
        XMLWriter.writeFile(doc, "output/" + filesToCopy[iFile]);
        
      }
      catch (Exception ex)
      {
        System.out.println("Error: Could not read or modify file:\n" +
          "output/" + filesToCopy[iFile] + "\n");
        ex.printStackTrace(System.out);
        return;
      }  
    }
    
    System.out.println(filesToCopy.length +
      " language files successfully created!");
    
  }
  
}
