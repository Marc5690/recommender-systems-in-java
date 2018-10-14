/**
 * A class to define a personalised recommender.
 * The scoring function used to rank recommendation candidates is the mean similarity to the target user's profile cases, with personalised weightings for the genre and director.
 * 
 * Marc Laffan
 * 13/02/2015
 */

package alg.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import util.Matrix;
import util.ScoredThingDsc;
import util.reader.DatasetReader;
import alg.cases.MovieCase;
import alg.cases.similarity.CaseSimilarity;
import alg.feature.similarity.FeatureSimilarity;

public class PersonalisedRecommender extends Recommender
{
	/**
	 * constructor - creates a new PersonalisedRecommender object
	 * @param caseSimilarity - an object to compute case similarity. This object is not actually used, but this design
	 * is consistent with other recommender constructor contracts.
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public PersonalisedRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		super(reader);
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
					double genreWeight = reader.getUserGenreWeighting(userId); //Get the current users personal genre weighting
					double directorWeight = reader.getUserDirectorWeighting(userId); //Get the current users personal director weighting
					double actorWeight = reader.getUserActorWeighting(); //This is simply the value 1.0. For the sake of Object-Orientation, this method was provided.
		
					//Parse the comparison movies:
					MovieCase m1 = (MovieCase) reader.getCasebase().getCase(candidateId);
					MovieCase m2 = (MovieCase) reader.getCasebase().getCase(id);
		
					//Add the personalised weightings:
					double above = genreWeight * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) + 
							directorWeight * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
							actorWeight * FeatureSimilarity.overlap(m1.getActors(), m2.getActors());
		
					double below = genreWeight + directorWeight + actorWeight;
		
					score =  (below > 0) ? above / below : 0;
				}		
				
				if(score > 0)
					ss.add(new ScoredThingDsc(score / profile.size(), candidateId)); // add the score for the current recommendation candidate case to the set
			}
		}

		// sort the candidate recommendation cases by score (in descending order) and return as recommendations 
		ArrayList<Integer> recommendationIds = new ArrayList<Integer>();
		
		for(Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext();)
		{
			ScoredThingDsc st = it.next();
			recommendationIds.add((Integer)st.thing);
		}
		
		return recommendationIds;
	}
}
