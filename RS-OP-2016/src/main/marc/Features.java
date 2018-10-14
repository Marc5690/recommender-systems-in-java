package main.marc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Features {

	private ArrayList<String> listOfFeatures;
	private HashMap<String, ArrayList<String>> sentimentMappings;
	private static ArrayList<String> listOfFeatureKeys = new ArrayList<String>(Arrays.asList("image", "battery", "physical", "display", "extensibility", "support", "pricing"));
	
	public Features()
	{
		listOfFeatures = new ArrayList<String>();
	}
	
	public Features(ArrayList<String> listOfFeaturesIn)
	{
		listOfFeatures = listOfFeaturesIn;
	}
	
	public void addFeature(String feature)
	{
		listOfFeatures.add(feature);
	}
	
	public boolean hasFeature(String feature)
	{
		if(listOfFeatures.contains(feature))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void loadFeatures(String fileName)
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("feature sets" + File.separator + fileName)));
			String line;
			while ((line = br.readLine()) != null) 
			{
					listOfFeatures.add(line);
			}
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
