/**
 * An abstract class to define a case-based recommender
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import util.Matrix;
import util.reader.DatasetReader;
import alg.cases.MovieCase;
import alg.cases.similarity.CaseSimilarity;
import alg.feature.similarity.FeatureSimilarity;

public abstract class Recommender 
{
	private Matrix matrix; // a Matrix object to store case similarities
	
	private Matrix actorMatrix; // a Matrix object to store actor similarities
	private Matrix directorMatrix; // a Matrix object to store director similarities
	private Matrix genreMatrix; // a Matrix object to store genre similarities
	
	/**
	 * constructor - creates a new Recommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public Recommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		BufferedReader tfReader = null; //This class now checks binaryData.txt for data before attempting to call similarity methods.
		
		try 
		{ 
			tfReader = new BufferedReader(new FileReader("tfData.txt"));
		} 
		catch (FileNotFoundException e2) 
		{
			e2.printStackTrace();
		}
		
		try {
			if(tfReader.readLine() != null) //If file has contents, read them
			{
			
				FileInputStream tfidfInputStream = null;
			
				ObjectInputStream storedInputTFIDFDocumentOO = null;
			
				try 
				{
					tfidfInputStream = new FileInputStream("tfData.txt");
				} 
				catch (FileNotFoundException e2) 
				{
					e2.printStackTrace();
				}
			
				try 
				{
					storedInputTFIDFDocumentOO = new ObjectInputStream(tfidfInputStream);
					try 
					{
						this.matrix = (Matrix) storedInputTFIDFDocumentOO.readObject();
					} 
					catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					} 
					storedInputTFIDFDocumentOO.close();
				} 
				catch (IOException e1) 
				{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}	
			}
			else
			{
				Set<Integer> ids = reader.getCasebase().getIds();
				this.matrix = new Matrix();
				
				for(Integer rowId: ids)
					for(Integer colId: ids)
						if(rowId.intValue() != colId.intValue())
						{
							double sim = caseSimilarity.getSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
							if(sim > 0)
								matrix.addElement(rowId, colId, sim);
						}
			    FileOutputStream tfidfOutputStream = null;				
				ObjectOutputStream storedOutputTFIDFDocumentOO = null;
				
				try {
					tfidfOutputStream = new FileOutputStream("tfData.txt");
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				try {
					storedOutputTFIDFDocumentOO = new ObjectOutputStream(tfidfOutputStream);
					storedOutputTFIDFDocumentOO.writeObject(matrix);
					storedOutputTFIDFDocumentOO.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
	
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Added new constructor for Task 3's "PersonalisedRecommender.java" class.
	public Recommender(final DatasetReader reader)
	{		
		// compute all feature similarities and store in Matrix objects
		this.genreMatrix = new Matrix();
		this.directorMatrix = new Matrix();
		this.actorMatrix = new Matrix();
		Set<Integer> ids = reader.getCasebase().getIds();
		
		//Similar to the original constructor, but iterate over the features and store them separately.
		for(Integer rowId: ids)
			for(Integer colId: ids)
				if(rowId.intValue() != colId.intValue())
				{
					MovieCase one = (MovieCase) reader.getCasebase().getCase(rowId);
					MovieCase two = (MovieCase) reader.getCasebase().getCase(colId);
					double actorSim = FeatureSimilarity.overlap(one.getActors(),two.getActors());
					double genreSim = FeatureSimilarity.overlap(one.getGenres(),two.getGenres());
					double directorSim = FeatureSimilarity.overlap(one.getDirectors(),two.getDirectors());
					if(genreSim > 0)
						genreMatrix.addElement(rowId, colId, genreSim);
					if(actorSim > 0)
						actorMatrix.addElement(rowId, colId, actorSim);
					if(directorSim > 0)
						directorMatrix.addElement(rowId, colId, directorSim);
				}
		
	}
	
	/**
	 * returns the case similarity between two cases
	 * @param rowId - the id of the first case
	 * @param colId - the id of the second case
	 * @return the case similarity or null if the Matrix object does not contain the case similarity
	 */
	public Double getCaseSimilarity(final Integer rowId, final Integer colId)
	{
		return matrix.getElement(rowId, colId);
	}
	
	/**
	 * returns a ranked list of recommended case ids
	 * @param userId - the id of the target user
	 * @param reader - an object to store user profile data and movie metadata
	 * @return the ranked list of recommended case ids
	 */
	public abstract ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader);
}
