package main.marc;

import java.util.HashMap;

public class ProductSentiment {
	
	private String id;
	private HashMap<String, Sentiment> features;
	
	public ProductSentiment(String id)
	{
		this.id = id;
		this.features = new HashMap<String, Sentiment>();
	}

	public void addPositiveSentiment(String feature)
	{
		if(features.containsKey(feature))
		{
			features.get(feature).addPositiveSentiment();
		}
		else
		{
			features.put(feature, new Sentiment());
			features.get(feature).addPositiveSentiment();
		}
	}
	
	public void addNegativeSentiment(String feature)
	{
		if(features.containsKey(feature))
		{
			features.get(feature).addNegativeSentiment();
		}
		else
		{
			features.put(feature, new Sentiment());
			features.get(feature).addNegativeSentiment();
		}
		
	}
	
	public void addNeutralSentiment(String feature)
	{
		if(features.containsKey(feature))
		{
			features.get(feature).addNeutralSentiment();
		}
		else
		{
			features.put(feature, new Sentiment());
			features.get(feature).addNeutralSentiment();
		}
	}
	
	public String toString()
	{
		return new String("Product ID: " + id);
	}
}
