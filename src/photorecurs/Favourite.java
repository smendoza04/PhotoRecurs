/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecurs;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;  
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class Favourite {
    File file;
    DocumentBuilderFactory documentBuilderFactory; 
    DocumentBuilder documentBuilder;
    Document document;
    NodeList nodeList;  
    Element root;
    ArrayList<String> allImages;

    public Favourite() throws SAXException, IOException, ParserConfigurationException {
        this.file = new File("./src/photorecurs/FavouriteList.xml");
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();  
        document = documentBuilder.parse(file); 
        root = document.getDocumentElement();
        document.getDocumentElement().normalize();
        nodeList = document.getElementsByTagName("images");
        reloadArray();
    }
    
    public void reloadArray(){
        nodeList = document.getElementsByTagName("images");
        allImages = new ArrayList<String>();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element e = (Element) node;  
            String path = e.getElementsByTagName("path").item(0).getTextContent();
            allImages.add(path);
        }
    }
    
    public ArrayList<String> getAllImages(){
        return allImages;
    }
    
    public void addElement(String newImage) throws TransformerException{
        Element newImagen = document.createElement("images");
        Element newPath = document.createElement("path");
        newPath.setTextContent(newImage);
        newImagen.appendChild(newPath);
        root.appendChild(newImagen);
        saveFile();
        reloadArray();
    }
    public void deleteElement(String oldImage) throws TransformerException{
        if(allImages.contains(oldImage)){
            int indexOld = allImages.indexOf(oldImage);
            for(int i = 0; i < nodeList.getLength(); i++){
                if(indexOld == i ){
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }
            }
        }
        saveFile();
        reloadArray();
    }
    public boolean contains(String check){
        return allImages.contains(check);
    }
    
    public void saveFile() throws TransformerConfigurationException, TransformerException{
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(file); // xml is a object of File i.e. File xml = new File(filePath);
        Source input = new DOMSource(document);
        transformer.transform(input, output);
    }
}
