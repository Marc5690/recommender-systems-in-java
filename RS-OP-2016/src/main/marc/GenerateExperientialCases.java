package main.marc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Review;
import util.nlp.Parser;
import util.reader.DatasetReader;

public class GenerateExperientialCases {

	public final static String EOL = System.getProperty("line.separator");
	
	public static void main(String[] args)
	{
		//File IO with sentiment word lexicons
		HashSet<String> positiveWords = new HashSet<String>();
		HashSet<String> negativeWords = new HashSet<String>();
		Features cameraFeatures = new Features();
		cameraFeatures.loadFeatures("Digital Camera Features.txt");
		Features printerFeatures = new Features();
		printerFeatures.loadFeatures("Printer Features.txt");
		SentimentWordList positiveSentiment = new SentimentWordList();
		positiveSentiment.readSentiment("positive-words.txt");
		SentimentWordList negativeSentiment = new SentimentWordList();
		negativeSentiment.readSentiment("negative-words.txt");
		HashMap<String, Integer> patterns = new HashMap<String, Integer>();
		HashMap<String, ProductSentiment> productSentiment = new HashMap<String, ProductSentiment>();
		Parser parser = new Parser(); // create an instance of the Parser class
		
		//Take in all data
		String filename = "Digital Camera.txt"; // set the dataset filename
		//String filename = "Printer.txt"; // set the dataset filename
		DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class
		HashMap<String, ProductSentiment> productSentiments = new HashMap<String, ProductSentiment>();
		ArrayList<Review> reviews = reader.getSampleReviews(); // get all reviews and store in an ArrayList
		
		for(Review review:reviews)
		{			
			String productName = review.getProductName();
			String reviewText = review.getReviewText();	
			String[] sentences = parser.getSentences(reviewText); // get the sentences
			Bigram bigram = new Bigram();
			
			//First run - find patterns
			for(String sentence: sentences)
			{
				
				String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
				boolean[] sentenceBigramIdentification = new boolean[tokens.length];//Flip the boolean positions to true for bigram identification
				
//				String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
//				String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence
				for(int i = 0; i < tokens.length; i++) // Iterate every word, and if it is a feature, find the sentiment word.
				{
					if(i+1 <= tokens.length) //If we reach the end of the line, the token cannot be the first-half of a bigram word
					{
						if(bigram.isBigram(tokens[i], tokens[i] +1))
						{
							sentenceBigramIdentification[i] = true;
							sentenceBigramIdentification[i + 1] = true;
						}
					}
					
					if(cameraFeatures.hasFeature(tokens[i]))
					{
						for(int p = 0; p < tokens.length; p++)
						{
							String pos[] = parser.getPOSTags(tokens);
							
						
							if(positiveSentiment.isSentimentWord(tokens[p]) || negativeSentiment.isSentimentWord(tokens[p]))
							{
								if(p>i)
								{
									String[] patternArray = Arrays.copyOfRange(pos, i, p);
									StringBuilder pattern = new StringBuilder("");
									for(String posTag: patternArray)
									{
										//If the last element in patternArray is the posTag, then the first element must be the feature.
										if(!patternArray[patternArray.length -1].equals(posTag))////Check if works right
										{
											//If it is a valid feature and is not the second half of a bigram...
											if(cameraFeatures.hasFeature(tokens[i].toString()) && sentenceBigramIdentification[i - 1] != true )
											{
												pattern.append("FEATURE -");
											}
											else
											{
												pattern.append(posTag);
											}
									}
									
									if(patterns.containsKey(pattern.toString()))
									{										
										Integer count = patterns.get(pattern.toString());
										patterns.put(pattern.toString(),  count + 1);
									}
									else
									{
										patterns.put(pattern.toString(), 1);
									}
								}}
								else
								{
									String[] patternArray = Arrays.copyOfRange(pos, p, i);
									StringBuilder pattern = new StringBuilder("");
									for(String posTag: patternArray)
									{
										if(!patternArray[patternArray.length -1].equals(posTag))////Check if works right
										{
											pattern.append(posTag + "-");
										}
										else
										{
											pattern.append("FEATURE-");
										}
									}
									
									if(patterns.containsKey(pattern.toString()))
									{										
										Integer count = patterns.get(pattern.toString());
										patterns.put(pattern.toString(),  count + 1);
									}
									else
									{
										patterns.put(pattern.toString(), 1);
									}
								}
//								pos[p]..pos[i]
								 // get the POS tag for each sentence token
//								int distance = Math.abs(p-i);		
							}
						}
					}
				}
			}
		}
		
		//Remove all patterns that occur only once
//		http://stackoverflow.com/questions/2594059/removing-all-items-of-a-given-value-from-a-hashmap
		patterns.values().removeAll(Collections.singleton(1));//5593 -> 211 with 300 reviews
		
		//Match pattern then get sentiment on a feature basis per product
		for(Review review:reviews)
		{	
			String productId = review.getProductId();
			ProductSentiment currentProduct = null;
			
			if(productSentiments.containsKey(productId))
			{
				currentProduct = productSentiments.get(productId);
			}
			else
			{
				currentProduct = new ProductSentiment(productId);
			}
			
			String reviewText = review.getReviewText();
			
			String[] sentences = parser.getSentences(reviewText); // get the sentences
			
			//First run - find patterns again
			for(String sentence: sentences)
			{	
				String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
				String pos[] = parser.getPOSTags(tokens);
				
				StringBuilder sentencePattern = new StringBuilder("");
				for(int i = 0; i < pos.length; i++)// posTag: pos)
				{
					//Get feature index of POS, then take it from token and see if it is in feature							
					if(cameraFeatures.hasFeature(tokens[i].toString()))///////////////////////////////////////////////////////////////
					{
						sentencePattern.append("FEATURE-");
					}
					else
					{
						sentencePattern.append(pos[i] + "-");
					}
				}
				
//				if(sentencePattern.toString().contains("FEATURE"))
//				{
				for(String pattern : patterns.keySet())
				{
					if(sentencePattern.toString().contains(pattern))//////////////Won't work without full dataset? Since single pattern occurences removed
					{
						//WRRRRRRROOOOOOOOOOOOONNNNNNNNNNGGGGGGGGGGGGGGG
						//Need to match patterns to words again, right now we're just using the full string that contains the pattern.
						String sentimentWord = null;
						//Find the sentiment word again. Here, we assume that if the feature is at one end of the pattern, then the
						//sentiment word is at the opposite end.
						
						if(sentencePattern.toString().endsWith("FEATURE"))
						{
							sentimentWord = tokens[0];
						}
						else
						{
							sentimentWord = tokens[tokens.length - 1];
						}
							
						if(positiveSentiment.isSentimentWord(sentimentWord))
						{
							//But, if also negation word nearby (within 4 words), flip ("n't" or "not")
							currentProduct.addPositiveSentiment(sentimentWord);
						}
						else if(negativeSentiment.isSentimentWord(sentimentWord))
						{
							//But, if also negation word nearby (within 4 words), flip ("n't" or "not")
							currentProduct.addNegativeSentiment(sentimentWord);
						}
						else
						{
							currentProduct.addNeutralSentiment(sentimentWord);
						}
					}
				}
//				}
			}
			productSentiments.put(productId, currentProduct);
//				String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
		}

		
	
		// tip - how to replace all <br /> tags with line separators: 
//		System.out.println("\n*** review text with \"<br />\" tags:\n" + reviews.get(2).getReviewText());		
		System.out.println("\n*** review text with \"<br />\" tags replaced by line separators:\n" + 
						reviews.get(2).getReviewText().replaceAll("<br />", EOL));
	
		//Find associated sentiment (Good X, bad Y)
		//Use feature sets to find the feature (aspect ratio, auto focus, etc), 
		//then the sentiment lexicon to find out whether sentiment is positive or negative.
		//Then it's just a matter of counting them up.
		//Actual recommendations are task 2.
	
		//Save every single case as a CSV file (Place in provided printer_cases and 
		//digital_camera_cases folders). Format product-category_product-id.csv
	
		//Refactor to either put code in new classes or extract functionality as methods (This is not functional programming!).
	}
}
