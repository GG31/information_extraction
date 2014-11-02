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
    	PATTERNS.put("marriedTo", new ArrayList<String>(){{ add("is married to"); add("is wife of"); add("is the husband of"); add("has been married to");}} );
    }
    
    public static void extractPatterns(String line) {
    	for(String pattern:PATTERNS.keySet()) {
    		for(int i=0; i<PATTERNS.get(pattern).size(); i++){
    			checkPattern(line, pattern, PATTERNS.get(pattern).get(i));
    		}
    	}
    }
    
    
    private static void checkPattern(String line, String patternKey, String patternPossible) {
    	Pattern p1 = Pattern.compile(REGULAR_EXPRESSION1 + patternPossible + REGULAR_EXPRESSION2);
    	Matcher m = p1.matcher(line);
    	if (m.matches())
		{
    		ParserEntity.triplets.add(new Triplet(m.group(1), patternKey, m.group(2)));
		}
    }
    

}
