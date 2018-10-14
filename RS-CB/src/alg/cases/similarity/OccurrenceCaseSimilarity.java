/**
 * A class to compute the case similarity between objects of type MovieCase
 * Uses the overlap feature similarity metric
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.cases.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.PopularityCase;
import alg.cases.TermCase;
import alg.feature.similarity.FeatureSimilarity;
import util.reader.DatasetReader;

public class OccurrenceCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	final static double CONFIDENCE_WEIGHT = 1; //the weight for genre co-occurrence probability
	
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public OccurrenceCaseSimilarity()
	{}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2) 
	{
		TermCase m1 = (TermCase)c1;
		TermCase m2 = (TermCase)c2;
		HashMap<String, ArrayList<String>> occurrences = DatasetReader.getOccurrences();
		HashMap<String, Integer> occurrenceTransactions = DatasetReader.getOccurrenceTransactions();
		Set<String> genres = m1.getGenres();
		Set<String> caseGenres = m2.getGenres();
		ArrayList<Double> allConfidence = new ArrayList<Double>();
		
		Integer[] allOccurrences = Arrays.copyOf(occurrenceTransactions.values().toArray() , occurrenceTransactions.values().size(), Integer[].class);
		Integer allOccurrencesSize = 0;
			
		for(Integer transactionOccurencesNumber: allOccurrences) //Basically "concatenate" the number of occurrences into a single number.
		{
			allOccurrencesSize += transactionOccurencesNumber;
		}
		
		for(String genre: genres)
		{
			ArrayList<String> pairs = occurrences.get(genre);//Get every occurrence of another genre that was ever paired with this genre.
			Integer totalGenreOccurences = occurrenceTransactions.get(genre);//Get the total number of times this genre appeared.
			double support = (totalGenreOccurences.doubleValue()/allOccurrencesSize.doubleValue()) * 100; //Percentage of transaction containing m1 genre
			Integer caseGenreOccurenceCount = 0;
			Integer totalOccurences = DatasetReader.getTotalOccurrences();
			
			for(String caseGenre: caseGenres) //Here, we are basically calculating as per the algorithm in the lecture slides.
			{
				for(String occurence: pairs)
				{
					if(occurence.equals(caseGenre))
					{
						caseGenreOccurenceCount += 1;
					}
				}
				double supportBoth = ((totalGenreOccurences.doubleValue() + caseGenreOccurenceCount.doubleValue())/allOccurrencesSize.doubleValue()) * 100;
				double supportNotBoth = totalOccurences - supportBoth;
				double supportNot = totalOccurences - support;
				
				double genreConfidence = (supportBoth/support)/(supportNotBoth/supportNot);
				allConfidence.add(genreConfidence);
			}
			
			
		}
		
		double maxConfidence = 0.0;//This holds the total of all calculations, which is then divided by their number in the confidence variable.
		
		for(Double conf : allConfidence)
		{
			maxConfidence += conf;
		}
		Double confidence = maxConfidence/allConfidence.size(); //This is the actual value that holds the similarity between cases.
		
		double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors()) + 
				confidence;
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + CONFIDENCE_WEIGHT;
		
		return (below > 0) ? above / below : 0;
	}
}
