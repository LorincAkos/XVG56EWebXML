package xvg56edomparse;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;

public class XVG56EDOMRead {

    public static void main(String[] args) {
        // XML fájl neve
        String inputFileName = "XVG56E_XML.xml";
        // Kimeneti fájl neve
        String outputFileName = "output_XML.txt";

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File(inputFileName));
            
            doc.getDocumentElement().normalize();

            try (PrintWriter writer = new PrintWriter(outputFileName, "UTF-8")) {

                // Gyökérelem kiírása
                System.out.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
                writer.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
                writer.println();

                // Feldolgozás külön metódusokba
                printFelhasznalok(doc, writer);
                printOsszefoglalok(doc, writer);
                printStudiok(doc, writer);
                printAnimek(doc, writer);
                printKarakterek(doc, writer);
                printMegjelenesek(doc, writer);
                printMufajok(doc, writer);
                printAnimeMufajok(doc, writer);
                printSajatListak(doc, writer);

                System.out.println("\nFeldolgozás kész. Eredmény mentve ide: " + outputFileName);
            }

        } catch (Exception e) {
            System.err.println("Hiba történt az XML feldolgozása közben: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------- FELHASZNÁLÓ ----------

    private static void printFelhasznalok(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("felhasznalo");
        writer.println("==== FELHASZNÁLÓK ====");
        System.out.println("==== FELHASZNÁLÓK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element f = (Element) list.item(i);

            String fkod = f.getAttribute("fkod");
            String nev = getTagText(f, "nev");
            String email = getTagText(f, "email");
            String reg = getTagText(f, "regisztracio");

            writer.println("Felhasználó kód: " + fkod);
            writer.println("  Név: " + nev);
            writer.println("  Email: " + email);
            writer.println("  Regisztráció: " + reg);
            writer.println("  Kedvenc műfajok:");

            System.out.println("Felhasználó kód: " + fkod);
            System.out.println("  Név: " + nev);
            System.out.println("  Email: " + email);
            System.out.println("  Regisztráció: " + reg);
            System.out.println("  Kedvenc műfajok:");

            NodeList mufajok = f.getElementsByTagName("kedvencmufaj");
            for (int j = 0; j < mufajok.getLength(); j++) {
                String km = mufajok.item(j).getTextContent().trim();
                writer.println("    - " + km);
                System.out.println("    - " + km);
            }

            writer.println();
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- ÖSSZEFOGLALÓ ----------

    private static void printOsszefoglalok(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("osszefoglalo");
        writer.println("==== ÖSSZEFOGLALÓK ====");
        System.out.println("==== ÖSSZEFOGLALÓK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element o = (Element) list.item(i);

            String okod = o.getAttribute("okod");
            String ofk = o.getAttribute("o_f");
            String total = getTagText(o, "totalanime");
            String atlag = getTagText(o, "atlagpont");
            String utolso = getTagText(o, "utolsofrissites");
            String ido = getTagText(o, "totalido");

            writer.println("Összefoglaló kód: " + okod + " (felhasználó: " + ofk + ")");
            writer.println("  Összes anime: " + total);
            writer.println("  Átlagpont: " + atlag);
            writer.println("  Utolsó frissítés: " + utolso);
            writer.println("  Összes idő (óra): " + ido);
            writer.println();

            System.out.println("Összefoglaló kód: " + okod + " (felhasználó: " + ofk + ")");
            System.out.println("  Összes anime: " + total);
            System.out.println("  Átlagpont: " + atlag);
            System.out.println("  Utolsó frissítés: " + utolso);
            System.out.println("  Összes idő (óra): " + ido);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- STÚDIÓ ----------

    private static void printStudiok(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("studio");
        writer.println("==== STÚDIÓK ====");
        System.out.println("==== STÚDIÓK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element s = (Element) list.item(i);

            String skod = s.getAttribute("skod");
            String nev = getTagText(s, "nev");
            String orszag = getTagText(s, "orszag");
            String alapitas = getTagText(s, "alapitas");
            String projektek = getTagText(s, "projektek");

            writer.println("Stúdió kód: " + skod);
            writer.println("  Név: " + nev);
            writer.println("  Ország: " + orszag);
            writer.println("  Alapítás: " + alapitas);
            writer.println("  Projektek száma: " + projektek);
            writer.println();

            System.out.println("Stúdió kód: " + skod);
            System.out.println("  Név: " + nev);
            System.out.println("  Ország: " + orszag);
            System.out.println("  Alapítás: " + alapitas);
            System.out.println("  Projektek száma: " + projektek);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- ANIME ----------

    private static void printAnimek(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("anime");
        writer.println("==== ANIMEK ====");
        System.out.println("==== ANIMEK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element a = (Element) list.item(i);

            String akod = a.getAttribute("akod");
            String studioRef = a.getAttribute("a_s");
            String cim = getTagText(a, "cim");
            String tipus = getTagText(a, "tipus");
            String kezdes = getTagText(a, "kezdes");
            String befejezes = getTagText(a, "befejezes");
            String epizod = getTagText(a, "epizod");

            writer.println("Anime kód: " + akod + " (stúdió: " + studioRef + ")");
            writer.println("  Cím: " + cim);
            writer.println("  Típus: " + tipus);
            writer.println("  Kezdés: " + kezdes);
            writer.println("  Befejezés: " + befejezes);
            writer.println("  Epizódok: " + epizod);
            writer.println();

            System.out.println("Anime kód: " + akod + " (stúdió: " + studioRef + ")");
            System.out.println("  Cím: " + cim);
            System.out.println("  Típus: " + tipus);
            System.out.println("  Kezdés: " + kezdes);
            System.out.println("  Befejezés: " + befejezes);
            System.out.println("  Epizódok: " + epizod);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- KARAKTER ----------

    private static void printKarakterek(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("karakter");
        writer.println("==== KARAKTEREK ====");
        System.out.println("==== KARAKTEREK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element k = (Element) list.item(i);

            String kkod = k.getAttribute("kkod");

            Element nevElem = (Element) k.getElementsByTagName("nev").item(0);
            String csaladnev = getTagText(nevElem, "csaladnev");
            String keresztnev = getTagText(nevElem, "keresztnev");

            String kor = getTagText(k, "kor");
            String nem = getTagText(k, "nem");

            writer.println("Karakter kód: " + kkod);
            writer.println("  Név: " + csaladnev + " " + keresztnev);
            writer.println("  Kor: " + kor);
            writer.println("  Nem: " + nem);
            writer.println();

            System.out.println("Karakter kód: " + kkod);
            System.out.println("  Név: " + csaladnev + " " + keresztnev);
            System.out.println("  Kor: " + kor);
            System.out.println("  Nem: " + nem);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- MEGJELENÉS ----------

    private static void printMegjelenesek(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("megjelenes");
        writer.println("==== MEGJELENÉSEK ====");
        System.out.println("==== MEGJELENÉSEK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element m = (Element) list.item(i);

            String animeRef = m.getAttribute("a_k_a");
            String karakterRef = m.getAttribute("a_k_k");
            String szerep = getTagText(m, "szerep");

            writer.println("Anime: " + animeRef + " - Karakter: " + karakterRef);
            writer.println("  Szerep: " + szerep);
            writer.println();

            System.out.println("Anime: " + animeRef + " - Karakter: " + karakterRef);
            System.out.println("  Szerep: " + szerep);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- MŰFAJ ----------

    private static void printMufajok(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("mufaj");
        writer.println("==== MŰFAJOK ====");
        System.out.println("==== MŰFAJOK ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element m = (Element) list.item(i);

            String mkod = m.getAttribute("mkod");
            String nev = getTagText(m, "nev");
            String leiras = getTagText(m, "leiras");
            String felnott = getTagText(m, "felnott");
            String rang = getTagText(m, "nepszerusegrang");

            writer.println("Műfaj kód: " + mkod);
            writer.println("  Név: " + nev);
            writer.println("  Leírás: " + leiras);
            writer.println("  Felnőtt tartalom: " + felnott);
            writer.println("  Népszerűségi rang: " + rang);
            writer.println();

            System.out.println("Műfaj kód: " + mkod);
            System.out.println("  Név: " + nev);
            System.out.println("  Leírás: " + leiras);
            System.out.println("  Felnőtt tartalom: " + felnott);
            System.out.println("  Népszerűségi rang: " + rang);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- ANIME–MŰFAJ ----------

    private static void printAnimeMufajok(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("animemufaj");
        writer.println("==== ANIME–MŰFAJ ====");
        System.out.println("==== ANIME–MŰFAJ ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element am = (Element) list.item(i);

            String animeRef = am.getAttribute("a_m_a");
            String mufajRef = am.getAttribute("a_m_m");
            String elsodleges = getTagText(am, "elsodleges");

            writer.println("Anime: " + animeRef + " - Műfaj: " + mufajRef);
            writer.println("  Elsődleges: " + elsodleges);
            writer.println();

            System.out.println("Anime: " + animeRef + " - Műfaj: " + mufajRef);
            System.out.println("  Elsődleges: " + elsodleges);
            System.out.println();
        }
        writer.println();
        System.out.println();
    }

    // ---------- SAJÁT LISTA ----------

    private static void printSajatListak(Document doc, PrintWriter writer) {
        NodeList list = doc.getElementsByTagName("sajatlista");
        writer.println("==== SAJÁT LISTA ====");
        System.out.println("==== SAJÁT LISTA ====");

        for (int i = 0; i < list.getLength(); i++) {
            Element sl = (Element) list.item(i);

            String animeRef = sl.getAttribute("f_a_a");
            String felhRef = sl.getAttribute("f_a_f");
            String statusz = getTagText(sl, "statusz");
            String pontszam = getTagText(sl, "pontszam");

            writer.println("Felhasználó: " + felhRef + " - Anime: " + animeRef);
            writer.println("  Státusz: " + statusz);
            writer.println("  Pontszám: " + pontszam);
            writer.println();

            System.out.println("Felhasználó: " + felhRef + " - Anime: " + animeRef);
            System.out.println("  Státusz: " + statusz);
            System.out.println("  Pontszám: " + pontszam);
            System.out.println();
        }
        writer.println();
        System.out.println();
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

