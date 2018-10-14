package main.marc;

public class Sentiment {

	private int positiveSentiment;
	private int negativeSentiment;
	private int neutralSentiment;
	
	
	public Sentiment()
	{
		this.positiveSentiment = 0;
		this.negativeSentiment = 0;
		this.neutralSentiment = 0;
	}
	
	public void addPositiveSentiment()
	{
		positiveSentiment++;
	}
	
	public void addNegativeSentiment()
	{
		negativeSentiment++;
	}
	
	public void addNeutralSentiment()
	{
		neutralSentiment++;
	}
	
	public String toString()
	{
		return new String("Positive: " + positiveSentiment + " " + ", Negative:  " + negativeSentiment + " " + ", Neutral: " + neutralSentiment);
	}
	
}
