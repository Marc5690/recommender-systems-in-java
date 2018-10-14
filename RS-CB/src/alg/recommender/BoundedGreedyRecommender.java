/**
 * A class to define a case-based recommender.
 * The scoring function used to rank recommendation candidates is the bounded greedy similarity to the target user's profile cases.
 * 
 * Marc Laffan
 * 13/02/2016
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
import alg.casebase.Casebase;
import alg.cases.MovieCase;
import alg.cases.similarity.CaseSimilarity;
import alg.feature.similarity.FeatureSimilarity;

public class BoundedGreedyRecommender extends Recommender
{
	/**
	 * constructor - creates a new MaxRecommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public BoundedGreedyRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
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
		double boundPercent = 1.0;//Top percent of recommendations to return. 1.0 is 100%, 0.1 is 10%
		Casebase readerBase = reader.getCasebase();
		ArrayList<Integer> recommendations = new ArrayList<Integer>();
		int bound = (int) (readerBase.size() * boundPercent);//Our bound should be "boundPercent" of the candidate recommendations size
		Set<Integer> userMovies = reader.getUserProfile(userId).keySet();
		
		//Arrange recommendations with the getUnfilteredRecommendations() method
		ArrayList<Integer> unfilteredRecommendations = this.getUnfilteredRecommendations(userId, reader);
		
		//For every movie, find the most similar to what the user has already seen.
		//This is the first part of the actual Bounded Greedy algorithm (Find first most similar)
		//Please note that it is absolutely possible to get a perfect 1.0 for the initial score here,
		//thanks to movies such as "The Matrix" and "The Matrix: Revolutions" having the exact same features.
		Integer maxSimilar = 0;
		Double initialScore = 0.0;
		
		for(Iterator<Integer> movieId = unfilteredRecommendations.iterator(); movieId.hasNext();)
		{
			Integer currentMovie = movieId.next();
			
			if(recommendations.size() < 1 && !userMovies.contains(movieId) && initialScore != 1.0)//Ensure we only take one movie that the user hasn't seen
			{
				for(Integer userMovie: userMovies)
				{
					Double sim = super.getCaseSimilarity(currentMovie,userMovie);
					if(sim != null && sim>initialScore)
					{
						initialScore = sim;
						maxSimilar = currentMovie;
					}
				}
				movieId.remove();
			}	
		}
		
		//Add our initial most likely candidate.
		recommendations.add(maxSimilar);
		
		//As we cannot concurrently modify the recommendations list, we use the "addToRecommendations" list to hold a number of 
		//recommendations that will be later added to the recommendations list.
		ArrayList<Integer> addToRecommendations = new ArrayList<Integer>();
		
		//As long as it is one lower than the bound, as the previous loop has already given up an initial recommendation
		for(Integer i = 0;i<bound-1;i++)
		{
			Integer maxDisimilarMovie = 0;//The least similar movie to what the recommendation list already holds.
			double score = 1.0;//When calculating dis-similarity, we start with the highest similarity and work backwards.
			
			//Go through all current recommendations.  PLease note that recommendations will be added as the loop progresses.
			for(Iterator<Integer> movieId = recommendations.iterator(); movieId.hasNext();)
			{
				Integer currentMovie = movieId.next();
			
				//Run through all candidates from the getUnfilteredRecommendations() method
				for(Iterator<Integer> candidateMovie = unfilteredRecommendations.iterator(); candidateMovie.hasNext();)
				{
					Integer movie = candidateMovie.next();
					Double sim = super.getCaseSimilarity(movie, currentMovie);
					
					//We are essentially doing the opposite of the maxSimilar loop. Finding the least similar. 
					if(sim != null && sim<score)
					{
						score = sim;
						maxDisimilarMovie = movie;
					}
					
					//Done iterating? Remove the most dissimilar candidate so that it does not appear in the next iteration.
					if(candidateMovie.hasNext() == false)
					{
						addToRecommendations.add(maxDisimilarMovie);
					}
				}
				unfilteredRecommendations.remove(Integer.valueOf(maxDisimilarMovie));//TODO: Test this to ensure it works as intended.
			}		
		}

		recommendations.addAll(addToRecommendations);

		return recommendations;
	}
	
	//Extracted original getRecommendations() method from MaxRecommender for use in this classes getRecommendations() method	
	public ArrayList<Integer> getUnfilteredRecommendations(final Integer userId, final DatasetReader reader)
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
					//Please note that the target and candidate have been switched here.
					Double sim = super.getCaseSimilarity(id, candidateId);
					if(sim != null && sim>score) score = sim;
				}				

				if(score > 0)
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
