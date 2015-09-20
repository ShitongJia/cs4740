package ngram;

import java.util.HashMap;

public class WordTypeData {
	
	private String word;  					// word type
	private int freq;	  					// frequency of a word type
	private HashMap<String,Integer> table;  // key is the next word (for bigram). 
											// value is the frequency of this word pair
	
	public WordTypeData(String w){ 
		word = w;
		freq = 1;
		table = new HashMap<String,Integer>();
	}
	
	public WordTypeData( String firstW, String secondW ) {
		word = firstW;
		freq = 1;
		table = new HashMap<String,Integer>();
		table.put( secondW, 1 );
	}
	
	public int getFreq() {
		return freq;
	}
	
	public HashMap<String,Integer> getTable() {
		return table;
	}
	
	/* Update the wordtype data when another bigram is processed */
	public void update( String secondW ) {
		incrementFreq();
		if( table.containsKey( secondW ) ) {
			table.put( secondW, table.get(secondW)+1 );
		} else {
			table.put( secondW, 1 );
		}
	}
	/**/
	public void supplement(String noSeenW){
		
	}
	
	private void incrementFreq() {
		freq++;
	}
}
