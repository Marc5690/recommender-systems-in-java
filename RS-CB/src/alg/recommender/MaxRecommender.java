/**
 * A class to define a case-based recommender.
 * The scoring function used to rank recommendation candidates is the max similarity to the target user's profile cases.
 * 
 * Marc Laffan
 * 03/02/2015
 */

package alg.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import util.ScoredThingDsc;
import util.reader.DatasetReader;
import alg.cases.similarity.CaseSimilarity;

public class MaxRecommender extends Recommender
{
	/**
	 * constructor - creates a new MaxRecommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public MaxRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		super(caseSimilarity, reader);
	}
	
	/**
	 * returns a ranked list of recommended case ids
	 * @param userId - the id of the target user
	 * @param reader - an object to store user profile data and movie metadata
	 * @return the ranked list of recommended case ids
	 */
	public ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader)
	{
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
		
		// get the target user profile
		Map<Integer,Double> profile = reader.getUserProfile(userId);

		// get the ids of all recommendation candidate cases
		Set<Integer> candidateIds = reader.getCasebase().getIds();
		
		// compute a score for all recommendation candidate cases
		for(Integer candidateId: candidateIds)
		{
			if(!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
			{
				double score = 0;

				// iterate over all the target user profile cases and compute a score for the current recommendation candidate case
				for(Integer id: profile.keySet()) 
				{
					Double sim = super.getCaseSimilarity(candidateId, id);
					//This line was changed for Task 1.
					//I simply changed a line to update the score when the
					//similarity is greater than the current similarity.
					//Otherwise, do nothing (In other words, do not update the score).
					if(sim != null && sim>score) score = sim;
				}				

				if(score > 0)
					//Removed division by profile size (We are not computing mean for this class, we are computing max).
					//Add the score for the current recommendation candidate case to the set.
					ss.add(new ScoredThingDsc(score, candidateId)); 
			}
		}

		// sort the candidate recommendation cases by score (in descending order) and return as recommendations 
		ArrayList<Integer> recommendationIds = new ArrayList<Integer>();
		
		for(Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext(); )
		{
			ScoredThingDsc st = it.next();
			recommendationIds.add((Integer)st.thing);
		}
		
		return recommendationIds;
	}
}
