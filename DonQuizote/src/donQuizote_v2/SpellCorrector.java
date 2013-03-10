package donQuizote_v2;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class SpellCorrector {
	
	public static String[] correct(String[] qAndAs){
		
		String[] questionAndAnswers = new String[qAndAs.length];
		
		SpellChecker checker = new SpellChecker();
		
		// Correct the question
		
	    // Ask google for some suggestions
		for (int i=0; i<qAndAs.length; i++){
		    SpellResponse spellResponse = checker.check( qAndAs[i] );
			try {
				String replacement = qAndAs[i];
		    	for( SpellCorrection sc : spellResponse.getCorrections() )
		    		{
		    		// Process each answer
		   	    		if (sc.getConfidence() > 0) {
		   	    			replacement = replace(replacement, qAndAs[i], sc);
		   	    		}
			       }
		    	
		    	
		    	// Update the question
		    	System.out.println("SPELL: Replacing '"+qAndAs[i]+"' with '"+replacement+"'");
		    	questionAndAnswers[i] = replacement;
		    			
			} 
			catch(Exception e)
			{	
				questionAndAnswers[i] = qAndAs[i];
			}
	
		}
		
    	return questionAndAnswers;
 
	
	}
	
	public static String[] stripQuestionWords(String[] strings){
		

		String[] correctedStringArray = new String[strings.length];
		

	    // Ask google for some suggestions
		for (int i=0; i<strings.length; i++){
		   
			String replacement = strings[i];
			
			// Strip question words and phrases
	    	String[] questionWords = {"which of the following", "which", "what", "where", "how many"};
	    	for (String qW : questionWords) {
	    		replacement = replacement.replaceFirst(qW, "");
	    	}
	    	
	    	correctedStringArray[i] = replacement;
		}
			
		return correctedStringArray;
	
	}
	
	//TODO: fix this for multiwords please
	public static  String replace(String string, String original, SpellCorrection corr){
		
		
		//System.out.println("DEBUG " + string.substring(corr.getOffset(), corr.getOffset()+corr.getLength()) + corr.getWords()[0]);
		
		String replacement = "";
		int i = 0;
		
		while (replacement.length() < corr.getLength() - 1)
		{
			//System.out.println(corr.getLength());
			//System.out.println(replacement.length()); 
			
			if (i > 0) replacement += " ";
			replacement += corr.getWords()[i];
			
			i++;
		}
		System.out.println("#SC " + replacement);
		String newstring = string.replaceFirst(original.substring(corr.getOffset(), corr.getOffset()+corr.getLength()), replacement);
		//System.out.println(newstring+string+" #SpellCorrector 2" );
		return newstring;
	}

	
	public static void main(String[] args){
		
		
		args = new String[]{"Ehot pune"};
		SpellCorrector.correct(args);
		
		
		//String[] test = new String[]{"test1'\\23| 123 a1b1"};
		//test = SpellCorrector.correctChars(test);

		
	}
	
	public static String[] correctChars(String[] strings) {
		
		
		int i = 0;
		for (String s : strings){
			
			
			// Get rid of all the obvious bits
			s = s.replace('/', 'l');
			s = s.replace('\\', 'l');
			s = s.replace('|', 'l');
			//s = s.replace('\'','');
			

			
			StringBuilder newString = new StringBuilder(s);
			
			// Go down the string. if we find a number right next to a letter, it's probably wrong.
			for (int j = 0; j < s.length(); j++){
				
				Boolean letterToTheLeft = true;
				if (j < 1 || !Character.isAlphabetic(s.charAt(j-1))) { letterToTheLeft = false; }
				Boolean letterToTheRight = true;
				if (j + 1 > s.length()-1 || !Character.isAlphabetic(s.charAt(j+1))) { letterToTheRight = false; }
				
				
				if (letterToTheLeft || letterToTheRight) {
					
					// Replace that with the right stuff, 1 for l etc.
					if (s.charAt(j) == '1') { newString.setCharAt(j, 'l'); }
					if (s.charAt(j) == '0') { newString.setCharAt(j, 'o'); }
					if (s.charAt(j) == '7') { newString.setCharAt(j, '?'); }
				}
			}
			
			strings[i] = newString.toString();
			i++;
			
		}
		return strings;
	}

}
