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

	private static final String DIR_NAME = "Wikipedia_corpus";
	private static final String LIST_ENTITY = "entity_list.txt";
	private ArrayList<Entity> myEntities;
	private Entity currentEntity;
	private String currentFile;
	private boolean firstLine;

	public ParserEntity() {
		this.firstLine = true;
		this.myEntities = new ArrayList<>();
		readEntities();

		File repertoire = new File(DIR_NAME);
		File[] files = repertoire.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println("LOL " + files[i].toString());
			this.currentFile = extractFileName(files[i].toString());
			this.read(files[i]);
			System.out.println();
		}

		// this.currentFile = "Didier_Pironi";
		// File files = new File("Wikipedia_corpus/Didier_Pironi.txt");
		// read(files);
		// Afficher les triplets
		System.out.println("ENTITY");
		int nbT = 0;
		for (Entity entity : myEntities) {
			System.out.print(entity.toStringTriplet());
			nbT++;
		}
		System.out.println("NB entities " + this.myEntities.size()
				+ " nbTriplet " + nbT);
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

	private void parseLine(String line) {
		// System.out.println("LINE " + line);
		String sortie = "";
		String[] s = line.split(" ");
		for (int i = 0; i < s.length; i++) {
			boolean found = false;

			for (int j = 0; j < myEntities.size(); j++) {
				int n = checkWords(s, i, myEntities.get(j).getIdentifiants(), 0);
				if (n != 0) {
					if ((n != 1
							|| checkWordAfterBefore(s, i, myEntities.get(j)
									.getIdentifiants())) || i<10) {
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

				if (firstLine) {
					TypeExtractor.typeExtractor(currentEntity, s, i);
				}
			} else {
				i--;
			}

		}
		firstLine = false;
		writeFile(sortie + "\n");
	}

	private boolean checkWordAfterBefore(String[] words, int index,
			String[] identifiants) {
		if (words[index].charAt(words[index].length() - 1) != ','
				&& words[index].charAt(words[index].length() - 1) != '.'
				&& startByMaj(words[index + 1])) {
			return false;
		}
		if (index > 0) {
			if (startByMaj(words[index - 1])
					&& words[index - 1].charAt(words[index - 1].length() - 1) != '.'
					&& words[index - 1].charAt(words[index - 1].length() - 1) != ',') {
				for (int i = 0; i < identifiants.length; i++) {

					if (words[index - 1].equals(identifiants[i]))
						return true;
				}
				return false;
			}
		}
		return true;
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
				|| words[indexW].toLowerCase().equals(entity[indexE] + ".")
				|| words[indexW].toLowerCase().equals(entity[indexE] + ",") || words[indexW].toLowerCase().equals(entity[indexE] + "'s")) {
			if ((entity[indexE].equals("he") || entity[indexE].equals("she") || entity[indexE]
					.equals("it")) && !(compareToFileEntity(entity)))
				return 0;
			return 1 + checkWords(words, ++indexW, entity, ++indexE);
		} else {
			return checkWords(words, indexW, entity, ++indexE);
		}

	}

	private String extractFileName(String chaine) {
		int n = chaine.length() - 1;
		while (n >= 0) {
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