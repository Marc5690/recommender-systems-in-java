/**
 * An example class to read in a review dataset
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package examples;

import java.util.ArrayList;
import java.util.Set;

import util.Review;
import util.reader.DatasetReader;

public class ReviewExample 
{
	public final static String EOL = System.getProperty("line.separator");
	
	public static void main(String[] args)
	{
		String filename = "Digital Camera.txt"; // set the dataset filename
		//String filename = "Printer.txt"; // set the dataset filename
		DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class
		
		Set<String> productIds = reader.getProductIds(); // get all product ids
		System.out.println("total # products: " + productIds.size()); // print the number of product ids in the Set
		
		ArrayList<Review> reviews = reader.getReviews(); // get all reviews and store in an ArrayList
		System.out.println("total # reviews: " + reviews.size()); // print the number of reviews in the ArrayList
		System.out.println("\nthe 3rd review:\n" + reviews.get(2).toString()); 	// print the 3rd review
		
		// tip - how to replace all <br /> tags with line separators: 
		System.out.println("\n*** review text with \"<br />\" tags:\n" + reviews.get(2).getReviewText());		
		System.out.println("\n*** review text with \"<br />\" tags replaced by line separators:\n" + 
							reviews.get(2).getReviewText().replaceAll("<br />", EOL));
	}
}