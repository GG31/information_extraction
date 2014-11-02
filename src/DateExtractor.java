import java.util.HashMap;


public class DateExtractor {
	private static final HashMap<String, String> mapMonth;
    static
    {
    	mapMonth = new HashMap<String, String>();
    	mapMonth.put("january", "01");
    	mapMonth.put("february", "02");
    	mapMonth.put("march", "03");
    	mapMonth.put("april", "04");
    	mapMonth.put("may", "05");
    	mapMonth.put("june", "06");
    	mapMonth.put("july", "07");
    	mapMonth.put("august", "08");
    	mapMonth.put("september", "09");
    	mapMonth.put("october", "10");
    	mapMonth.put("november", "11");
    	mapMonth.put("december", "12");
    }
    
    
	
	public static void dateExtractor(Entity entity, String[] words, int index) {
		if(words[index].equals("born") || words[index].equals("(born")) {
			try { 
		        int day = Integer.parseInt(words[index+1]); 
		        String month = words[index+2];
		        int year;
		        if(words[index+3].substring(words[index+3].length()-1).equals(")")){
		        	year = Integer.parseInt(words[index+3].substring(0, words[index+3].length()-1)); 
		        }else
		        	year= Integer.parseInt(words[index+3]); 
		        ParserEntity.triplets.add(new Triplet(entity.getUri(),"hasDate", formalizeDate(day, month,year)));        
		    } catch(NumberFormatException e) { 
		    	try{
		    		int year= Integer.parseInt(words[index+1]);
		    		ParserEntity.triplets.add(new Triplet(entity.getUri(),"hasDate", year + "")); 
		    	} catch(NumberFormatException e1){
		    		
		    	}
		    }
		} else{
			try { 
				if (words[index].charAt(0) == '('){
					int day = Integer.parseInt(words[index].substring(1));
					String month = words[index + 1];
					int year;
		        if(words[index+2].substring(words[index+2].length()-1).equals(")")){
		        	year = Integer.parseInt(words[index+2].substring(0, words[index+2].length()-1)); 
		        }else
		        	year= Integer.parseInt(words[index+2]); 
		        	ParserEntity.triplets.add(new Triplet(entity.getUri(),"hasDate",formalizeDate(day, month,year)));  
				}
		    } catch(NumberFormatException e) { 
		    }catch(ArrayIndexOutOfBoundsException e) {
		    } catch(StringIndexOutOfBoundsException e) {
		    	
		    }
		}
	}
	
	private static String formalizeDate(int day, String month, int year) {
		String dayString = day + "";
		if(day<10) {
			dayString = "0" + day;
		}
		String s = year + "-" + mapMonth.get(month.toLowerCase()) + "-" + dayString;
		return s;
	}
}
