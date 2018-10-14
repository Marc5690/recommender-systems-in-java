/**
 * Used to evaluate the case-based recommendation algorithm
 * 
 * Marc Laffan
 * 17/02/2016
 * Assignment 1, Task 5
 */

package alg;

import java.io.File;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.JaccardCaseSimilarity;
import alg.cases.similarity.OccurrenceCaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.cases.similarity.SymmetricCaseSimilarity;
import alg.recommender.Recommender;
import alg.recommender.PersonalisedRecommender;
import alg.recommender.MaxRecommender;
import alg.recommender.MeanRecommender;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteTask5 
{
	public static void main(String[] args)
	{	
		// set the paths and filenames of the training, test and movie metadata files and read in the data
		String trainFile = "dataset" + File.separator + "trainData.txt";
		String testFile = "dataset" + File.separator + "testData.txt";
		String movieFile = "dataset" + File.separator + "movies.txt";
		DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);
		
		// configure the case-based recommendation algorithm - set the case similarity and recommender
		CaseSimilarity caseSimilarity = new OccurrenceCaseSimilarity();
		Recommender recommender = new MaxRecommender(caseSimilarity, reader);
		
		// evaluate the case-based recommender
		Evaluator eval = new Evaluator(recommender, reader);

		System.out.println("topN Recall Precision");
		for(int topN = 5; topN <= 50; topN += 5)
			System.out.println(topN + " " + eval.getRecall(topN) + " " + eval.getPrecision(topN));
	}
}
