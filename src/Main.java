import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Main {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParserEntity parseEntity = new ParserEntity();
		char c1 = 'A';
		char c2 = 'Z';
		//System.out.println((int)c1 + " " + (int)c2);
		//Pattern p1 = Pattern.compile(".*entity name=\"(\\D+)\".*entity. is married to .entity name=\"(\\D+)\"..*");//<entity name=(\\w+)>.*");//.*</entity> is married to <entity name=") ;  //"\\(.*\\)"
		//String texte = "his career <entity name=\"http://en.wikipedia.org/wiki/Didier_Pironi\">he</entity> is married to <entity name=\"http://en.wikipedia.org/wiki/Marie\">Marie</entity>";
		//String texte = "youhouhttp://.\"bob\"<>lol";
		//texte = texte.replace('>', ')');
		/*System.out.println(texte);
		Matcher m = p1.matcher(texte) ;  
		System.out.println(m.groupCount());
		if (m.matches())
		{
		   System.out.println ("Premier nombre : " + m.group (1));
		   System.out.println ("Deuxième nombre : " + m.group (2));
		}
		else{
			System.out.println("FAIL");
		}*/
		
		         
		/*StringBuffer sb =  new StringBuffer() ;  
		 int i =  1 ;  
		 while (m.find()) {  
		    m.appendReplacement(sb,  "[" + i++ +  "]") ;  
		}  
		m.appendTail(sb) ;*/
		/*String [] bateaux = p1.split(texte) ;  
		 for (int i =  0 ; i < bateaux.length ; i++) {  
		    System.out.println("[" + i +  "] = " + bateaux[i]) ;
		 }
		try{
			   Pattern p = Pattern .compile(">*</entity> is married to <entity name=");
			   String entree = "his career <entity name=\"http://en.wikipedia.org/wiki/Didier_Pironi\">he</entity> is married to <entity name=\"http://en.wikipedia.org/wiki/Marie\">Marie</entity>";
			   Matcher m = p.matcher(entree);
			   while (m.find())
			      System.out.println(entree.substring(m.start(), m.end()));
			}catch(PatternSyntaxException pse){
			}*/
	}

	
}
