import java.util.ArrayList;
import java.util.HashMap;

public class Entity {
	private static final HashMap<String, ArrayList<String>> GENDER_ENTITY;
	static {
		GENDER_ENTITY = new HashMap<String, ArrayList<String>>();
		GENDER_ENTITY.put("Alessandro", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Benjamin", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Catherine", new ArrayList<String>() {
			{
				add("she");
			}
		});
		GENDER_ENTITY.put("Chiara", new ArrayList<String>() {
			{
				add("she");
			}
		});
		GENDER_ENTITY.put("David", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Didier", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Kyoto", new ArrayList<String>() {
			{
				add("it");
			}
		});
		GENDER_ENTITY.put("Olivier", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Paolo", new ArrayList<String>() {
			{
				add("he");
			}
		});
		GENDER_ENTITY.put("Tokyo", new ArrayList<String>() {
			{
				add("Metropolis");
				add("it");
			}
		});
	}
	private String uri;
	private String name;
	private String[] identifiants;
	private ArrayList<Triplet> triplets;

	public Entity(String uri) {
		this.uri = uri;
		this.name = extractName(uri);
		String[] id = splitName(name);
		ArrayList<String> idAdditional = GENDER_ENTITY.get(id[0]);
		if (idAdditional == null)
			idAdditional = new ArrayList<>();
		this.identifiants = new String[id.length + idAdditional.size()];

		for (int i = 0; i < id.length; i++) {
			this.identifiants[i] = id[i].toLowerCase();
		}
		for (int i = 0; i < idAdditional.size(); i++)
			this.identifiants[id.length + i] = idAdditional.get(i).toLowerCase();
		this.triplets = new ArrayList<>();
	}

	public String getUri() {
		return this.uri;
	}

	public String[] getIdentifiants() {
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

	public String toString() {
		return "<entity name=\"" + uri + "\">";
	}

	public String toStringTriplet() {
		String s = "";
		for (Triplet triplet : this.triplets) {
			s += triplet.toString() + "\n";
		}
		return s;
	}

	public int getNbTriplet() {
		return this.triplets.size();
	}

}
