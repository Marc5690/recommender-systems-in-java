/**
 * Used to evaluate the item-based CF algorithm 
 * 
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib;

import java.io.File;

import alg.ib.neighbourhood.*;
import alg.ib.predictor.*;
import similarity.metric.*;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteIB
{
	public static void main(String[] args)
	{
		// configure the item-based CF algorithm - set the predictor, neighbourhood and similarity metric ...
		Predictor predictor = new DeviationFromItemMeanPredictor();
		Neighbourhood neighbourhood = new NearestNeighbourhood(5);
		SimilarityMetric metric = new CosineMetric();
		
		// set the paths and filenames of the item file, train file and test file ...
		String itemFile = "ML dataset" + File.separator + "u.item";
		String trainFile = "ML dataset" + File.separator + "u.train";
		String testFile = "ML dataset" + File.separator + "u.test";
		
		// set the path and filename of the output file ...
		String outputFile = "results" + File.separator + "predictions.txt";
		
		////////////////////////////////////////////////
		// Evaluates the CF algorithm (do not change!!):
		// - the RMSE and coverage are output to screen
		// - output file is created
		DatasetReader reader = new DatasetReader(itemFile, trainFile, testFile);
		ItemBasedCF ibcf = new ItemBasedCF(predictor, neighbourhood, metric, reader);
		
		Evaluator eval = new Evaluator(ibcf, reader.getTestData());
		eval.writeResults(outputFile);
		
		Double RMSE = eval.getRMSE();
		if(RMSE != null) System.out.printf("RMSE: %.6f\n", RMSE);
		
		for(int i = 1; i <= 5; i++)
		{
			RMSE = eval.getRMSE(i);
			if(RMSE != null) System.out.printf("RMSE (true rating = %d): %.6f\n", i, RMSE);
		}
		
		double coverage = eval.getCoverage();
		System.out.printf("coverage: %.2f%s\n", coverage, "%");				
	}
}
	