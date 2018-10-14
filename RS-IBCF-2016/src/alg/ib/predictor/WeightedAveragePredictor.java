/**
 * An class to compute the target user's predicted rating for the target item (item-based CF) using the simple average technique.
 * 
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib.predictor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;
import similarity.metric.*;

public class WeightedAveragePredictor implements Predictor
{
	/**
	 * constructor - creates a new SimpleAveragePredictor object
	 */
	public WeightedAveragePredictor()
	{
	}
	
	/**
	 * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
	 * @param userId - the numeric ID of the target user
	 * @param itemId - the numerid ID of the target item
	 * @param userProfileMap - a map containing user profiles
	 * @param itemProfileMap - a map containing item profiles
	 * @param neighbourhood - a Neighbourhood object
	 * @param simMap - a SimilarityMap object containing item-item similarities
	 */
	public Double getPrediction(final Integer userId, final Integer itemId, final Map<Integer,Profile> userProfileMap, final Map<Integer,Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap)	
	{
		Profile itemProfile = itemProfileMap.get(itemId); //Target Item
		Profile userProfile = userProfileMap.get(userId); //Target User
		
		if(itemProfile == null) 
		{	//This program has been designed to handle null results for this method
			//so if the item is not in the itemProfileMap, then the user has already
			//rated it. So we stop method execution and return null.				
			return null;
		}

		double similaritiesSum = 0.0;
		double count = 0.0;
		double similaritiesSumRatings = 0.0;
		
		for(Integer item: userProfile.getIds())
		{
			if(neighbourhood.isNeighbour(itemId, item ))
			{
				//If neighbour, find similarity in the simMap, increment counter and 
				//apply rating similarity multiplication by rating, while leaving the below as it is
				similaritiesSum += simMap.getSimilarity(item, itemId);
				count += 1.0;
				
				double rating = userProfile.getValue(item);
				similaritiesSumRatings += (simMap.getSimilarity(item, itemId)*rating);
			}
		}

		double below = similaritiesSum / count;
		double above = similaritiesSumRatings / count;
		
		if(below > 0)
		{
			Double prediction = new Double((below > 0) ?  (above/below) : 0);
			return prediction;
		}
		else
		{
			return null;
		}
	}
}

