package ngram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class Unigram {
	public static HashMap<String, Double> noSmoothModel; // key: word type => value : probability
	public static HashMap<String, Double> smoothModel;
	public static HashMap<Integer, Double> smoothFreqTable;
	
	public static void buildNoSmoothModel() {
		noSmoothModel = new HashMap<String, Double>();
		int tokens = DataSet.numTokens;
		
		for( Map.Entry<String, WordTypeData> entry : DataSet.tokenTable.entrySet() ) {
			String word = entry.getKey();
			int count = entry.getValue().getFreq();
			double prob = (double) count / tokens;
			noSmoothModel.put( word, prob );
		}
	}
	
	public static void genSentence(int limit){
		
		StringBuilder sen = new StringBuilder();
		String curW = (String) noSmoothModel.keySet().toArray()[(new Random().nextInt(noSmoothModel.keySet().size()))];
		genSenWithSeeding(curW,limit);
	}
	
	public static void genSenWithSeeding(String seed,int limit){
		StringBuilder sen = new StringBuilder();
		String curW = seed;
		sen.append(curW);
		int count = 1;
		while(!curW.matches("^(\\.)|(\\!)|(\\?)$")&&count<=limit){
			curW = (String)(noSmoothModel.keySet().toArray()[new Random().nextInt(DataSet.numWordTypes)]);
			if(curW.equals("<s>")){
				continue;
			}
			sen.append(" "+curW);
			
			//System.out.println(curW);
			count++;
			
		}
		if(count == limit)
		sen.append(curW);
		
		System.out.println(sen.toString());
	}
	
	private static void createFreqTable(){
		smoothFreqTable = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> freqTable = new HashMap<Integer,Integer>();
		
		for(Map.Entry<String, WordTypeData> entry: DataSet.tokenTable.entrySet()){
			int freq = entry.getValue().getFreq();
			if(!freqTable.containsKey(freq)){
				freqTable.put(freq, 1);
			}
			else{
				freqTable.put(freq,freqTable.get(freq)+1);
			}
		}
		
		int maxC = 0;
		for(Map.Entry<Integer, Integer> entry :freqTable.entrySet()){
			maxC = Math.max(entry.getKey(), maxC);
		}
		
		int curC = 1;   
		int nextC = 2;
		
		while(nextC <= maxC){
			if(!freqTable.containsKey(nextC)){
				nextC++;
				continue;
			}
			else{
				double newFreq = (nextC) * (double)((double)freqTable.get(nextC)/freqTable.get(curC));
				smoothFreqTable.put(curC,newFreq);
				curC = nextC;
				nextC++;
			}
			
		}
		smoothFreqTable.put(maxC, (double)(freqTable.get(maxC)));
	}
	
	public static void goodTuringSmooth(){
		
		createFreqTable();
//		for (Map.Entry<Integer, Double> entry : smoothFreqTable.entrySet()){
//			System.out.println(entry.getKey() + ": " + entry.getValue());
//		}
		
		smoothModel = new HashMap<String,Double> ();
		int tokens = DataSet.numTokens;
		
		for(Map.Entry<String, WordTypeData> entry:DataSet.tokenTable.entrySet()){
			String word = entry.getKey();
			double newFreq = smoothFreqTable.get(entry.getValue().getFreq());
			smoothModel.put(word, newFreq / tokens);
		}
		
	}
	
	
	 //private String findNextWord(String curW){
		
	//}
}
