/**
 * A class containing various feature similarity metric implementations
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.feature.similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FeatureSimilarity 
{
	/**
	 * computes exact match similarity between string feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between string feature values
	 */
	public static double exact(final String s1, final String s2)
	{
		if(s1.trim().compareToIgnoreCase(s2.trim()) == 0) return 1.0;
		else return 0.0;
	}
	
	/**
	 * computes overlap similarity between set feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 */
	public static double overlap(final Set<String> s1, final Set<String> s2) 
	{
		int intersection = 0;
		
		for(String str: s1)
			if(s2.contains(str))
				intersection++;
		
		int min = (s1.size() < s2.size()) ? s1.size() : s2.size();		
		return (min > 0) ? intersection * 1.0 / min : 0;
	}
	
	/**
	 * computes Jaccard similarity between set feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 */	
	public static double Jaccard(final Set<String> s1, final Set<String> s2) 
	{
		int intersection = 0;
		
		for(String str: s1)
			if(s2.contains(str))
				intersection++;
		
		int union = s1.size() + s2.size() - intersection;		
		return (union > 0) ? intersection * 1.0 / union : 0;
	}
	
	/**
	 * computes Symmetrical similarity between set rating values.
	 * This method attempts to find as many similar ratings as possible between sets, then returns
	 * a value based on this score.
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 */	
	public static double Symmetry(final List<Double> s1, final List<Double> s2) 
	{
		Integer matches = 0;
		final int size1 = s1.size();
		final int size2 = s2.size();
		
		for(Double rate: s1){
			//If the loop finds the exact same rating in the second list of ratings,
			//we infer similarity from this discovery and remove the similarity.
			//The reason for removal is so that we don't repeatedly increment on the same ratings
			//over and over again. Once a match has been found, it is removed from the equation.
			if(s2.contains(rate)){
				matches++;
				s2.remove(rate);
			}
		}
		
		int comparison = size1 + size2;
		
		return matches * 1.0 /comparison;
	}
	
	/**
	 * computes Asymmetrical similarity between set popularity values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 * This method can return higher than 1.0 if one case is substantially more popular than another.
	 */	
	public static double Asymmetry(final Integer s1, final Integer s2) 
	{	
		//This method follows the functionality as described on slide 39 (Asymmetrical functions)
		//in the second set of lecture notes (lecture 3-4).
		Integer max = s1 + (Math.max(0, (s1 - s2)));
		Integer subtraction = s1 - s2;
		double x = subtraction.doubleValue()/max.doubleValue(); //Get double values, as integers round off.
		double y = 1d - x; //An int follow by the letter "d" simply evaluates to the double value of 
		                   //the integer. Java will not calculate integers correctly when trying to store them
		                   //as double.
		
		return 1d - (subtraction/max);
	}
	
}
