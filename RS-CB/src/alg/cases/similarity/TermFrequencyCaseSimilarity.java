/**
 * A class to compute the case similarity between objects of type MovieCase
 * Uses the overlap feature similarity metric
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.cases.similarity;

import java.util.ArrayList;
import java.util.HashMap;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.PopularityCase;
import alg.cases.TermCase;
import alg.feature.similarity.FeatureSimilarity;
import util.reader.DatasetReader;

public class TermFrequencyCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	final static double DOCUMENT_WEIGHT = 1; // the weight for feature review documents
	
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public TermFrequencyCaseSimilarity()
	{}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2) 
	{
		HashMap<String, HashMap<Integer,Double>> termFrequency = DatasetReader.getTermFrequencyCases();
		
		TermCase m1 = (TermCase)c1;
		TermCase m2 = (TermCase)c2;
		
		double top = 0.0;
		double m1Low = 0.0;
		double m2Low = 0.0;
		double tfIdfSimilarity = 0.0;
		
		ArrayList<Double> m1LowList = new ArrayList<Double>();
		ArrayList<Double> m2LowList = new ArrayList<Double>();
		
		for(String term: termFrequency.keySet())
		{
			double m1Term = termFrequency.get(term).get(m1.getId());
			double m2Term = termFrequency.get(term).get(m2.getId());
			
			double multiplyTerm = m1Term * m2Term;
			
			top += multiplyTerm;
			m1LowList.add(m1Term);
			m2LowList.add(m2Term);
		}
		
		for(double frequency: m1LowList)
		{
			double squarem1Low = frequency * frequency;
			m1Low += squarem1Low;
		}
		
		for(double frequency: m2LowList)
		{
			double squarem2Low = frequency * frequency;
			m2Low += squarem2Low;
		}
		
		tfIdfSimilarity = top/(Math.sqrt(m1Low) * Math.sqrt(m2Low));
		
		
		double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors()) +
				tfIdfSimilarity;
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + DOCUMENT_WEIGHT;
		
		return (below > 0) ? above / below : 0;
	}
}
