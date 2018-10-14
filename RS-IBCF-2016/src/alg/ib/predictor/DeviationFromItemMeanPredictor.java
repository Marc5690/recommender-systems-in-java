/**
 * An class to compute Deviation From Item Mean for a target user on a target item that the user has
 * not yet rated.
 * 
 * Marc Laffan
 * 03/03/2016
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

public class DeviationFromItemMeanPredictor implements Predictor
{
	/**
	 * constructor - creates a new SimpleAveragePredictor object
	 */
	public DeviationFromItemMeanPredictor()
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
		double itemMean;
		
		if(itemProfile == null) 
		{	//This program has been designed to handle null results for this method
			//so if the item is not in the itemProfileMap, then the user has already
			//rated it. So we stop method execution and return null.
			return null;
		}
		else
		{
			itemMean = itemProfile.getMeanValue();
		}

		double similaritiesSum = 0.0;
		double count = 0.0;
		double similaritiesSumRatings = 0.0;
		
		for(Integer item: userProfile.getIds())
		{
			if(neighbourhood.isNeighbour(itemId, item ))
			{
				//If neighbour, find similarity in the simMap, increment counter and 
				//apply rating mean substraction for the above, while leaving the below as it is
				similaritiesSum += simMap.getSimilarity(item, itemId);//Above
				count += 1.0;
				double rating = userProfile.getValue(item) - itemProfileMap.get(item).getMeanValue();//Used for below
				similaritiesSumRatings += (simMap.getSimilarity(item, itemId)*rating);//Below
			}			
		}
		
		double below = similaritiesSum / count;
		double above = similaritiesSumRatings / count;
		
		if(below > 0)
		{
			Double prediction = new Double((below > 0) ?  itemMean + (above/below) : 0);
			return prediction;
		}
		else
		{
			return null;
		}
	}
}