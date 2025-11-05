package domxvg56e1105;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DomModifyxvg56e {
	public static void main(String argv[]){
		try {
		File xmlfile = new File("XVG56Ehallgatok.xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.parse(xmlfile);
		
		Node hallgato = doc.getElementsByTagName("hallgato").item(0);
		
		NamedNodeMap attr = hallgato.getAttributes();
		Node nodeAttr = attr.getNamedItem("id");
		nodeAttr.setTextContent("01");
		
		NodeList list = hallgato.getChildNodes();
		
		for(int temp = 0; temp < list.getLength();temp++){
			Node node = list.item(temp);
			
			if(node.getNodeType() == node.ELEMENT_NODE) {
				Element element = (Element)node;
				
				if("keresztnev".equals(element.getNodeName())) {
					if("Pál".equals(element.getTextContent())) {
						element.setTextContent("Olivia");
					}
				}
				
				if("vezeteknev".equals(element.getNodeName())) {
					if("Vigh".equals(element.getTextContent())) {
						element.setTextContent("Kiss");
					}
				}
			}
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource source = new DOMSource(doc);
		
		System.out.println("--Módosított Fájl--");
		StreamResult consoleResult = new StreamResult(System.out);
		transformer.transform(source, consoleResult);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}