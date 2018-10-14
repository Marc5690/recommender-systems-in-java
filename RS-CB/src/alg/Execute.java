/**
 * Used to evaluate the case-based recommendation algorithm
 * 
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import java.io.File;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.JaccardCaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.Recommender;
import alg.recommender.MeanRecommender;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class Execute 
{
	public static void main(String[] args)
	{	
		// set the paths and filenames of the training, test and movie metadata files and read in the data
		String trainFile = "dataset" + File.separator + "trainData.txt";
		String testFile = "dataset" + File.separator + "testData.txt";
		String movieFile = "dataset" + File.separator + "movies.txt";
		DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);
		
		// configure the case-based recommendation algorithm - set the case similarity and recommender
		CaseSimilarity caseSimilarity = new OverlapCaseSimilarity();
		Recommender recommender = new MeanRecommender(caseSimilarity, reader);
		
		// evaluate the case-based recommender
		Evaluator eval = new Evaluator(recommender, reader);

		System.out.println("topN Recall Precision");
		for(int topN = 5; topN <= 50; topN += 5)
			System.out.println(topN + " " + eval.getRecall(topN) + " " + eval.getPrecision(topN));
	}
}
