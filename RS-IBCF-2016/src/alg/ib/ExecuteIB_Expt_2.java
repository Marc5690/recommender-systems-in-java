/**
 * This class provides an example of how to run the experiments: in this example, RMSE and coverage performance
 * versus neighbourhood size is investigated using the nearest neighbourhood, simple average predictor, and 
 * Pearson correlation similarity approaches.
 * 
 * Michael O'Mahony
 * 20/02/2016
 */

package alg.ib;

import java.io.File;

import alg.ib.neighbourhood.*;
import alg.ib.predictor.*;
import similarity.metric.*;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteIB_Expt_2
{
	public static void main(String[] args)
	{
		System.out.println("neighbourhoodSize,rmse,coverage");				

		for (double neighbourhoodSize = 0.00d; neighbourhoodSize <= 1d; neighbourhoodSize += 0.05d)
		{
			// configure the item-based CF algorithm - set the predictor, neighbourhood and similarity metric ...
			Predictor predictor = new DeviationFromItemMeanPredictor();
			Neighbourhood neighbourhood = new SimilarityThreshold(neighbourhoodSize);
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
			double rmse = (RMSE != null) ? RMSE.doubleValue() : -1;
			double coverage = eval.getCoverage();
			System.out.println(neighbourhoodSize + "," + rmse + "," + coverage);		
		}
	}
}
