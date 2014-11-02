import java.util.ArrayList;
import java.util.HashMap;


public class Entity {
	private static final HashMap<String, String> GENDER_ENTITY;
    static
    {
    	GENDER_ENTITY = new HashMap<String, String>();
    	GENDER_ENTITY.put("Alessandro", "he");
    	GENDER_ENTITY.put("Benjamin", "he");
    	GENDER_ENTITY.put("Catherine", "she");
    	GENDER_ENTITY.put("Chiara", "she");
    	GENDER_ENTITY.put("David", "he");
    	GENDER_ENTITY.put("Didier", "he");
    	GENDER_ENTITY.put("Kyoto", "it");
    	GENDER_ENTITY.put("Olivier", "he");
    	GENDER_ENTITY.put("Paolo", "he");
    	GENDER_ENTITY.put("Tokyo", "it");
    }
	private String uri;
	private String name;
	private String[] identifiants;
	private ArrayList<Triplet>triplets;
	
	public Entity(String uri) {
		this.uri = uri;
		this.name = extractName(uri);
		String[]identifiants = splitName(name);
		this.identifiants = new String[identifiants.length + 1];
		for (int i=0; i<identifiants.length; i++) {
			this.identifiants[i] = identifiants[i].toLowerCase();
		}
		this.identifiants[identifiants.length] = GENDER_ENTITY.get(identifiants[0]);
		this.triplets = new ArrayList<>();
	}
	
	public String getUri(){
		return this.uri;
	}
	
	public String[] getIdentifiants(){
		return this.identifiants;
	}
	
	public void addTriplet(String property, String value) {
		this.triplets.add(new Triplet(uri, property, value));
	}
	
	private String extractName(String chaine) {
		int n = chaine.length() - 1;
		while (n >= 0) {
			if (chaine.charAt(n) == '/') {
				return chaine.substring(n + 1);
			}
			n--;
		}
		return null;
	}
	
	private String[] splitName(String fileName) {
		String[] s = fileName.replace(",", "").replace("\n", "").split("_");
		return s;
	}
	
	public String toString(){
		return "<entity name=\"" + uri + "\">";
	}
	
	public String toStringTriplet() {
		String s = "";
		for(Triplet triplet : this.triplets) {
			s += triplet.toString() + "\n";
		}
		return s;
	}
	
	public int getNbTriplet(){
		return this.triplets.size();
	}

}
