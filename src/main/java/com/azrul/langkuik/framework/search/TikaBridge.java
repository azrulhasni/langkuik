/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.search;

import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.minio.MinioService;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.xml.sax.SAXException;

/**
 *
 * @author azrul
 */
public class TikaBridge implements ValueBridge<String, String> {
    
    
    private final Parser parser;
    private  MinioService minioService;


    public TikaBridge(MinioService minioService) {
        this.minioService=minioService;
        parser = new AutoDetectParser();
    }

    @Override
    public String toIndexedValue(String documentPath, ValueBridgeToIndexedValueContext context) {
        if (documentPath == null) {
            return null;
        }
        
        try (InputStream input = getDocument(documentPath)) {
            StringWriter writer = new StringWriter();
            WriteOutContentHandler contentHandler = new WriteOutContentHandler(writer);
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();
            parser.parse(input, contentHandler, metadata, parseContext);
            String res = writer.toString();
            if (res!=null){
                return res.trim();
            }else{
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(TikaBridge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException | TikaException ex) {
            Logger.getLogger(TikaBridge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TikaBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public InputStream getDocument(String relativePath){
        
        if (minioService==null){
            minioService = new MinioService();
            return minioService.get(relativePath);
        }else{
            return minioService.get(relativePath);
        }
              
    }

   
}
