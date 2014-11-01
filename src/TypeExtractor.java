
public class TypeExtractor {

	public static void typeExtractor(Entity currentEntity, String[] words, int index) {
		if ((words[index].equals("is") || words[index].equals("was"))
				&& (words[index + 1].equals("a") || words[index + 1]
						.equals("an"))) {
			findType(currentEntity, words, index + 2);
		}
	}

	private static void findType(Entity currentEntity, String[] words, int index) {
		String type = "";
		boolean stop = false;
		for (int i = index; i < words.length; i++) {
			if (words[i].equals("and")) {
				currentEntity.addTriplet("type", type.substring(0, type.length() - 1));
				//createAndAddTriple(currentEntity.getUri(), "type",
					//	type.substring(0, type.length() - 1));
				type = "";
				stop = true;
			} else {
				if (words[i].equals("from") || words[i].equals("who")
						|| words[i].equals("of")) {
					currentEntity.addTriplet("type", type.substring(0, type.length() - 1));
					//createAndAddTriple(currentEntity.getUri(), "type",
						//	type.substring(0, type.length() - 1));
					break;
				}
				if (words[i].length() > 2) {
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& type.equals(""))
						break;
					if (words[i].substring(words[i].length() - 2).equals("ed")
							&& !type.equals("")) {
						currentEntity.addTriplet("type", type.substring(0, type.length() - 1));
						//createAndAddTriple(currentEntity.getUri(), "type",
							//	type.substring(0, type.length() - 1));
						break;
					}
				}
				type += words[i] + " ";
				if (endByPoint(words[i]) || endByVirgule(words[i])) {
					currentEntity.addTriplet("type", type.substring(0, type.length() - 2));
					//createAndAddTriple(currentEntity.getUri(), "type",
						//	type.substring(0, type.length() - 2));
					type = "";
					if (stop || endByPoint(words[i]))
						break;
				}
			}
		}
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
