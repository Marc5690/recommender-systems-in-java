package examples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.nlp.Parser;

public class IdentifyBiGrams 	
{
	public static void main(String[] args)
	{
		// Read bigram 
		Map<String,Set<String>> bigramMap = instantiateBiGrams();
		
		// There are three features in the following sentences - "battery life", "battery power" and 
		// "recording button".
		// Important - in these cases, "battery" should not also be considered as a single-noun feature 
		// (duplication) - check for occurrence of bigram features in sentences first, and then for 
		// single-noun features.
		String reviewStr = "The battery life is truly outstanding. The battery power is good. " +
							"The recording button is positioned poorly.";
		
		Parser parser = new Parser(); // create an instance of the Parser class
		String[] sentences = parser.getSentences(reviewStr); // get the sentences
		for(String sentence: sentences) // iterate over each sentence
		{
			System.out.println("\nSENTENCE: " + sentence);
			String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
			String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
			String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence
			
			// Check for the occurrence of bigram features in each sentence
			for(int i = 0; i < tokens.length; i++)
			{
				// The code below is not complete - e.g.should check that POS tags corresponding to the bigram terms follow the patterns 
				// noun-noun or adjective-noun, and all tokens should be converted to lowercase ... 
				//
				// Also need to ensure a given token is not counted twice as a feature - e.g. once as a single-noun feature and again 
				// as a bigram feature. One way to do this is to create a boolean array (size equal to the number of tokens in the sentence) 
				// and set elements to true if the corresponding terms have been already identified as features. Check for occurrence of bigram
				// features first, and then check for occurrence of single-noun features.
				
				if(i < tokens.length - 1 // need -1 because searching for bigram features...
						&& bigramMap.containsKey(tokens[i]) 
						&& bigramMap.get(tokens[i]).contains(tokens[i+1]))

					System.out.println("BIGRAM: " + tokens[i] + " " + tokens[i + 1]);
			}
			
			// Check for the occurrence of bigram features in each sentence
			for(int i = 0; i < chunks.length; i++)
			{	
				if(chunks[i].equals("I-NP") && chunks[i+1].equals("I-NP"))
				{
					System.out.println("Bigram!");
				}
				System.out.println("CHUNKS: " + chunks[i]);
			}
						
						
		}
	}
	
	// A map to store all bigram features. Create a separate map to store all single-noun features
	public static Map<String,Set<String>> instantiateBiGrams()
	{
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();

		// Consider two bigram features - "battery power" and "battery life"
		Set<String> set1 = new HashSet<String>();
		set1.add("life");
		set1.add("power");
		map.put("battery", set1);
		
		// And one more feature - "recording button"
		Set<String> set2 = new HashSet<String>();
		set2.add("button");
		map.put("recording", set2);
		
		return map;
	}
}