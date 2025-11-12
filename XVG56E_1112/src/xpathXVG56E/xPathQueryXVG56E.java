package xpathXVG56E;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xPathQueryXVG56E {
    static String neptunkod = "XVG56E";

    public static void main(String[] args) throws Exception {
        String xmlPath = "studentXVG56E.xml";
        if (args.length > 1 && !args[1].isEmpty()) neptunkod = args[1].trim();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        XPath xpath = XPathFactory.newInstance().newXPath();

        StringBuilder commented = new StringBuilder();

        run(doc, xpath, 1,  "Válassza ki az összes student elemet, amely a class gyermekei!", "/class/student", commented);
        run(doc, xpath, 2,  "Válassza ki azt a student elemet, amelynek id attribútuma '02'!", "//student[@id='02' or @id='2']", commented);
        run(doc, xpath, 3,  "Válassza ki az összes student elemet, bárhol a dokumentumban!", "//student", commented);
        run(doc, xpath, 4,  "Válassza ki a második student elemet, amely a class root elemet gyermeke!", "/class/student[2]", commented);
        run(doc, xpath, 5,  "Válassza ki az utolsó student elemet, amely a class root elemet gyermeke!", "/class/student[last()]", commented);
        run(doc, xpath, 6,  "Válassza ki az utolsó előtti student elemet, amely a class root elemet gyermeke!", "/class/student[last()-1]", commented);
        run(doc, xpath, 7,  "Válassza ki az első két student elemet, amelyek a root elemet gyermekei!", "/class/student[position()<=2]", commented);
        run(doc, xpath, 8,  "Válassza ki a class root elemet összes gyermek elemét!", "/class/*", commented);
        run(doc, xpath, 9,  "Válassza ki az összes student elemet, amely legalább egy attribútummal rendelkezik!", "//student[@*]", commented);
        run(doc, xpath, 10, "Válassza ki a dokumentum összes elemét!", "//*", commented);
        run(doc, xpath, 11, "Válassza ki a class root elemet összes student elemét, amelynél a kor > 20!", "/class/student[kor > 20]", commented);
        run(doc, xpath, 12, "Válassza ki az összes student elem összes keresztnev vagy vezeteknev csomópontját!", "//student/keresztnev | //student/vezeteknev", commented);

        Path out = Paths.get("utasitasok_" + neptunkod + ".java");
        Files.writeString(out, commented.toString(), StandardCharsets.UTF_8);
        System.out.println("A kommentelt utasítások elmentve ide: " + out.toAbsolutePath());
    }

    private static void run(Document doc, XPath xpath, int no, String title, String expr, StringBuilder commented) throws Exception {
        NodeList nodes = (NodeList) xpath.evaluate(expr, doc, XPathConstants.NODESET);

        System.out.println("=== " + no + ". feladat ===");
        System.out.println(title);
        System.out.println("Találatok: " + nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            System.out.println("[" + (i + 1) + "] " + describeNode(n));
        }

        // Utasítás sor megjegyzésbe
        String commentedLine = "// " + expr;
        System.out.println(commentedLine + "\n");
        commented.append(commentedLine).append(System.lineSeparator());
    }

    private static String describeNode(Node n) {
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE -> {
                Element e = (Element) n;
                StringBuilder sb = new StringBuilder();
                sb.append("<").append(e.getTagName());
                if (e.hasAttributes()) {
                    NamedNodeMap atts = e.getAttributes();
                    for (int j = 0; j < atts.getLength(); j++) {
                        Node a = atts.item(j);
                        sb.append(" ").append(a.getNodeName()).append("=\"").append(a.getNodeValue()).append("\"");
                    }
                }
                sb.append(">");
                if (!hasElementChildren(e)) {
                    String text = e.getTextContent() == null ? "" : e.getTextContent().trim().replaceAll("\\s+", " ");
                    if (!text.isEmpty()) sb.append(" ").append(text).append(" ");
                }
                sb.append("</").append(e.getTagName()).append(">");
                return sb.toString();
            }
            case Node.ATTRIBUTE_NODE -> {
                return "@" + n.getNodeName() + "=\"" + n.getNodeValue() + "\"";
            }
            case Node.TEXT_NODE -> {
                return n.getNodeValue().trim();
            }
            default -> {
                return n.getNodeName();
            }
        }
    }

    private static boolean hasElementChildren(Element e) {
        NodeList ch = e.getChildNodes();
        for (int i = 0; i < ch.getLength(); i++) {
            if (ch.item(i).getNodeType() == Node.ELEMENT_NODE) return true;
        }
        return false;
    }
}
