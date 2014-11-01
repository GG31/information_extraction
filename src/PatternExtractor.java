import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternExtractor {
	private static final String REGULAR_EXPRESSION1 = ".*entity name=\"(\\D+)\".*entity. ";//is married to .entity name=\"(\\D+)\"..*";
	private static final String REGULAR_EXPRESSION2 = " .entity name=\"(\\D+)\"..*";
	
	private static final HashMap<String, ArrayList<String>> PATTERNS;
    static
    {
    	PATTERNS = new HashMap<String, ArrayList<String>>();
    	PATTERNS.put("marriedTo", new ArrayList<String>(){{ add("is married to"); add("is wife of"); }} );
    }
    
    public static void extractPatterns(String line) {
    	for(String pattern:PATTERNS.keySet()) {
    		for(int i=0; i<PATTERNS.get(pattern).size(); i++){
    			checkPattern(line, pattern, PATTERNS.get(pattern).get(i));
    		}
    	}
    }
    
    private static ArrayList<Triplet> triplet = new ArrayList<>();
    
    private static void checkPattern(String line, String patternKey, String patternPossible) {
    	System.out.println("TESTING " + REGULAR_EXPRESSION1 + patternPossible + REGULAR_EXPRESSION2);
    	System.out.println("ON LINE " + line);
    	Pattern p1 = Pattern.compile(REGULAR_EXPRESSION1 + patternPossible + REGULAR_EXPRESSION2);
    	Matcher m = p1.matcher(line);
    	System.out.println("nb group " + m.groupCount());
    	if (m.matches())
		{
		   System.out.println ("Premier nombre : " + m.group (1));
		   System.out.println ("Deuxième nombre : " + m.group (2));
		   //entity.addTriplet(patternKey, m.group(2));
		   triplet.add(new Triplet(m.group(1), patternKey, m.group(2)));
		}
		else{
			System.out.println("FAIL");
		}
    }
    
    public static ArrayList<Triplet> getTriplet() {
    	return triplet;
    }

}
