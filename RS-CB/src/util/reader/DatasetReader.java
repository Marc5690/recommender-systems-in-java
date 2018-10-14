/**
 * A class to read in and store user profile data and movie metadata. Also reads in test user profile data.
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import alg.casebase.Casebase;
import alg.cases.TermCase;
import alg.cases.PopularityCase;
import alg.cases.TermCase;
import util.Porter;

public class DatasetReader 
{
	private Casebase cb; // stores case objects
	private Map<Integer,Map<Integer,Double>> userProfiles; // stores training user profiles
	private Map<Integer,Map<Integer,Double>> testProfiles; // stores test user profiles
	private Map<Integer,Double> userGenreWeighting; // stores the genre weighting for individual user profiles
	private Map<Integer,Double> userDirectorWeighting; // stores the director weighting for individual user profiles
	private Map<Integer,ArrayList<String>> userGenreList; // stores the genres of the movies an individual user has seen
	private Map<Integer,ArrayList<String>> userDirectorList; // stores the directors of the movies an individual user has seen
	private double actorWeighting = 1.0;
	private HashMap<String, Integer> tempTermDoc; // HashMap that stores all terms, before we shave off stem words
	private Set<String> termDocument; //Set that stores all terms minus stem words
	private static HashMap<String, HashMap<Integer,Double>> termFrequencyCases;
	private HashMap<String, Double> inverseDocumentFrequencyCases;
	private HashMap<String, Double> finalTermFrequencyInverseDocumentFrequency;
	private static HashMap<String, HashMap<Integer,Double>> binaryCases;
	private static HashMap<String, ArrayList<String>> occurrences;//The actual occurrences themselves, along with every genre that appears with them
	private static HashMap<String, Integer> occurrenceTransactions;//Number of occurrences of genres
	private static Integer totalOccurrences;//Total number of overall occurences	
	//BiMap taken from Guava external library (See "referenced libraries" folder).
	//If having trouble with install, simply go to https://github.com/google/guava/wiki/Release19
	//and download "guava-19.0.jar". Right-click the RS-CB folder, click properties -> libraries -> Add External Jar	
	private BiMap<String, Integer> termDocByName;
	private BiMap<Integer, String> termDocById;
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public DatasetReader(final String trainFile, final String testFile, final String movieFile)
	{
		totalOccurrences = 0;
		occurrences = new HashMap<String, ArrayList<String>>();
		occurrenceTransactions = new HashMap<String, Integer>();
		tempTermDoc = new HashMap<String, Integer>();
		termDocument = new HashSet<String>();
		finalTermFrequencyInverseDocumentFrequency = new HashMap<String, Double>();
		termDocByName = HashBiMap.create();
		termDocById = HashBiMap.create();
		termFrequencyCases = new HashMap<String, HashMap<Integer, Double>>();
		binaryCases = new HashMap<String, HashMap<Integer, Double>>();
		inverseDocumentFrequencyCases = new HashMap<String, Double>();
		userGenreWeighting = new HashMap<Integer, Double>();
		userDirectorWeighting = new HashMap<Integer, Double>();
		userGenreList = new HashMap<Integer, ArrayList<String>>();
		userDirectorList = new HashMap<Integer, ArrayList<String>>();
		readCasebase(movieFile);
		userProfiles = readUserProfiles(trainFile);
		shaveStemWords();
		generateTermIds();
		termFrequency();
		binaryTransform();
		inverseDocumentFrequency();
		finalTermFrequencyInverseDocumentFrequency();
		testProfiles = readUserProfiles(testFile);
	}
	
	/** 
 	 * @return the training user profile ids
	 */
	public Set<Integer> getUserIds()
	{
		return userProfiles.keySet();
	}

	/** 
	 * @param id - the id of the training user profile to return
 	 * @return the training user profile
	 */
	public Map<Integer,Double> getUserProfile(final Integer id)
	{
		return userProfiles.get(id);
	}
	
	/** 
 	 * @return the test user profile ids
	 */
	public Set<Integer> getTestIds()
	{
		return testProfiles.keySet();
	}
	
	/** 
	 * @param id - the id of the test user profile to return
 	 * @return the test user profile
	 */
	public Map<Integer,Double> getTestProfile(final Integer id)
	{
		return testProfiles.get(id);
	}
	
	/**
	 * @return the casebase
	 */
	public Casebase getCasebase()
	{
		return cb;
	}
	
	/** 
	 * @param filename - the path and filename of the file containing the user profiles
 	 * @return the user profiles
	 */
	private Map<Integer,Map<Integer,Double>> readUserProfiles(final String filename)
	{
		
		Map<Integer,Map<Integer,Double>> map = new HashMap<Integer,Map<Integer,Double>>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			br.readLine(); // read in header line
			
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, "\t");
				if(st.countTokens() != 4)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}
				
				Integer userId = new Integer(st.nextToken());
				Integer movieId = new Integer(st.nextToken());
				Double rating = new Double(st.nextToken());
				String review = st.nextToken();
				
				Map<Integer,Double> profile = (map.containsKey(userId) ? map.get(userId) : new HashMap<Integer,Double>());
				profile.put(movieId, rating);
				map.put(userId, profile);
				
				//Added for task 6, add the review
				TermCase currentMovie = (TermCase) cb.getCase(movieId);
				Porter p = new Porter();
				String[] words = review.split(" ");
				for(String word: words)
				{
					if(word!="" && !word.isEmpty())
					{
						String strippedWord = p.stripAffixes(word);
						if(tempTermDoc.containsKey(strippedWord))
						{
							int currentWordCount = tempTermDoc.get(strippedWord).intValue();
							tempTermDoc.put(strippedWord, currentWordCount + 1);
							
						}
						else
						{
							tempTermDoc.put(strippedWord, 1);
						}
						currentMovie.addReview(strippedWord + " ");
					}
				}
				
				//Task 5
				//For each movie the user has seen, add the genre.
				for (String genre: currentMovie.getGenres())
				{
					updateGenre(userId, genre);
				}
				
				//For each movie the user has seen, add the director.
				for (String director: currentMovie.getDirectors())
				{
					updateDirector(userId, director);
				}
				
				//If we are using training data, then populate popularity Casebase
				if(filename.equals("dataset\\trainData.txt") && cb.getCase(movieId).getClass().equals(PopularityCase.class))
				{ 
					
					//If the PopularityCase exists already, then just add this lines rating
					if(cb.getCase(movieId) != null)
					{
						PopularityCase existingPopCase = (PopularityCase) cb.getCase(movieId);
						existingPopCase.addRating(rating);
					}
					
					//If it does not, add a new PopularityCase to the PopularityCaseBase using the movie ID as a key
					else
					{
						PopularityCase pc = new PopularityCase(movieId);
						pc.addRating(rating);					
						cb.addCase(movieId, pc);
					}
				}
			}
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		return map;
	}
	
	/** 
	 * creates the casebase
	 * @param filename - the path and filename of the file containing the movie metadata
 	 */
	private void readCasebase(final String filename) 
	{
		if(cb == null)
		{
			cb = new Casebase();
		}
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			
			while ((line = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, "\t");
				if(st.countTokens() != 5)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.out.println(line);
					System.out.println(st.countTokens());
					System.exit(1);
				}
				
				Integer id = new Integer(st.nextToken());
				String title = st.nextToken();
				ArrayList<String> genres = tokenizeString(st.nextToken());
				ArrayList<String> directors = tokenizeString(st.nextToken());
				ArrayList<String> actors = tokenizeString(st.nextToken());
				totalOccurrences += 1;
				
				//Task 5, iterate the each genre and "pair" it with any others it occurs with. Then, put the results in a hashmap
				for(String genre: genres)
				{	
					ArrayList<String> coOccurrences = new ArrayList<String>(genres);
					coOccurrences.remove(genre);
					
					if(occurrences.containsKey(genre))
					{
						ArrayList<String> currentOccurrences= occurrences.get(genre);
						currentOccurrences.addAll(coOccurrences);
						occurrences.put(genre, currentOccurrences);
						
					}
					else
					{
						occurrences.put(genre, coOccurrences);
					}
				}
				
				//Task 5. Iterate the genres again, and use them to increment the transaction occurences hashmap (Basically a list of the numbers of times a genre appears) 
				for(String genre: genres)
				{	
					if(occurrenceTransactions.containsKey(genre))
					{
						Integer transactionNumber = occurrenceTransactions.get(genre);
						occurrenceTransactions.put(genre, transactionNumber + 1);
					}
					else
					{
						occurrenceTransactions.put(genre, 1);
					}
				}
				
				//Change both TermCase reference and object to PopularityCase for task two 
				//TermCase for tasks 5 + 6
				//but leave as TermCase for task one.
				TermCase movie = new TermCase(id, title, genres, directors, actors);
				cb.addCase(id, movie);
			}
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/** 
	 * @param str - the string to be tokenized; ',' is the delimiter character
 	 * @return a list of string tokens
	 */
	private ArrayList<String> tokenizeString(final String str)
	{
		ArrayList<String> al = new ArrayList<String>();
		
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			al.add(st.nextToken().trim()); 
		
		return al;
	}
	
	//Upon taking the top 20% of the most occurring (stem) terms from the term document,
	//I found that it was removing words that could possibly be key to identifying similarities
	//(Words such as actress and violence). So it was recduced to 12% (Line 344).
	public void shaveStemWords()
	{		
		Integer shaveTop = (int) (tempTermDoc.size()*0.12);
		
		for(int i = 0; i < shaveTop; i++)
		{
			Integer maxValue = 0;
			HashMap.Entry<String, Integer> mostOccurring = null;
			
			for(HashMap.Entry<String, Integer> entry : tempTermDoc.entrySet())
			{
				if(entry.getValue() > maxValue)
				{
					mostOccurring = entry;
					maxValue = entry.getValue();
				}
			}
			tempTermDoc.remove(mostOccurring.getKey());
		}
		for(String term: tempTermDoc.keySet())
		{
			termDocument.add(term);
		}
	}
	
	//Populate two HashBiMaps with a term and a unique ID for that term, and a unique ID followed by it's term.
	//These were suggested to be used for term lookup purposes. Convenience in finding terms, more than anything else.
	public void generateTermIds()
	{
		int id = 0;
		for(String term:termDocument)
		{
			termDocByName.put(term, id);
			id++;
		}
			
		termDocById = termDocByName.inverse();	
	}
	
	//We normalise the Term Frequency, then perform Inverse Document Frequency
	public void termFrequency()
	{
		//Explanation on "cases" hashmap:
		//String key is the term itself. The value is initially the movie ID followed by the number
		//of occurrences of the term in the movies document.
		//Following one more loop, the number of occurrences are overridden to hold the normalised
		//term frequency by dividing the number of occurrences by the highest document term frequency.
		
		HashMap<String, Double> maxTermFrequencies = new HashMap<String, Double>();
		
		for(String term: termDocument)
		{
			HashMap<Integer,Double> termCases = new HashMap<Integer,Double>();
			Double maxTF = 0d;
			
			for(Integer termCaseId: cb.getIds())
			{

				TermCase termCase = (TermCase) cb.getCase(termCaseId);
				String reviewConcatenation = "v";//termCase.getReviewsDocument();

					
				//StringUtils imported from the Apache Commons jar file: 
				//http://commons.apache.org/proper/commons-lang/download_lang.cgi
				Double count = (double) StringUtils.countMatches(reviewConcatenation, term);
				
				termCases.put(termCaseId, count);
				if(count>maxTF)
				{
					maxTF = count;
				}
				maxTermFrequencies.put(term, maxTF);
					
				termFrequencyCases.put(term, termCases);
			}
		}
		
		//Rather than compute them again, we take the current contents of termFrequencyCases so that the binaryTransform()
		//method can simply convert the values to binary numbers, rather than calculate all of this again.
		binaryCases = termFrequencyCases;

		for(String termCase : termFrequencyCases.keySet())
		{
			Double maxTF = maxTermFrequencies.get(termCase);
			
			for(Map.Entry<Integer, Double> TermCase: termFrequencyCases.get(termCase).entrySet())
			{
				Double occurences = TermCase.getValue();
				double termFrequency = occurences.doubleValue()/maxTF.doubleValue();
				TermCase.setValue(termFrequency);
			}
			
		}
	}
	
	public void binaryTransform() //Convert the binary list to hold ones and zeroes, rather than fractions.
	{
		for(Entry<String, HashMap<Integer, Double>> binaryCase: binaryCases.entrySet())
		{
			for(Entry<Integer, Double> binaryConversion :binaryCase.getValue().entrySet()){
				if(binaryConversion.getValue() > 0 )
				{
					binaryConversion.setValue(1.0);
				}
				else
				{
					binaryConversion.setValue(0.0);
				}
			}
		}	
	}
	
	public void inverseDocumentFrequency() //Perform IDF operations, as seen in the lectures slides.
	{
		double logMax = cb.size();
		
		for(String term: termDocument)
		{
			double count = 0.0;
			for(Integer id : cb.getIds())
			{
				TermCase currentTerm = (TermCase) cb.getCase(id);  
				if(currentTerm.getReviewsDocument().contains(term))
				{
					count++;
				}
				
			}
			double idf = Math.log10(logMax/count);
			if(idf>1.0)
			{
				idf = 1.0;
			}
			inverseDocumentFrequencyCases.put(term, idf);
		}
	}
	
	public void finalTermFrequencyInverseDocumentFrequency() //FInal iteration over termFrequencyCases where we perform the last TFIDF operations.
	{
		for(String term: termFrequencyCases.keySet())
		{
			double idf = inverseDocumentFrequencyCases.get(term);
			
			for(Map.Entry<Integer, Double> movie : termFrequencyCases.get(term).entrySet())
			{
				Double tf = movie.getValue();
				double overallFrequency = idf * tf;
				movie.setValue(overallFrequency);
			}
		}
	}
	
	//Method that adds a genre string to the userGenreList map and updates the userGenreWeighting map
	private void updateGenre(int userId, String genre)
	{
		ArrayList<String> userGenres = userGenreList.get(userId) != null ? userGenreList.get(userId) : null;
		
		if(userGenres != null)
		{
			userGenreList.get(userId).add(genre);
		}
		else
		{
			ArrayList<String> genreList = new ArrayList<String>();
			genreList.add(genre);
			userGenreList.put(userId, genreList);
		}	
		
		ArrayList<String> allGenres = userGenreList.get(userId);
		Set<String> uniqueGenres = new HashSet<String> (userGenreList.get(userId));
		double allGenreSize = (double)allGenres.size();
		double allUniqueGenreSize = (double) uniqueGenres.size();
		double genreWeighting = allUniqueGenreSize/allGenreSize;
		
		userGenreWeighting.put(userId, genreWeighting);
	}
	
	//Method that adds a director string to the userDirectorList map and updates the userDirectorWeighting map
	private void updateDirector(int userId, String director)
	{
		ArrayList<String> userDirectors = userDirectorList.get(userId) != null ? userDirectorList.get(userId) : null;
		
		if(userDirectors != null)
		{
			userDirectorList.get(userId).add(director);
		}
		else
		{
			ArrayList<String> directorList = new ArrayList<String>();
			directorList.add(director);
			userDirectorList.put(userId, directorList);
		}
		
		ArrayList<String> allDirectors = userDirectorList.get(userId);
		Set<String> uniqueDirectors = new HashSet<String> (userDirectorList.get(userId));
		
		double allDirectorSize = (double)allDirectors.size();
		double allUniqueDirectorSize = (double) uniqueDirectors.size();
		double directorWeighting = allUniqueDirectorSize/allDirectorSize;
		
		userDirectorWeighting.put(userId, directorWeighting);
	}
	
	//Getter method for the userGenreWeighting map
	public Map<Integer, Double> getGenreWeighting() 
	{
		return userGenreWeighting;
	}

	//Getter method for the userDirectorWeighting map
	public Map<Integer, Double> getDirectorWeighting() 
	{
		return userDirectorWeighting;
	}
	
	//Getter method for the users individual genre weighting
	public Double getUserGenreWeighting(int userId) 
	{
		return userGenreWeighting.get(userId);
	}

	//Getter method for the users individual director weighting
	public Double getUserDirectorWeighting(int userId) 
	{
		return userDirectorWeighting.get(userId);
	}
	
	//Getter method for the actor weighting. For task 3, this is simply set as 1.0.
	public Double getUserActorWeighting() 
	{
		return actorWeighting;
	}
	
	//Getter method for Binary Cases
	public static HashMap<String, HashMap<Integer, Double>> getBinaryCases() 
	{
		return binaryCases;
	}
	
	//Getter method for occurrences
	public static HashMap<String, ArrayList<String>> getOccurrences() 
	{
		return occurrences;
	}
	
	//Getter method for occurrence transactions
	public static HashMap<String, Integer> getOccurrenceTransactions() 
	{
		return occurrenceTransactions;
	}
	
	//Getter method for total occurrences
	public static Integer getTotalOccurrences() 
	{
		return totalOccurrences;
	}
	
	//Getter method for term frequency cases
	public static HashMap<String, HashMap<Integer,Double>> getTermFrequencyCases()
	{
		return termFrequencyCases;
	}
}
