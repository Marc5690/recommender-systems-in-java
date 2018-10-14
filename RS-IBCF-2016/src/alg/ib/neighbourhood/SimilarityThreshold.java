/**
 * A class that implements the "similarity threshold" technique
 * 
 * Marc Laffan
 * 25/02/2016
 */

package alg.ib.neighbourhood;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import similarity.SimilarityMap;
import profile.Profile;
import util.ScoredThingDsc;

public class SimilarityThreshold extends Neighbourhood
{
	private final double l; // the similarity threshold itself, should be a fraction between 0 and 1
	
	/**
	 * constructor - creates a new NearestNeighbourhood object
	 * @param l - the similarity threshold
	 */
	public SimilarityThreshold(final int l)
	{
		super();
		this.l = l; //To ensure that nothing breaks due to mis-use, the program will still run 
					//even if a whole number is used with Similarity Thresholding
	}
	
	//Additional constructor required so we can find l (Rather than k, which is likely in the 100 range, while l is a fraction)
	public SimilarityThreshold(final double l)
	{
		super();
		this.l = l;
	}


	/**
	 * stores the neighbourhoods for all items in member Neighbour.neighbourhoodMap
	 * @param simMap - a map containing item-item similarities
	 */
	public void computeNeighbourhoods(final SimilarityMap simMap)
	{
		for(Integer itemId: simMap.getIds()) // iterate over each item
		{
			SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); // for the current item, store all similarities in order of descending similarity in a sorted set

			Profile profile = simMap.getSimilarities(itemId); // get the item similarity profile
			if(profile != null)
			{
				for(Integer id: profile.getIds()) // iterate over each item in the profile
				{
					double sim = profile.getValue(id);
					if(Math.abs(sim) > 0)
						ss.add(new ScoredThingDsc(sim, id));
				}
			}

			for(Iterator<ScoredThingDsc> iter = ss.iterator(); iter.hasNext();)
			{
				ScoredThingDsc st = iter.next();
				Integer id = (Integer)st.thing;
				if(st.score > l) //If the score is greater than l, then add it.
				{
					this.add(itemId, id);
				}
			}
		}
	}
}
