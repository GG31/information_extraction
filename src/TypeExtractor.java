import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeExtractor {
	private static final String[] WORD_STOP = { "from", "who", "of", "where",
			"for" };
	private static final String[] PATTERN_TYPE = { "is a", "is an", "was a",
			"was an" };
	private static final String REGULAR_EXPRESSION1 = ".*entity name=\"(\\D+)\".*entity(\\D+) ";
	private static final String REGULAR_EXPRESSION2 = " (\\D+)";
	public static ArrayList<Triplet> triplets = new ArrayList<>();

	public static void typeExtractor(Entity currentEntity, String[] words,
			int index) {
		if (words.length > index + 1
				&& matchPattern(words[index], words[index + 1])) {
			findType(currentEntity, words, index + 2);
		}
	}

	public static void typeExtractor(String line) {
		if (line != null && line.length() > 0) {
			String pattern = "[^\\p{Punct}\\sa-zA-Z0-9]";
			line = line.replaceAll(pattern, "");
			System.out.println("YOP "+ line);
			pattern = "[\\[\\d\\]]";
			line = line.replaceAll(pattern, "");
			String[] part = line.split("\\. ");
			for (String p : part) {
				System.out.println("PArt" + p);
				for (int i = 0; i < PATTERN_TYPE.length; i++) {
					checkPattern(p + ".", "type", PATTERN_TYPE[i]);
				}
			}
		}
	}

	private static void checkPattern(String line, String patternKey,
			String patternPossible) {
		Pattern p1 = Pattern.compile(REGULAR_EXPRESSION1 + patternPossible
				+ REGULAR_EXPRESSION2);
		Matcher m = p1.matcher(line);
		if (m.matches()) {
			System.out.println("MATCH " + line);
			// triplet.add(new Triplet(m.group(1), patternKey, m.group(2)));
			String uri = m.group(1);
			if(!m.group(2).contains("and")){
			String[] words = m.group(3).split(" ");
			findType(uri, words, 0);
			}else{
				System.out.println("POIUY " + m.group(2));
			}
		}

	}

	private static void findType(String uri, String[] words, int index) {
		String type = "";
		boolean stop = false;
		for (int i = index; i < words.length; i++) {
			if (words[i].equals("and")) {
				triplets.add(new Triplet(uri, "type", type.substring(0,
						type.length() - 1)));
				// currentEntity.addTriplet("type", type.substring(0,
				// type.length() - 1));
				type = "";
				stop = true;
			} else {
				if (wordStop(words[i])) {
					System.out.println(":P");
					triplets.add(new Triplet(uri, "type", type.substring(0,
							type.length() - 1)));
					// currentEntity.addTriplet("type", type.substring(0,
					// type.length() - 1));
					break;
				}
				if (words[i].length() > 2) {
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& type.equals(""))
						break;
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& !type.equals("")) {
						triplets.add(new Triplet(uri, "type", type.substring(0,
								type.length() - 1)));
						// currentEntity.addTriplet("type", type.substring(0,
						// type.length() - 1));
						break;
					}
				}
				type += words[i] + " ";
				System.out.println("LOL3" +words[i]);
				if (endByPoint(words[i]) || endByVirgule(words[i])) {
					triplets.add(new Triplet(uri, "type", type.substring(0,
							type.length() - 2)));
					// currentEntity.addTriplet("type", type.substring(0,
					// type.length() - 2));
					type = "";
					if (stop || endByPoint(words[i]))
						break;
				}
			}
		}
	}

	private static void findType(Entity currentEntity, String[] words, int index) {
		String type = "";
		boolean stop = false;
		for (int i = index; i < words.length; i++) {
			if (words[i].equals("and")) {
				currentEntity.addTriplet("type",
						type.substring(0, type.length() - 1));
				type = "";
				stop = true;
			} else {
				if (wordStop(words[i])) {
					currentEntity.addTriplet("type",
							type.substring(0, type.length() - 1));
					break;
				}
				if (words[i].length() > 2) {
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& type.equals(""))
						break;
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& !type.equals("")) {
						currentEntity.addTriplet("type",
								type.substring(0, type.length() - 1));
						break;
					}
				}
				type += words[i] + " ";
				
				if (endByPoint(words[i]) || endByVirgule(words[i])) {
					currentEntity.addTriplet("type",
							type.substring(0, type.length() - 2));
					type = "";
					if (stop || endByPoint(words[i]))
						break;
				}
			}
		}
	}

	private static boolean matchPattern(String word1, String word2) {
		for (String s : PATTERN_TYPE) {
			if ((word1 + " " + word2).equals(s))
				return true;
		}
		return false;
	}

	private static boolean wordStop(String word) {
		for (String s : WORD_STOP) {
			if (word.equals(s))
				return true;
		}
		return false;
	}

	private static boolean endByPoint(String s) {
		
		char lastChar = s.charAt(s.length() - 1);
		if (lastChar == '.') {
			return true;
		}
		return false;
	}

	private static boolean endByVirgule(String s) {
		char lastChar = s.charAt(s.length() - 1);
		if (lastChar == ',') {
			return true;
		}
		return false;
	}
}
