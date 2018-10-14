package main.marc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class SentimentWordList {

	private HashSet<String> listOfSentimentWords;	

	public SentimentWordList()
	{
		listOfSentimentWords = new HashSet<String>();
	}
	
	public SentimentWordList(HashSet<String> listOfFeaturesIn)
	{
		listOfSentimentWords = listOfFeaturesIn;
	}
	
	public void addSentiment(String sentiment)
	{
		listOfSentimentWords.add(sentiment);
	}
	
	public HashSet<String> getSentimentWords()
	{
		return listOfSentimentWords;
	}
	
	public boolean isSentimentWord(String sentimentWord)
	{
		if(listOfSentimentWords.contains(sentimentWord))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public void readSentiment(String fileName)
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("sentiment lexicon" + File.separator + fileName)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				if(!line.contains(";"))
				{
					listOfSentimentWords.add(line);
				}
			}
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
