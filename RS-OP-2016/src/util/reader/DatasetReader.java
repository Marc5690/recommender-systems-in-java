/**
 * A class to read in and store product reviews.
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import util.Review;

public class DatasetReader 
{
	private ArrayList<Review> reviews; // data structure to store all reviews
	private Set<String> productIds; // data structure to store all distinct product ids

	/**
	 * default constructor - creates a new DatasetReader object
	 */
	public DatasetReader()
	{
		reviews = new ArrayList<Review>();
		productIds = new HashSet<String>();
	}
	
	/**
	 * default constructor - creates a new DatasetReader object
	 * @param filename - the name of the file containing the reviews
	 */
	public DatasetReader(final String filename)
	{
		this();
		readReviews(filename);
	}
	
	/**
	 * @return the reviews
	 */
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	/**
	 * @return a sample subset of the reviews
	 */
	public ArrayList<Review> getSampleReviews() {
		ArrayList<Review> reviewsArray = new ArrayList<Review>();

		reviewsArray.add(new Review("a", "b", "b", "b", "b", "b", "I have and love my Canon G10 but did not want to carry it everywhere with me. I liked how you can move through the different modes quickly and I especially liked the live mode. I am an artist and it is very important for me to control color and contrast and I cxan easily do that in the live mode.", 3.3, "b", 2, 4, 4.4));
		reviewsArray.add(new Review("b", "b", "b", "b", "b", "b", "I have and love my Canon G10 but did not want to carry it everywhere with me. I liked how you can move through the different modes quickly and I especially liked the live mode. I am an artist and it is very important for me to control color and contrast and I can easily do that in the live mode.", 3.3, "b", 2, 4, 4.4));

		//		reviewsArray.add(new Review("b", "b", "b", "b", "b", "b", "I have my Powershot A2200 for about two weeks now. I was a little concerned about the mixed reviews but I thought I would take a chance. I have and love my Canon G10 but did not want to carry it everywhere with me.  I needed a little camera to carry with me and not worry about. I purchased my Powershot A2200 for 129. and could not complain about that. I liked how you can move through the different modes quickly and I especially liked the live mode. I am an artist and it is very important for me to control color and contrast and I can easily do that in the live mode. I understand the problems some of the other reviews complained about especially the blurriness. I too had blurry photos when I first started using it. This camera has a learning curve - it is a very sensitive camera and you have to become familiar with it and choose the mode ( program, live , auto ) that is best for you. Also there are several options to control in the focus of the camera. I suggest you become familiar with it to improve your results.  This is a good camera for someone like myself that is familiar with using cameras and want options for controlling images.  If you just want to point and shoot and not think about it - this may not be the best choice. I am really impressed with the photos I have taken. I mainly use the live mode - it is a very flexible mode - shooting well in low museum light and even out of a fast moving car.  Given what I received for this cost I am very pleased. I love my little light canon camera.", 3.3, "b", 2, 4, 4.4));
//		reviewsArray.add(new Review("c", "b", "b", "b", "b", "b", "I have my Powershot A2200 for about two weeks now. I was a little concerned about the mixed reviews but I thought I would take a chance. I have and love my Canon G10 but did not want to carry it everywhere with me.  I needed a little camera to carry with me and not worry about. I purchased my Powershot A2200 for 129. and could not complain about that. I liked how you can move through the different modes quickly and I especially liked the live mode. I am an artist and it is very important for me to control color and contrast and I can easily do that in the live mode. I understand the problems some of the other reviews complained about especially the blurriness. I too had blurry photos when I first started using it. This camera has a learning curve - it is a very sensitive camera and you have to become familiar with it and choose the mode ( program, live , auto ) that is best for you. Also there are several options to control in the focus of the camera. I suggest you become familiar with it to improve your results.  This is a good camera for someone like myself that is familiar with using cameras and want options for controlling images.  If you just want to point and shoot and not think about it - this may not be the best choice. I am really impressed with the photos I have taken. I mainly use the live mode - it is a very flexible mode - shooting well in low museum light and even out of a fast moving car.  Given what I received for this cost I am very pleased. I love my little light canon camera.", 3.3, "b", 2, 4, 4.4));
		return reviewsArray;
		//return new ArrayList<Review>(reviews.subList(0, 30));
	}

	/**
	 * @return the product ids
	 */
	public Set<String> getProductIds() {
		return productIds;
	}
	
	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(final ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	/** 
	 * reads reviews from a file
	 * @param filename - the path and filename of the file containing the reviews
 	 */
	private void readReviews(final String filename) 
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("dataset" + File.separator + filename)));
			String line;
			
			while ((line = br.readLine()) != null) 
			{
				String[] tokens = line.split(";;");
				if(tokens.length != 12)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}
				
				String reviewId = tokens[0];
				String reviewerUrl = tokens[1];
				
				String productCategory = tokens[2];
				String productId = tokens[3];
				String productName = tokens[4];
				
				String reviewTitle = tokens[5];
				String reviewText = tokens[6];
				double reviewRating = Double.parseDouble(tokens[7]);
				String reviewDate = tokens[8];
				
				int posVotes = Integer.parseInt(tokens[9]);
				int totalVotes = Integer.parseInt(tokens[10]);
				double helpfulness = Double.parseDouble(tokens[11]);
				
				reviews.add(new Review(reviewId, reviewerUrl, productCategory, productId, productName, reviewTitle, reviewText, reviewRating, reviewDate, posVotes, totalVotes, helpfulness));
				productIds.add(productId);
			}
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
