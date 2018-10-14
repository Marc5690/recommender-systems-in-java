/**
 * A class to represent a movie case
 * 
 * Marc Laffan
 * 03/02/2015
 */

package alg.cases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//There is little point in not extending the MovieCase class for the PopularityCase class.
//It saves several lines of code and allows the original MovieCase class to remain untouched
//while also allowing the PopularityCase class to be substituted in areas of the project with 
//minimal changes.
public class PopularityCase extends MovieCase
{
	
	private ArrayList<Double> rating; //The case rating average
	private Integer popularity; //The case popularity
	
	/**
	 * constructor - creates a new PopularityCase object.
	 * This constructor is used to accommodate the MovieCase class constructor of a similar contract, but
	 * also to ensure that a default popularity and rating is set where a PopularityCase object is 
	 * constructed from MovieCase data.
	 * @param id - the case id
	 */
	public PopularityCase(final Integer id) 
	{
		super(id);
		rating = new ArrayList<Double>();
		popularity = 0;
	}
	
	/**
	 * constructor - creates a new PopularityCase object.
	 * This constructor is used to accommodate the MovieCase class constructor of a similar contract, but 
	 * also to ensure that a default popularity and rating is set where a PopularityCase object is
	 * constructed from MovieCase data.
	 * @param id - used to populate the case id
	 * @param title - the superclass title
	 * @param genres - the superclass genres
	 * @param directors - the superclass directors
	 * @param actors - the superclass actors
	 */
	public PopularityCase(final Integer id, final String title, final ArrayList<String> genres, final ArrayList<String> directors, final ArrayList<String> actors)
	{
		super(id,title,genres,directors,actors);
		rating = new ArrayList<Double>();
		popularity = 0;
	}
	
	/**
	 * constructor - creates a new PopularityCase object.
	 * This constructor is specific to the PopularityCase class. Where
	 * @param id - used to populate the case id
	 * @param title - the superclass title
	 * @param genres - the superclass genres
	 * @param directors - the superclass directors
	 * @param actors - the superclass actors
	 * @param popularity - used to populate the popularity field
	 * @param rating - used to populate the rating field
	 */
	public PopularityCase(final Integer id, final String title, final ArrayList<String> genres, final ArrayList<String> directors, final ArrayList<String> actors, final Integer popularity, final ArrayList<Double> rating)
	{
		super(id,title,genres,directors,actors);
		this.popularity = popularity;
		this.rating = rating;
	}
	
	/**
	 * returns the popularity
	 */
	public Integer getPopularity()
	{
		return popularity;
	}
	
	/**
	 * returns all ratings in a List<Double>
	 */
	public List<Double> getRating()
	{
		return rating;
	}
	
	/*
	 * adds a rating
	 */
	public void addRating(final Double rating)
	{
		this.rating.add(rating);
		popularity++;
	}
	
	/**
	 * returns a string representation of the PopularityCase object
	 */
	public String toString()
	{
		return super.toString() + " " + getPopularity() + " " + getRating();
	}
}
