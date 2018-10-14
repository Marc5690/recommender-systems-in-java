/**
 * An interface to compute the similarity between cases
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.cases.similarity;

import alg.cases.Case;

public interface CaseSimilarity 
{
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2);
}
