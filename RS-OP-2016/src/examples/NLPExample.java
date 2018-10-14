/**
 * An example class to demonstrate NLP parsing
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package examples;

import util.nlp.Parser;

public class NLPExample {
	public static void main(String[] args)
	{
		String reviewStr = "The battery life is truly outstanding. The battery power is good. " +
				"The recording button is positioned poorly.";
				
		System.out.println(reviewStr + "\n");
		
		Parser parser = new Parser(); // create an instance of the Parser class
		String[] sentences = parser.getSentences(reviewStr); // get the sentences
		System.out.println("Token\t\tChunk Tag\tPOS Tag");
		/*55 Chunk Tag
		 * ADJP - Adjective Phrase
NP - Noun Phrase
VP - Verb Phrase
		 */
		
		/*56 POS Tag
		 * IN - Preposition or subordinating conjunction
JJ - Adjective
JJR - Adjective, comparative
NN - Noun, singular or mass
NNS - Noun, plural
NNP - Proper noun, singular
NNPS - Proper noun, plural
		 * 
		 */
		for(String sentence: sentences) // iterate over each sentence
		{
			String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
			String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
			String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence
			
			for(int i = 0; i < tokens.length; i++) // print the sentence tokens and corresponding chunk and POS tags
				System.out.println(tokens[i] + "\t\t" + chunks[i] + "\t\t" + pos[i]);
			System.out.println("\n+++++\n");			
		}
	}

}
