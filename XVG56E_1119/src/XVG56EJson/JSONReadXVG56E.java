package XVG56EJson;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReadXVG56E {
	public static void main(String[] args) {
		try(FileReader reader = new FileReader("orarendXVG56E.json")){

			JSONParser jsonParser = new JSONParser();
			 JSONObject root = (JSONObject)jsonParser.parse(reader);

	            JSONObject orarend = (JSONObject) root.get("xvg56e_orarend");

	            JSONArray orak = (JSONArray) orarend.get("ora");
			System.out.println("Órarend: Mérnökinformatika 2025\n");
			
			for (Object o : orak) {

                JSONObject ora = (JSONObject) o;

                System.out.println("targy: " + ora.get("targy"));

                JSONObject idopont = (JSONObject) ora.get("idopont");
                System.out.println("nap: " + idopont.get("nap"));
                System.out.println("tol: " + idopont.get("tol"));
                System.out.println("ig:  " + idopont.get("ig"));

                System.out.println("helyszin: " + ora.get("helyszin"));
                System.out.println("oktato: " + ora.get("oktato"));
                System.out.println("szak: " + ora.get("szak"));
                System.out.println("tipus: " + ora.get("_tipus"));

                System.out.println();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
}
