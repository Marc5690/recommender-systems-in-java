/**
 * This class is largely similar to the Pearson similarity class, but with several modifications
 * to apply significance weighting
 * 
 * Marc Laffan
 * 02/03/2016
 */

package similarity.metric;

import java.util.Set;

import profile.Profile;

public class PearsonWithSignificanceWeightingMetric implements SimilarityMetric
{
	/**
	 * constructor - creates a new PearsonMetric object
	 */
	public PearsonWithSignificanceWeightingMetric()
	{
	}
		
	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	public double getSimilarity(final Profile p1, final Profile p2)
	{
		double threshold = 50.0; //Added this threshold value, as per the algorithm in the notes
		
        double sum_r1 = 0;
        double sum_r1_sq = 0;
        double sum_r2 = 0;
        double sum_r2_sq = 0;
        double sum_r1_r2 = 0;
                
        Set<Integer> common = p1.getCommonIds(p2);
        Integer n = common.size();// number n, as size of intersection in the algorithm.
        for(Integer id: common)
		{
			double r1 = p1.getValue(id).doubleValue();
			double r2 = p2.getValue(id).doubleValue();

			sum_r1 += r1;
			sum_r1_sq += r1 * r1;
			sum_r2 += r2;
			sum_r2_sq += r2 * r2;
			sum_r1_r2 += r1 * r2;
		}
		
		double above = (common.size() > 0) ? sum_r1_r2 - (sum_r1 * sum_r2) / common.size() : 0;
		double below = (common.size() > 0) ? Math.sqrt( (sum_r1_sq - (sum_r1 * sum_r1) / common.size()) * (sum_r2_sq - (sum_r2 * sum_r2) / common.size()) ) : 0;
		double pearson = above/below ;
		
		if(n < threshold)//If the intersection is greater than the threshold, we apply weighting.
		{
			double significanceWeighting = n/threshold;
			pearson *= significanceWeighting;
		    below *= significanceWeighting;
		}
		
		return (below > 0) ? pearson: 0;
	}
}
