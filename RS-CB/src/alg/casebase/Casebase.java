/**
 * This class stores case objects
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.casebase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import alg.cases.Case;

public class Casebase 
{
	private Map<Integer,Case> cb; // stores case ids and objects
	
	/**
	 * constructor - creates a new Casebase object
	 */
	public Casebase()
	{
		cb = new HashMap<Integer,Case>();
	}
	
	/**
	 * adds a case to the casebase
	 * @param id - the case id
	 * @param c - the case object
	 */
	public void addCase(final Integer id, final Case c)
	{
		cb.put(id, c);
	}
	
	/**
	 * @returns all case ids
	 */
	public Set<Integer> getIds()
	{
		return cb.keySet();
	}

	/**
	 * @param id - the case id
	 * @returns the case object
	 */
	public Case getCase(final Integer id)
	{
		return cb.get(id);
	}
	
	/**
	 * @returns the number of cases in the casebase
	 */
	public int size()
	{
		return cb.size();
	}
}
