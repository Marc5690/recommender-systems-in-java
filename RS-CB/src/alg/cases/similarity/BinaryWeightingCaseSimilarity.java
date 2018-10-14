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

public class BinaryWeightingCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	final static double DOCUMENT_WEIGHT = 1; // the weight for feature review documents
	
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public BinaryWeightingCaseSimilarity()
	{}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2) 
	{
		HashMap<String, HashMap<Integer,Double>> binaryTermWeightings = DatasetReader.getBinaryCases();
		
		TermCase m1 = (TermCase)c1;
		TermCase m2 = (TermCase)c2;
		Integer m1Count = 0;
		Integer m2Count = 0;
		double binarySimilarity = 0.0;
		
		for(String term: binaryTermWeightings.keySet())
		{
			Double m1binary = binaryTermWeightings.get(term).get(m1.getId());
			Double m2binary = binaryTermWeightings.get(term).get(m2.getId());
			
			m1Count += m1binary.intValue();
			m2Count += m2binary.intValue();
		}
		
		Integer max = Math.max(m1Count, m2Count);
		Integer min = Math.min(m1Count, m2Count);
		if(min != 0 && max != 0)
		{
			binarySimilarity = min/max;
		}
		
		double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors()) +
				binarySimilarity;
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + DOCUMENT_WEIGHT;
		
		return (below > 0) ? above / below : 0;
	}
}
