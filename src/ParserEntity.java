import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ParserEntity {
	private static final char[] PONCTUATIONS = { '.', ',', ';', ':', '[', '(', ')', ']' };
	private static final String DIR_NAME = "Wikipedia_corpus";
	private static final String DIR_NAME_WITH_ENTITY = "Sortie";
	private static final String LIST_ENTITY = "entity_list.txt";
	private ArrayList<Entity> myEntities;
	public static ArrayList<Triplet> triplets;
	private Entity currentEntity;
	private String currentFile;
	private boolean firstLine;

	public ParserEntity() {
		this.firstLine = true;
		this.myEntities = new ArrayList<>();
		this.triplets = new ArrayList<>();
		readEntities();

		File repertoire = new File(DIR_NAME);
		File[] files = repertoire.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println("LOL " + files[i].toString());
			this.currentFile = extractName(files[i].toString());
			this.read(files[i]);
			System.out.println();
		}

		File repertoireWithEntity = new File(DIR_NAME_WITH_ENTITY);
		File[] filesWithEntity = repertoireWithEntity.listFiles();
		for (int i = 0; i < filesWithEntity.length; i++) {
			this.readForPattern(filesWithEntity[i]);
		}

		// Afficher les triplets
		System.out.println("ENTITY");
		int nbT = 0;
		for(Triplet t: this.triplets){
			System.out.println(t.toString());
			nbT++;
		}
		System.out.println("NB triplet type "+ nbT);
	}

	private void readEntities() {
		File file = new File(LIST_ENTITY);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);

			// Here BufferedInputStream is added for fast reading.
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			// dis.available() returns 0 if the file does not have more lines.
			while (dis.available() != 0) {
				String s = dis.readLine();
				Entity e = new Entity(s);
				checkTripletFromName(s, extractName(s));
				myEntities.add(e);
			}

			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void read(File file) {
		// File file = new File(fileName);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		firstLine = true;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0) {
				parseLine(dis.readLine());
			}
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void readForPattern(File file) {
		// File file = new File(fileName);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		firstLine = true;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0) {
				String line = dis.readLine();
				PatternExtractor.extractPatterns(line);
				TypeExtractor.typeExtractor(line);
			}
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void parseLine(String line) {
		// System.out.println("LINE " + line);
		String sortie = "";
		String[] s = line.split(" ");
		for (int i = 0; i < s.length; i++) {
			boolean found = false;

			for (int j = 0; j < myEntities.size(); j++) {
				int n = checkWords(s, i, myEntities.get(j).getIdentifiants(), 0);
				if (n != 0) {
					if ((n != 1 || checkWordAfterBefore(s, i, myEntities.get(j)
							.getIdentifiants()))
							|| (i < 10 && firstLine)) {
						// Verify word before si l'un des deux est un mot
						// commençant par une majuscule ET n'appartenant pas aux
						// identifiants alors out

						this.currentEntity = myEntities.get(j);
						sortie += myEntities.get(j).toString();
						for (int k = 0; k < n; k++) {
							sortie += s[i++];
							if (k != n - 1) {
								sortie += " ";
							}
						}
						sortie += "</entity> ";
						found = true;
					}
				}
			}

			if (!found) {
				sortie += s[i] + " ";
				// check date
				DateExtractor.dateExtractor(currentEntity, s, i);

			} else {
				i--;
			}

		}
		firstLine = false;
		writeFile(sortie + "\n");
	}

	private boolean checkWordAfterBefore(String[] words, int index,
			String[] identifiants) {
		if (!endByPonctuation(words[index]) && startByMaj(words[index + 1])) {
			return false;
		}
		if (index > 0) {
			if (startByMaj(words[index - 1])
					&& !endByPonctuation(words[index - 1])) {
				for (int i = 0; i < identifiants.length; i++) {

					if (words[index - 1].equals(identifiants[i]))
						return true;
				}
				return false;
			}
		}
		return true;
	}

	private boolean endByPonctuation(String word) {
		for (char c : PONCTUATIONS) {
			if (word.charAt(word.length() - 1) == c)
				return true;
		}
		return false;
	}

	private boolean startByMaj(String word) {
		if (word.charAt(0) >= 65 && word.charAt(0) <= 90)
			return true;
		return false;
	}

	private int checkWords(String[] words, int indexW, String[] entity,
			int indexE) {
		if (indexE >= entity.length || indexW >= words.length)
			return 0;
		if (words[indexW].toLowerCase().equals(entity[indexE])
				|| compareWithPonct(words[indexW], entity[indexE])) {
			if ((entity[indexE].equals("he") || entity[indexE].equals("she") || entity[indexE]
					.equals("it")) && !(compareToFileEntity(entity)))
				return 0;
			return 1 + checkWords(words, ++indexW, entity, ++indexE);
		} else {
			return checkWords(words, indexW, entity, ++indexE);
		}
	}

	private boolean compareWithPonct(String word, String wordEntity) {
		for (char c : PONCTUATIONS) {
			if (lastChar(word) == c) {
				if (word.toLowerCase().equals(wordEntity + c)) {
					return true;
				}else if (c == ']') {
					if (word.toLowerCase().indexOf('[') > 0) {
						return compareWithPonct(
								word.substring(0, word.indexOf('[')),
								wordEntity);
					}
				}
			}
		}
		if (word.toLowerCase().equals(wordEntity + "'s"))
			return true;
		return false;
	}

	private char lastChar(String word) {
		if(word.length()>0)
		return word.charAt(word.length() - 1);
		return ' ';
	}
	
	private String extractName(String chaine) {
		int n = chaine.length() - 1;
		while (n >= 0) {
			// System.out.println(s.charAt(n));
			if (chaine.charAt(n) == '/') {
				return chaine.substring(n + 1);
			}
			n--;
		}
		return null;
	}

	private boolean compareToFileEntity(String[] identifiants) {
		String[] s = splitFileName(this.currentFile.toLowerCase());
		for (int i = 0; i < s.length; i++) {
			if (!s[i].equals(identifiants[i]))
				return false;
		}
		return true;
	}

	private String[] splitFileName(String fileName) {
		String[] s = fileName.substring(0, fileName.length() - 4)
				.replace(",", "").replace("\n", "").split("_");
		return s;
	}
	
	private void checkTripletFromName(String uri, String name) {
		if(name.indexOf(',') != -1) {
			this.triplets.add(new Triplet(uri, "type", "city"));
		}
		int n = name.indexOf('(');
		if(n != -1) {
			String value = name.substring(n+1, name.length()-1).replace("_", " ");
			this.triplets.add(new Triplet(uri, "type", value));
		}
	}

	private void writeFile(String content) {
		try {
			FileWriter fw = new FileWriter("Sortie/" + this.currentFile, true);
			BufferedWriter output = new BufferedWriter(fw);
			output.write(content);
			output.flush();
			output.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
