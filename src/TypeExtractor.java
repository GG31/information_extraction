import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeExtractor {
	private static final String[] WORD_STOP = { "from", "who", "of", "where",
			"for" };
	private static final String[] PATTERN_TYPE = { "is a", "is an", "was a",
			"was an" };
	private static final String REGULAR_EXPRESSION1 = ".*entity name=\"(\\D+)\".*entity(\\D+) ";
	private static final String REGULAR_EXPRESSION2 = " (\\D+)";

	public static void typeExtractor(String line) {
		if (line != null && line.length() > 0) {
			String pattern = "[^\\p{Punct}\\sa-zA-Z0-9]";
			line = line.replaceAll(pattern, "");
			pattern = "[\\[\\d\\]]";
			line = line.replaceAll(pattern, "");
			String[] part = line.split("\\. ");
			for (String p : part) {
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
			String uri = m.group(1);
			if(!m.group(2).contains("and")){
			String[] words = m.group(3).split(" ");
			findType(uri, words, 0);
			}
		}

	}

	private static void findType(String uri, String[] words, int index) {
		String type = "";
		boolean stop = false;
		for (int i = index; i < words.length; i++) {
			if (words[i].equals("and")) {
				ParserEntity.triplets.add(new Triplet(uri, "type", type.substring(0,
						type.length() - 1)));
				type = "";
				stop = true;
			} else {
				if (wordStop(words[i])) {
					ParserEntity.triplets.add(new Triplet(uri, "type", type.substring(0,
							type.length() - 1)));
					break;
				}
				if (words[i].length() > 2) {
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& type.equals(""))
						break;
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& !type.equals("")) {
						ParserEntity.triplets.add(new Triplet(uri, "type", type.substring(0,
								type.length() - 1)));
						break;
					}
				}
				type += words[i] + " ";
				if (endByPoint(words[i]) || endByVirgule(words[i])) {
					ParserEntity.triplets.add(new Triplet(uri, "type", type.substring(0,
							type.length() - 2)));
					type = "";
					if (stop || endByPoint(words[i]))
						break;
				}
			}
		}
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
