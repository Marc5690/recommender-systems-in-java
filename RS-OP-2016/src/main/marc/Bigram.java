package main.marc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Bigram {

	// Read bigram 
	Map<String,Set<String>> bigramMap = instantiateBiGrams();
	
	public Bigram()
	{
		
	}
	
	public boolean isBigram(String key, String value)
	{
		if(bigramMap.get(key).contains(value))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	// A map to store all bigram features. Create a separate map to store all single-noun features
	public static Map<String,Set<String>> instantiateBiGrams()
	{
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();

		// Consider two bigram features - "battery power" and "battery life"
		Set<String> set1 = new HashSet<String>();
		set1.add("life");
		set1.add("power");
		map.put("battery", set1);
			
		// And one more feature - "recording button"
		Set<String> set2 = new HashSet<String>();
		set2.add("button");
		map.put("recording", set2);
			
		return map;
	}
}
