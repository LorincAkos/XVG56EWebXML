package domxvg56e1105;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DomModify1xvg56e {
	public static void main(String argv[]){
		try {
		File xmlfile = new File("XVG56E_orarend.xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.parse(xmlfile);
		
		 NodeList orak = doc.getElementsByTagName("ora");

		  Element ora0 = (Element) orak.item(0);


            Element newChild = doc.createElement("oraado");
            newChild.setTextContent("Kiss Pál");

            // append to the end of <ora> children
            ora0.appendChild(newChild);

            for (int i = 0; i < orak.getLength(); i++) {
                Element ora = (Element) orak.item(i);
                // setAttribute updates if present, creates if missing
                ora.setAttribute("tipus", "eloadas");
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