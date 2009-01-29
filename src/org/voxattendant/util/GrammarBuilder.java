/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is vox-mail.
 *
 * The Initial Developer of the Original Code is Voxeo Corporation.
 * Portions created by Voxeo are Copyright (C) 2000-2007.
 * All rights reserved.
 * 
 * Contributor(s):
 * ICOA Inc. <info@icoa.com> (http://icoa.com)
 */

package org.voxattendant.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.voxattendant.model.AltSpellingBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.EntityBean;


public class GrammarBuilder {
   /**
    * Grammar file names. Might want to make the settable.
    */
   public static final String CONTACTS_GRAMMAR_FILENAME = "contacts.grammar";
   public static final String ENTITIES_GRAMMAR_FILENAME = "entities.grammar";
   
   public static final String CONTACTS_GRAMMAR_XML_FILENAME = "Contacts.xml";
   public static final String ENTITIES_GRAMMAR_XML_FILENAME = "Entities.xml";
   /**
    * Bit masked used to determine if we need to regenerate the grammar files
    */
   public static final int MASK_CONTACTS_GRAMMAR = 0x00000001;
   public static final int MASK_ENTITIES_GRAMMAR = 0x00000002;
   /**
    * The directory to generate the grammar files to.
    */
   private static String grammarDirectory;
   /**
    * Init() method to set the grammar directory
    *
    * @param grammarDirectory the grammar directory
    */
   public static void init(String grammarDirectory) {
      GrammarBuilder.grammarDirectory = grammarDirectory;
   }
   /**
    * Builds the Contact grammar
    *
    * @param contacts the list of contacts to generate the grammar from
    */
   public static void buildContactsGrammar(List contacts) {
      ////
      //System.out.println("In buildContactGrammar: " + (grammarDirectory + CONTACTS_GRAMMAR_FILENAME));
      ////
      
      //this is the old version...
//      try {
//         FileWriter fw = new FileWriter(grammarDirectory + CONTACTS_GRAMMAR_FILENAME);
//         fw.write("CONTACTS\n[\n");
//
//         if(contacts != null) {
//            ContactBean contactBean = null;
//            Iterator it = contacts.iterator();
//            for(; it.hasNext();) {
//               contactBean = (ContactBean)it.next();
//               if(!contactBean.isActive())
//                  continue;
//
//               StringBuffer firstnames = new StringBuffer("[");
//               StringBuffer lastnames = new StringBuffer("[");
//
//               firstnames.append("(" + contactBean.getFirstname().toLowerCase() + ")");
//               lastnames.append("(" + contactBean.getLastname().toLowerCase() + ")");
//
//               BeanCollection altSpellings = contactBean.getAltSpellings();
//               if(altSpellings != null) {
//                  AltSpellingBean altSpellingBean = null;
//
//                  for(int i = 0; i < altSpellings.size(); i ++) {
//                     altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
//
//                     if(altSpellingBean.getAltspellingTypeId() == AltSpellingBean.TYPE_FIRSTNAME)
//                        firstnames.append(" (" + altSpellingBean.getSpelling().toLowerCase() + ")");
//                     else if(altSpellingBean.getAltspellingTypeId() == AltSpellingBean.TYPE_LASTNAME)
//                        lastnames.append(" (" + altSpellingBean.getSpelling().toLowerCase() + ")");
//                  }
//               }
//               firstnames.append("]");
//               lastnames.append("]");
//
//               fw.write("   (\n");
//               fw.write("      " + firstnames + "\n");
//               fw.write("      " + lastnames + "\n");
//               fw.write("   )  {<mainmenu \"contact-" + contactBean.getContactId() + "\">}\n");
//            }
//         }
//         fw.write("]\n");
//         fw.close();
//      }
//      catch(Exception e) {
//         e.printStackTrace();
//      }
      
      
      System.out.println("In buildContactGrammar: " + (grammarDirectory + CONTACTS_GRAMMAR_XML_FILENAME));
      //now, build voicexml2.0 xml grammar
      try {
         FileWriter fw = new FileWriter(grammarDirectory + CONTACTS_GRAMMAR_XML_FILENAME);
         
            fw.write("<?xml version=\"1.0\"?>\n");
            fw.write("<grammar xmlns=\"http://www.w3.org/2001/06/grammar\" xml:lang=\"en-US\" root=\"CONTACTS\">\n");
            fw.write("<rule id=\"CONTACTS\">\n");
            //Optional phrases
            fw.write("<item repeat=\"0-1\"><one-of><item>i'd like</item><item>I want</item><item>connect me to</item><item>I wanna call</item> </one-of> </item>");
            fw.write("<one-of>\n");         

         if(contacts != null) {
            ContactBean contactBean = null;
            Iterator it = contacts.iterator();
            for(; it.hasNext();) {
               contactBean = (ContactBean)it.next();
               if(!contactBean.isActive())
                  continue;
               
               String firstName = contactBean.getFirstname().toLowerCase();
               String lastName = contactBean.getLastname().toLowerCase();
               
               fw.write("<item>\n");
               fw.write(firstName + " " + lastName + " <tag> <![CDATA[  <mainmenu \"contact-" + contactBean.getContactId() + "\">  ]]>  </tag>\n");
               fw.write("</item>\n\n");
               
               String extension = ContactBean.generateDtmfExtension(contactBean.getExtension());
               if (extension != null && extension.length() > 0)
               {
                   fw.write("<item>\n");
                   fw.write(extension + " <tag> <![CDATA[  <mainmenu \"contact-" + contactBean.getContactId() + "\">  ]]>  </tag>\n");
                   fw.write("</item>\n\n");
               }
               BeanCollection altSpellings = contactBean.getAltSpellings();
               if(altSpellings != null) {
                  AltSpellingBean altSpellingBean = null;
                  
                  for(int i = 0; i < altSpellings.size(); i ++) {
                     altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                     
                     if(altSpellingBean.getAltspellingTypeId() == AltSpellingBean.TYPE_FIRSTNAME) {
                        fw.write("<item>\n");
                        fw.write(altSpellingBean.getSpelling().toLowerCase() + " " + lastName + " <tag> <![CDATA[  <mainmenu \"contact-" + contactBean.getContactId() + "\">  ]]>  </tag>\n");
                        fw.write("</item>\n\n");
                     } 
                     else if(altSpellingBean.getAltspellingTypeId() == AltSpellingBean.TYPE_LASTNAME) {
                        fw.write("<item>\n");
                        fw.write(firstName + " " + altSpellingBean.getSpelling().toLowerCase() + " <tag> <![CDATA[  <mainmenu \"contact-" + contactBean.getContactId() + "\">  ]]>  </tag>\n");
                        fw.write("</item>\n\n");
                     }
                     else if(altSpellingBean.getAltspellingTypeId() == AltSpellingBean.TYPE_NAME_ALIAS) {
                        fw.write("<item>\n");
                        fw.write(altSpellingBean.getSpelling().toLowerCase() + " <tag> <![CDATA[  <mainmenu \"contact-" + contactBean.getContactId() + "\">  ]]>  </tag>\n");
                        fw.write("</item>\n\n");
                     }
                                            
                  } //for - alt spellings
               
               } //if alt spellings               
            }//for
         }
         
         fw.write("</one-of>\n");
         
         //please
         fw.write("<item repeat=\"0-1\"><one-of><item>please</item></one-of> </item>");
         
         fw.write("</rule>\n");
         fw.write("</grammar>\n");

         fw.close();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
   /**
    * Build entities grammar
    *
    * @param entities the list of entities to generate the grammars from
    */
   public static void buildEntitiesGrammar(List entities) {
      
      System.out.println("In buildEntityGrammar: " + grammarDirectory + ENTITIES_GRAMMAR_XML_FILENAME);
      //now build the new voicexml 2.0 version
      try {
         FileWriter fw = new FileWriter(grammarDirectory + ENTITIES_GRAMMAR_XML_FILENAME);
         
            fw.write("<?xml version=\"1.0\"?>\n");
            fw.write("<grammar xmlns=\"http://www.w3.org/2001/06/grammar\" xml:lang=\"en-US\" root=\"ENTITIES\">\n");
            fw.write("<rule id=\"ENTITIES\">\n");
            //Optional phrases
            fw.write("<item repeat=\"0-1\"><one-of><item>i'd like</item><item>I want</item><item>connect me to</item><item>I wanna call</item> </one-of> </item>");
            fw.write("<item repeat=\"0-1\"><one-of><item>the</item></one-of> </item>");
            
            
            fw.write("<one-of>\n");  
         
         buildEntitiesGrammar(entities, fw, 2);
         
            fw.write("</one-of>\n");
            
            fw.write("<item repeat=\"0-1\"><one-of><item>department</item></one-of> </item>");
            fw.write("<item repeat=\"0-1\"><one-of><item>please</item></one-of> </item>");
            
            fw.write("</rule>\n");
            fw.write("</grammar>\n");
            fw.close();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
      
      
      ////
      System.out.println("In buildEntityGrammar: " + grammarDirectory + ENTITIES_GRAMMAR_FILENAME);
      ////
      try {
         FileWriter fw = new FileWriter(grammarDirectory + ENTITIES_GRAMMAR_FILENAME);
         fw.write("ENTITIES\n[\n");
         buildEntitiesGrammar(entities, fw, 1);
         fw.write("          ]\n");
         fw.close();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
      
   }
   /**
    * Helper function for generating entities grammar
    *
    * @param entities the list of entities to generate the grammars from
    * @param writer the stream to write to
    */
   
   //version: 1=gsl, 2=xml
   
   private static void buildEntitiesGrammar(List entities, Writer writer, int version) {
      try {
         if(entities != null) {
            EntityBean entityBean = null;
            Iterator it = entities.iterator();
            for(; it.hasNext();) {
               entityBean = (EntityBean)it.next();

               if(!entityBean.isActive())
                  continue;

               if(entityBean.isSubEntity())
                  continue;

               if (version == 1)
                    buildEntityGrammar(entityBean, writer);
               else
                    buildEntityGrammar_XML(entityBean, writer);

               // Handle sub-entities
               if(entityBean.getSubEntities() != null)
                  buildSubEntitiesGrammar(entityBean.getSubEntities().getSortedList("parentId"), writer, version);
            }
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
   /*
    * Helper function for generating sub-entities grammar
    *
    * @param entities the list of sub-entities to generate the grammars from
    * @param writer the stream to write to
    */
   private static void buildSubEntitiesGrammar(List entities, Writer writer, int version)
      throws IOException {

      if(entities != null) {
         EntityBean entityBean = null;
         Iterator it = entities.iterator();
         for(; it.hasNext();) {
            entityBean = (EntityBean)it.next();
            if(!entityBean.isActive())
               continue;
                        
            if (version == 1)
                buildEntityGrammar(entityBean, writer);
            else
                buildEntityGrammar_XML(entityBean, writer);
         }
      }
   }
   
   
     
   /*
    * Helper function for generating grammar for an entity
    *
    * @param entityBean the entityBean to generate grammars for
    * @param writer the stream to write to
    */
   private static void buildEntityGrammar(EntityBean entityBean, Writer writer)
      throws IOException {

      if(entityBean == null)
         return;

      if(!entityBean.isActive())
         return;

      StringBuffer names = new StringBuffer("[");
      names.append("(" + entityBean.getName().toLowerCase() + ")");

      BeanCollection altSpellings = entityBean.getAltSpellings();
      if(altSpellings != null) {
         AltSpellingBean altSpellingBean = null;

         for(int i = 0; i < altSpellings.size(); i ++) {
            altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
            names.append(" (" + altSpellingBean.getSpelling().toLowerCase() + ")");
         }
      }
      names.append("]");
      writer.write("   (\n");
      writer.write("      " + names + "\n");
      writer.write("   )  {<mainmenu \"entity-" + entityBean.getEntityId() + "\">}\n");
   }
   
   
   /*
    * Helper function for generating grammar for an entity
    *
    * @param entityBean the entityBean to generate grammars for
    * @param writer the stream to write to
    */
   private static void buildEntityGrammar_XML(EntityBean entityBean, Writer writer)
      throws IOException {

      if(entityBean == null)
         return;

      if(!entityBean.isActive())
         return;
      
        writer.write("<item>\n");
        writer.write(entityBean.getName().toLowerCase() + " <tag> <![CDATA[  <mainmenu \"entity-" + entityBean.getEntityId() + "\">  ]]>  </tag>\n");
        writer.write("</item>\n\n");
          

      BeanCollection altSpellings = entityBean.getAltSpellings();
      if(altSpellings != null) {
         AltSpellingBean altSpellingBean = null;

         for(int i = 0; i < altSpellings.size(); i ++) {
            altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                        
            writer.write("<item>\n");
            writer.write(altSpellingBean.getSpelling().toLowerCase() + " <tag> <![CDATA[  <mainmenu \"entity-" + entityBean.getEntityId() + "\">  ]]>  </tag>\n");
            writer.write("</item>\n\n");
         
         }//for
      }
   }
   
   
   //the following isn't used with the voicexml2.0 xml grammar, so only the old version exists
   /**
    * Generates an inline grammar for the entities
    *
    * @param entities the list of entities to generate the inline grammars from
    */
   public static String buildInlineSubEntityGrammar(List entities) {

      StringWriter sw = new StringWriter();
      try {
         sw.write("[\n");
         buildSubEntitiesGrammar(entities, sw, 1); //force version 1
         sw.write("]\n");
      }
      catch(Exception e) {
         e.printStackTrace();
      }
      return sw.toString();
   }
   //
   // Helper functions
   //
   /**
    * Determine if a bit is set
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return boolean true if it is set, otherwise false
    */
   public static boolean isBitOn(int bits, int mask) {
      return (0 != (bits & mask));
   }
   /**
    * Setting the modified grammar bit on
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return int the modified grammar
    */
   public static int setBit(int bits, int mask) {
      return (bits | mask);
   }
   /**
    * Unsets the modified grammar bit
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return int the modified grammar
    */
   public static int unsetBit(int bits, int mask) {
      return (bits & ~mask);
   }
}
