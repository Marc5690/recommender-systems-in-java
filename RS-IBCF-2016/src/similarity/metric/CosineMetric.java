/**
 * Compute the Cosine between profiles
 * 
 * Marc Laffan
 * 25/02/2016
 */

package similarity.metric;

import java.util.Set;

import profile.Profile;

public class CosineMetric implements SimilarityMetric
{
	/**
	 * constructor - creates a new PearsonMetric object
	 */
	public CosineMetric()
	{
	}
		
	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	public double getSimilarity(final Profile p1, final Profile p2)
	{
        double sum_r1_r2 = 0;
        double sum_p1_sq = 0;
        double sum_p2_sq = 0;
        Set<Integer> common = p1.getCommonIds(p2); //Get the intersection for the above.
        Set<Integer> p1Ids = p1.getIds();//Used in below
        Set<Integer> p2Ids = p2.getIds();//Used in below
        
        for(Integer id: p1Ids) //Used in below
     		{
     			double r1 = p1.getValue(id).doubleValue();
     			sum_p1_sq += r1 * r1; //Add the square of the rating to the first profiles sum
     		}
        
        for(Integer id: p2Ids) //Used in below
     		{
      			double r2 = p2.getValue(id).doubleValue();
     			sum_p2_sq += r2 * r2; //Add the square of the rating to the second profiles sum
     		}
        
        for(Integer id: common) //Used in above
		{
			double r1 = p1.getValue(id).doubleValue();
			double r2 = p2.getValue(id).doubleValue();
			sum_r1_r2 += r1 * r2; //Add the multiplication of all intersections to the combined sum
		}
		
		double above = (common.size() > 0) ? sum_r1_r2 : 0;
		double below = (common.size() > 0) ? Math.sqrt(sum_p1_sq * sum_p2_sq) : 0; 
		return (below > 0) ? above/below : 0;
	}
}
