/**
 * A class to represent a sparse matrix
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Matrix implements Serializable
{
	private Map<Integer,Map<Integer,Double>> matrix; // the data structure used to store case similarities
	
	/**
	 * constructor - creates a new Matrix object
	 */
	public Matrix()
	{
		matrix = new HashMap<Integer,Map<Integer,Double>>();
	}
	
	/**
	 * adds an element  to the matrix
	 * @param rowId - the row id
	 * @param colId - the column id
	 */
	public void addElement(final Integer rowId, final Integer colId, final Double entry)
	{
		Map<Integer,Double> row = (matrix.containsKey(rowId)) ? matrix.get(rowId) : new HashMap<Integer,Double>();
		row.put(colId, entry);
		matrix.put(rowId, row);
	}
	
	/**
	 * @param rowId - the row id
	 * @param colId - the column id
	 * @return the (Double) element corresponding to (rowId, colId) or null if the element is not present in the matrix
	 */
	public Double getElement(final Integer rowId, final Integer colId)
	{
		return (matrix.containsKey(rowId)) ? matrix.get(rowId).get(colId) : null;
	}
	
	/**
	 * @return a string representation of the object
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		for(Integer rowId: matrix.keySet())
			buf.append(rowId + " " + matrix.get(rowId) + "\n");
		return buf.toString();
	}
}
