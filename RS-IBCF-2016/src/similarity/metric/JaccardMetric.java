/**
 * Compute the Jaccard similarity between profiles
 * 
 * Marc Laffan
 * 25/02/2016
 */

package similarity.metric;

import java.util.HashSet;
import java.util.Set;

import profile.Profile;

public class JaccardMetric implements SimilarityMetric
{
	/**
	 * constructor - creates a new PearsonMetric object
	 */
	public JaccardMetric()
	{
	}
		
	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	public double getSimilarity(final Profile p1, final Profile p2)
	{
		Integer threshold = 50;//Threshold for Jaccard similarity metric
		//Get the union used to perform jaccard weighting, and add the second profiles IDs.
		//The set used for this will guarantee no duplicates.
		Set<Integer> unionIds = new HashSet<Integer>(p1.getIds());
		unionIds.addAll(p2.getIds());
		
        double sum_r1 = 0;
        double sum_r1_sq = 0;
        double sum_r2 = 0;
        double sum_r2_sq = 0;
        double sum_r1_r2 = 0;
                
        Set<Integer> common = p1.getCommonIds(p2);
        for(Integer id: common) //Regular weighting, significance weighting applied later on if applicable.
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
		
		double jaccard = above/below;
		
		if(common.size() < threshold) //Perform significance weighting if threshold is breached.
        {
        	jaccard *= (common.size()/unionIds.size());
        }
		
		return (below > 0) ? jaccard: 0;
	}
}
