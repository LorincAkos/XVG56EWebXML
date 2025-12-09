package xvg56edomparse;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XVG56EDOMModify {

    public static void main(String[] args) {
        String inputFileName = "XVG56E_XML.xml";
        String outputFileName = "XVG56E_modified.xml";

        try {
            // ---- dokumentum beolvasása ----
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(inputFileName));
            doc.getDocumentElement().normalize();

            System.out.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
            System.out.println("Eredeti dokumentum beolvasva: " + inputFileName);
            System.out.println();

            // ---- MÓDOSÍTÁSOK ----

            // 1. f1 felhasználó email-jének megváltoztatása
            modifyFelhasznaloEmail(doc, "f1", "uj_email@example.com");

            // 2. új kedvencmufaj hozzáadása f2 felhasználóhoz
            addKedvencMufajToFelhasznalo(doc, "f2", "Horror");

            // 3. sajatlista (f2, a2) pontszámának módosítása
            modifySajatlistaPontszam(doc, "f2", "a2", 10);

            // 4. új anime elem hozzáadása (új akod: a4)
            addNewAnime(doc, "a4", "s3", "New Anime Title", "TV",
                        "2025.01.01", "2025.03.31", 12);

            // ---- MÓDOSÍTÁS MENTÉSE ----
            saveDocumentToFile(doc, outputFileName);

            System.out.println("Módosított dokumentum elmentve ide: " + outputFileName);

        } catch (Exception e) {
            System.err.println("Hiba történt az XML módosítása közben: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Egy felhasználó email címének megváltoztatása
    private static void modifyFelhasznaloEmail(Document doc, String felhasznaloId, String newEmail) {
        NodeList list = doc.getElementsByTagName("felhasznalo");
        for (int i = 0; i < list.getLength(); i++) {
            Element f = (Element) list.item(i);
            if (felhasznaloId.equals(f.getAttribute("fkod"))) {

                Element emailElem = (Element) f.getElementsByTagName("email").item(0);
                String oldEmail = emailElem.getTextContent().trim();

                System.out.println("---- 1. MÓDOSÍTÁS: FELHASZNÁLÓ EMAIL ----");
                System.out.println("Felhasználó: " + felhasznaloId);
                System.out.println("  Régi email: " + oldEmail);
                System.out.println("  Új email:  " + newEmail);
                System.out.println();

                // Új érték beállítása
                emailElem.setTextContent(newEmail);
                return;
            }
        }

        System.out.println("Felhasználó nem található (fkod = " + felhasznaloId + ")");
        System.out.println();
    }

    // Új kedvencmufaj elem hozzáadása egy felhasználóhoz
    private static void addKedvencMufajToFelhasznalo(Document doc, String felhasznaloId, String mufajNev) {
        NodeList list = doc.getElementsByTagName("felhasznalo");
        for (int i = 0; i < list.getLength(); i++) {
            Element f = (Element) list.item(i);
            if (felhasznaloId.equals(f.getAttribute("fkod"))) {

                System.out.println("---- 2. MÓDOSÍTÁS: ÚJ KEDVENC MŰFAJ FELHASZNÁLÓNAK ----");
                System.out.println("Felhasználó: " + felhasznaloId);
                System.out.println("  Hozzáadott kedvenc műfaj: " + mufajNev);

                // Új kedvencmufaj elem létrehozása
                Element ujMufajElem = doc.createElement("kedvencmufaj");
                ujMufajElem.setTextContent(mufajNev);

                // Hozzáadjuk a felhasznalo elemhez
                f.appendChild(ujMufajElem);

                System.out.println();
                return;
            }
        }

        System.out.println("Felhasználó nem található (fkod = " + felhasznaloId + ")");
        System.out.println();
    }

    // Egy sajátlista bejegyzés pontszámának módosítása
    private static void modifySajatlistaPontszam(Document doc, String felhasznaloId, String animeId, int newScore) {
        NodeList list = doc.getElementsByTagName("sajatlista");
        for (int i = 0; i < list.getLength(); i++) {
            Element sl = (Element) list.item(i);

            String f_a_f = sl.getAttribute("f_a_f");
            String f_a_a = sl.getAttribute("f_a_a");

            if (felhasznaloId.equals(f_a_f) && animeId.equals(f_a_a)) {

                Element pontszamElem = (Element) sl.getElementsByTagName("pontszam").item(0);
                String oldScore = pontszamElem.getTextContent().trim();

                System.out.println("---- 3. MÓDOSÍTÁS: SAJÁTLISTA PONTSZÁM ----");
                System.out.println("Felhasználó: " + felhasznaloId + ", Anime: " + animeId);
                System.out.println("  Régi pontszám: " + oldScore);
                System.out.println("  Új pontszám:  " + newScore);
                System.out.println();

                // Új pontszám beállítása
                pontszamElem.setTextContent(Integer.toString(newScore));
                return;
            }
        }

        System.out.println("Sajatlista bejegyzés nem található (fkod = " + felhasznaloId + ", akod = " + animeId + ")");
        System.out.println();
    }

    // Új <anime> elem beszúrása
    private static void addNewAnime(Document doc,
            String akod,
            String studioId,
            String cim,
            String tipus,
            String kezdes,
            String befejezes,
            int epizodok) {
		
		System.out.println("---- 4. MÓDOSÍTÁS: ÚJ ANIME HOZZÁADÁSA ----");
		System.out.println("  Új anime kód: " + akod);
		System.out.println("  Cím: " + cim);
		System.out.println("  Stúdió id: " + studioId);
		System.out.println();
		
		// Gyökérelem
		Element root = doc.getDocumentElement();
		
		// Új anime elem létrehozása
		Element animeElem = doc.createElement("anime");
		animeElem.setAttribute("akod", akod);
		animeElem.setAttribute("a_s", studioId);
		
		Element cimElem = doc.createElement("cim");
		cimElem.setTextContent(cim);
		animeElem.appendChild(cimElem);
		
		Element tipusElem = doc.createElement("tipus");
		tipusElem.setTextContent(tipus);
		animeElem.appendChild(tipusElem);
		
		Element kezdesElem = doc.createElement("kezdes");
		kezdesElem.setTextContent(kezdes);
		animeElem.appendChild(kezdesElem);
		
		Element befejezesElem = doc.createElement("befejezes");
		befejezesElem.setTextContent(befejezes);
		animeElem.appendChild(befejezesElem);
		
		Element epizodElem = doc.createElement("epizod");
		epizodElem.setTextContent(Integer.toString(epizodok));
		animeElem.appendChild(epizodElem);
		
		// --- LÉNYEG: jó helyre beszúrás ---
		
		// Megkeressük az utolsó <anime> elemet
		NodeList animeList = doc.getElementsByTagName("anime");
		
		if (animeList.getLength() > 0) {
			Element lastAnime = (Element) animeList.item(animeList.getLength() - 1);
			
			// Megnézzük, mi jön utána 
			Node next = lastAnime.getNextSibling();
			while (next != null && next.getNodeType() != Node.ELEMENT_NODE) {
				next = next.getNextSibling();
			}
		
			if (next == null) {
			root.appendChild(animeElem);
			} else {
			root.insertBefore(animeElem, next);
			}
			} else {
		root.appendChild(animeElem);
		}
    }


    // DOM dokumentum mentése fájlba
    private static void saveDocumentToFile(Document doc, String fileName) throws TransformerException {
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Szebb formázás (behúzás, sortörés)
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));

        transformer.transform(source, result);
    }
}

