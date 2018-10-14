/**
 * A class to compute the case similarity between objects of type PopularityCase
 * Uses the symmetric feature similarity metric
 * 
 * Marc Laffan
 * 03/02/2016
 */

package alg.cases.similarity;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.PopularityCase;
import alg.feature.similarity.FeatureSimilarity;

public class SymmetricCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	final static double RATING_WEIGHT = 1; // the weight for feature ratings
	final static double POPULARITY_WEIGHT = 1; //The weight for feature popularity
	
	
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public SymmetricCaseSimilarity()
	{}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2) 
	{
		PopularityCase m1 = (PopularityCase)c1;
		PopularityCase m2 = (PopularityCase)c2;
		
		double above = GENRE_WEIGHT * FeatureSimilarity.Jaccard(m1.getGenres(), m2.getGenres()) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.Jaccard(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.Jaccard(m1.getActors(), m2.getActors()) +
				RATING_WEIGHT * FeatureSimilarity.Symmetry(m1.getRating() , m2.getRating()) +
				POPULARITY_WEIGHT * FeatureSimilarity.Asymmetry(m1.getPopularity() , m2.getPopularity());
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + RATING_WEIGHT + POPULARITY_WEIGHT;
		
		return (below > 0) ? above / below : 0;

	}
}
