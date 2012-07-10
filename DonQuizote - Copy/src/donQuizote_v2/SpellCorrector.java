package donQuizote_v2;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class SpellCorrector {
	
	public static String[] correct(String[] qAndAs){
		
		String[] questionAndAnswers = new String[qAndAs.length];
		
		SpellChecker checker = new SpellChecker();
		
		// Correct the question, stripping question words
		
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
    	// Strip question words and phrases
    	String[] questionWords = {"which of the following", "which", "what", "where", "how many"};
    	for (String qW : questionWords) {
    		replacement =    replacement.replaceFirst(qW, "");
    	}
    	// Update the question
    	questionAndAnswers[i] = replacement;
    			
		} catch(Exception e){}
		questionAndAnswers[i] = qAndAs[i];
		}
		
    	return questionAndAnswers;
 
	
	}
	
	
	public static  String replace(String string, String original, SpellCorrection corr){
		//System.out.println(string+" becomes #SpellCorrector 1" );
		//System.out.println("DEBUG " + string.substring(corr.getOffset(), corr.getOffset()+corr.getLength()) + corr.getWords()[0]);
		String newstring = string.replaceFirst(original.substring(corr.getOffset(), corr.getOffset()+corr.getLength()),corr.getWords()[0]);
		//System.out.println(newstring+string+" #SpellCorrector 2" );
		return newstring;
	}

}
