package xvg56edomparse;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XVG56EDOMQuery {

    public static void main(String[] args) {
        String inputFileName = "XVG56E_XML.xml";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(inputFileName));
            doc.getDocumentElement().normalize();

            System.out.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
            System.out.println();

            // ---- LEKÉRDEZÉSEK ----

            // 1. Összes anime címe stúdiónévvel
            queryAnimekStudioNevvel(doc);

            // 2. Egy adott anime  összes karaktere és szerepe
            queryKarakterekEgyAnimeben(doc, "a1");

            // 3. Egy adott felhasználó listáján szereplő animék státusszal és pontszámmal
            queryFelhasznaloListaja(doc, "f1");

            // 4. lekérdezés: Egy adott műfajú (pl. "Fantasy") animék listája
            queryAnimekMufajAlapjan(doc, "Fantasy");

        } catch (Exception e) {
            System.err.println("Hiba történt az XML feldolgozása közben: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 1. LEKÉRDEZÉS
    private static void queryAnimekStudioNevvel(Document doc) {
        System.out.println("==== 1. LEKÉRDEZÉS: ANIMEK STÚDIÓVAL ====");

        NodeList animeList = doc.getElementsByTagName("anime");
        for (int i = 0; i < animeList.getLength(); i++) {
            Element anime = (Element) animeList.item(i);

            String akod = anime.getAttribute("akod");
            String studioId = anime.getAttribute("a_s");
            String cim = getTagText(anime, "cim");

            // stúdió nevének megkeresése a studio elemek között
            String studioNev = findStudioNevById(doc, studioId);

            System.out.println("Anime kód: " + akod);
            System.out.println("  Cím: " + cim);
            System.out.println("  Stúdió: " + studioNev );
            System.out.println();
        }

        System.out.println();
    }
    
    // 2. LEKÉRDEZÉS
    private static void queryKarakterekEgyAnimeben(Document doc, String animeId) {
        System.out.println("==== 2. LEKÉRDEZÉS: KARAKTEREK EGY ANIMEBEN ====");
        //Anime cim lekérése
        String animeCim = findAnimeCimById(doc, animeId);
        System.out.println("Anime cím: " + animeCim);
        System.out.println("Karakterek és szerepük:");

        //Animehez kapcsolodo karakterek lekérdezése
        NodeList megjelenesList = doc.getElementsByTagName("megjelenes");
        for (int i = 0; i < megjelenesList.getLength(); i++) {
            Element megjelenes = (Element) megjelenesList.item(i);

            String a_k_a = megjelenes.getAttribute("a_k_a"); 
            String a_k_k = megjelenes.getAttribute("a_k_k"); 

            if (animeId.equals(a_k_a)) {
                String szerep = getTagText(megjelenes, "szerep");

                String karakterNev = findKarakterNevById(doc, a_k_k);

                System.out.println("  Karakter: " + karakterNev + " (id: " + a_k_k + ")");
                System.out.println("    Szerep: " + szerep);
            }
        }

        System.out.println();
    }

    // 3. LEKÉRDEZÉS
    private static void queryFelhasznaloListaja(Document doc, String felhasznaloId) {
        System.out.println("==== 3. LEKÉRDEZÉS: FELHASZNÁLÓ SAJÁT LISTÁJA (fkod = " + felhasznaloId + ") ====");
        
        //Felhasználó megkeresése
        String felhasznaloNev = findFelhasznaloNevById(doc, felhasznaloId);
        System.out.println("Felhasználó neve: " + felhasznaloNev);
        System.out.println("Listán szereplő animék:");
        
        //Megtekintett animek kiírása
        NodeList sajatlistaList = doc.getElementsByTagName("sajatlista");
        for (int i = 0; i < sajatlistaList.getLength(); i++) {
            Element entry = (Element) sajatlistaList.item(i);

            String f_a_f = entry.getAttribute("f_a_f"); 
            String f_a_a = entry.getAttribute("f_a_a"); 

            if (felhasznaloId.equals(f_a_f)) {
                String statusz = getTagText(entry, "statusz");
                String pontszam = getTagText(entry, "pontszam");

                String animeCim = findAnimeCimById(doc, f_a_a);

                System.out.println("  Anime: " + animeCim + " (id: " + f_a_a + ")");
                System.out.println("    Státusz: " + statusz);
                System.out.println("    Pontszám: " + pontszam);
            }
        }

        System.out.println();
    }

    // 4. LEKÉRDEZÉS
    private static void queryAnimekMufajAlapjan(Document doc, String mufajNev) {
        System.out.println("==== 4. LEKÉRDEZÉS: \"" + mufajNev + "\" MŰFAJÚ ANIMEK ====");

        // Műfaj id lekérése
        String mufajId = findMufajIdByNev(doc, mufajNev);
        if (mufajId == null) {
            System.out.println("Nincs ilyen műfaj az adatbázisban: " + mufajNev);
            System.out.println();
            return;
        }

        System.out.println("Műfaj kód: " + mufajId);

        // Végigmegyünk az animékhez csatolt műfajokon
        NodeList list = doc.getElementsByTagName("animemufaj");
        for (int i = 0; i < list.getLength(); i++) {
            Element am = (Element) list.item(i);

            String a_m_m = am.getAttribute("a_m_m"); 
            String a_m_a = am.getAttribute("a_m_a"); 

            if (mufajId.equals(a_m_m)) {
                String animeCim = findAnimeCimById(doc, a_m_a);
                String elsodleges = getTagText(am, "elsodleges");

                System.out.println("  Anime: " + animeCim + " (id: " + a_m_a + ")");
                System.out.println("    Elsődleges műfaj?: " + elsodleges);
            }
        }

        System.out.println();
    }

    // SEGÉDFÜGGVÉNYEK NÉV ÉS CÍM KERESÉSÉHEZ
    
    //Id alapján megkeresi, hogy van-e olyan studio
    private static String findStudioNevById(Document doc, String studioId) {
        NodeList list = doc.getElementsByTagName("studio");
        for (int i = 0; i < list.getLength(); i++) {
            Element studio = (Element) list.item(i);
            if (studioId.equals(studio.getAttribute("skod"))) {
                return getTagText(studio, "nev");
            }
        }
        return "(ismeretlen stúdió)";
    }

    
    //Id alapján megkeresi, hogy van-ee olyan anime
    private static String findAnimeCimById(Document doc, String animeId) {
        NodeList list = doc.getElementsByTagName("anime");
        for (int i = 0; i < list.getLength(); i++) {
            Element anime = (Element) list.item(i);
            if (animeId.equals(anime.getAttribute("akod"))) {
                return getTagText(anime, "cim");
            }
        }
        return "(ismeretlen anime)";
    }

    //Id alapján megkeresi, hogy van-e olyan karakter
    private static String findKarakterNevById(Document doc, String karakterId) {
        NodeList list = doc.getElementsByTagName("karakter");
        for (int i = 0; i < list.getLength(); i++) {
            Element karakter = (Element) list.item(i);
            if (karakterId.equals(karakter.getAttribute("kkod"))) {

                Element nevElem = (Element) karakter.getElementsByTagName("nev").item(0);
                String csaladnev = getTagText(nevElem, "csaladnev");
                String keresztnev = getTagText(nevElem, "keresztnev");

                return csaladnev + " " + keresztnev;
            }
        }
        return "(ismeretlen karakter)";
    }

    //Id alapján megkeresi, hogy van-e olyan felhasználó
    private static String findFelhasznaloNevById(Document doc, String felhasznaloId) {
        NodeList list = doc.getElementsByTagName("felhasznalo");
        for (int i = 0; i < list.getLength(); i++) {
            Element f = (Element) list.item(i);
            if (felhasznaloId.equals(f.getAttribute("fkod"))) {
                return getTagText(f, "nev");
            }
        }
        return "(ismeretlen felhasználó)";
    }

    //Id alapján megkeresi, hogy van-e olyan műfaj
    private static String findMufajIdByNev(Document doc, String mufajNev) {
        NodeList list = doc.getElementsByTagName("mufaj");
        for (int i = 0; i < list.getLength(); i++) {
            Element m = (Element) list.item(i);
            String nev = getTagText(m, "nev");
            if (mufajNev.equals(nev)) {
                return m.getAttribute("mkod");
            }
        }
        return null;
    }

    //Adott gyermek tag szövegének kiolvasása
    private static String getTagText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) {
            return "";
        }
        return list.item(0).getTextContent().trim();
    }
}
