/**
 * A class to perform NLP operations
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package util.nlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;


public class Parser 
{
	private SentenceDetectorME sentenceDetector; // sentence detector model
	private Tokenizer tokenizer; // tokenizer model
	private POSTaggerME tagger; // POS tagger model
	private ChunkerME chunker; // chunker model

	public static void main(String[] args)
	{
		String str = "It provides amazing picture quality and is perfect for amateur photographers. " +
				"The battery life is truly outstanding, I am not even going to buy a spare battery. " +
				"It's weight is heavier than the D60, but that's not a problem. " +
				"The stereo sound and autofocus are limited, however. " +
				"The viewfinder is also really poor.";
		System.out.println(str + "\n");
		
		Parser parser = new Parser(); // create an instance of the Parser class
		String[] sentences = parser.getSentences(str); // get the sentences
		System.out.println("Token\t\tChunk Tag\tPOS Tag");
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

	/**
	 * default constructor - creates a new Parser object
	 */
	public Parser()
	{
		getSentenceDetector();
		getTokenizer();
		getPOSTagger();
		getChunker();
	}

	/**
	 * returns the sentences in a text
	 * @param str - the input text
	 * @return a String array containing the sentences in the input text (str)
	 */
	public String[] getSentences(final String str)
	{
		return sentenceDetector.sentDetect(str);
	}

	/**
	 * returns the tokens (words) in a sentence
	 * @param str - the input sentence
	 * @return a String array containing the sentence tokens (words)
	 */
	public String[] getSentenceTokens(final String str)
	{
		return tokenizer.tokenize(str);
	}

	/**
	 * returns the POS tags for each sentence token (word)
	 * @param tokens - a String array containing sentence tokens (words)
	 * @return a String array containing POS tags for the sentence tokens (words)
	 */
	public String[] getPOSTags(final String[] tokens)
	{
		return tagger.tag(tokens);
	}
	
	/**
	 * returns the chunk tags for each sentence token (word)
	 * @param tokens - a String array containing sentence tokens (words)
	 * @param pos - a String array containing POS tags for the sentence tokens (words)
	 * @return a String array containing chunk tokens for the sentence
	 */
	public String[] getChunkTags(final String[] tokens, final String[] pos)
	{
		return chunker.chunk(tokens, pos);
	}
	
	/**
	 * get the sentence detector model
	 */
	private void getSentenceDetector()
	{
		try {
			InputStream modelIn = new FileInputStream("OpenNLP_1.5.0_Models/en-sent.bin");
			SentenceModel model = new SentenceModel(modelIn);
			sentenceDetector = new SentenceDetectorME(model);
			modelIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the tokenizer model
	 */
	private void getTokenizer()
	{

		try {
			InputStream modelIn = new FileInputStream("OpenNLP_1.5.0_Models/en-token.bin");
			TokenizerModel model = new TokenizerModel(modelIn);
			tokenizer = new TokenizerME(model);
			modelIn.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the POS model
	 */
	private void getPOSTagger()
	{
		try {
			InputStream modelIn = new FileInputStream("OpenNLP_1.5.0_Models/en-pos-maxent.bin");
			POSModel model = new POSModel(modelIn);
			tagger = new POSTaggerME(model);
			modelIn.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get the chunker model
	 */
	private void getChunker()
	{
		try {
			InputStream modelIn = new FileInputStream("OpenNLP_1.5.0_Models/en-chunker.bin");
			ChunkerModel model = new ChunkerModel(modelIn);
			chunker = new ChunkerME(model);
			modelIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
