package ngram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Bigram {
	
	public static HashMap<String, HashMap<String, Double>> noSmoothModel;
	
	private static HashMap<Integer, Double> smoothFreqTable;
	public static HashMap<String, HashMap<String, Double>> smoothModel;
	
	
	public static void buildNoSmoothModel() {
		noSmoothModel = new HashMap<String, HashMap<String, Double>>();
		for( Map.Entry<String, WordTypeData> entry : DataSet.tokenTable.entrySet() ) {
			String firstW = entry.getKey();
			HashMap<String,Integer> table = entry.getValue().getTable();
			
			int firstWCount = entry.getValue().getFreq();
			HashMap<String, Double> innerMap = new HashMap<String, Double>();
			
			for( Map.Entry<String, Integer> innerEntry : table.entrySet() ) {
				String secondW = innerEntry.getKey();
				int pairCount = innerEntry.getValue();
				double prob = (double) pairCount / firstWCount;
				innerMap.put( secondW, prob );
			}
			
			noSmoothModel.put( firstW, innerMap );
		}
	}
	
	public static void genSentence(int limit){
		
		StringBuilder sen = new StringBuilder();
		String curW = (String) noSmoothModel.keySet().toArray()[(new Random().nextInt(DataSet.numWordTypes))];
		genSenWithSeeding(curW,limit);
	}
	
	public static void genSenWithSeeding(String seed,int limit){
		String curW  = seed.toLowerCase();
		StringBuilder sen = new StringBuilder();
		sen.append(curW);
		int count = 1;
		while(!curW.matches("^(\\.)|(\\!)|(\\?)$")&&count<=limit){
			
			String nextW = findNextWord(curW);
			if(nextW.equals("<s>")){
				continue;
			}
			sen.append(" "+nextW);
			curW = nextW;
			count++;
		}
		
		if(count == limit){
		sen.append(curW);}
		System.out.println(sen.toString());
	}
	
	private static String findNextWord(String curW)
	{
		double maxP = 0.0;
		String nextW = "";
		for(Map.Entry<String, Double> entry:noSmoothModel.get(curW).entrySet()){
			if(entry.getValue()>maxP){
				nextW = entry.getKey();
				maxP = entry.getValue();
			}
		}
		
		return nextW;
	}
	
	private static void createFreqTable(){
		countUnseenWord();
		HashMap<Integer, Integer> freqTable = new HashMap<Integer, Integer> ();
		smoothFreqTable = new HashMap<Integer, Double>();
		for(Map.Entry<String, WordTypeData> entry: DataSet.tokenTable.entrySet()){
			HashMap<String, Integer> table = entry.getValue().getTable();
			for(Map.Entry<String, Integer> wordPair: table.entrySet()){
				int freq = wordPair.getValue();
				if(!freqTable.containsKey(wordPair.getValue())){
					freqTable.put(freq, 1);
				}
				else{
					freqTable.put(freq,freqTable.get(freq)+1);
				}
			}
		}
		
		
		int maxC = 0;
		for (Map.Entry<Integer, Integer> entry:freqTable.entrySet()){
			maxC = Math.max(entry.getKey(),maxC);
		}
		int curC = 0;
		int nextC = 1;
		
		
		
		while(nextC <= maxC){
			if(!freqTable.containsKey(nextC)){
				nextC++;
				continue;  // skip to next c if Nc+1 = 0;
			}
			else{
				double newFreq = (nextC) * (double)((double)freqTable.get(nextC)/freqTable.get(curC));
				smoothFreqTable.put(curC, newFreq);
				curC = nextC;
				nextC++;
			}
		}
		smoothFreqTable.put(maxC, (double)(freqTable.get(maxC)));

	}
	
	private static void countUnseenWord(){
		
		HashSet<String> wordType = new HashSet(DataSet.tokenTable.keySet());
		System.out.println(wordType.size());
		
		for(Map.Entry<String, WordTypeData> firstW: DataSet.tokenTable.entrySet()){
		for(String secondW:wordType){
			if(!firstW.getValue().getTable().keySet().contains(secondW)){
				firstW.getValue().getTable().put(secondW, 0);
				//DataSet.tokenTable.put(firstW.getKey(), firstW.getValue());
			}
		}
		}
	}
	
	public static void goodTuringSmooth(){
		smoothModel = new HashMap<String, HashMap<String, Double>>();
		createFreqTable();
//		for (Map.Entry<Integer, Double> entry : smoothFreqTable.entrySet()){
//			System.out.println(entry.getKey() + ": " + entry.getValue());
//		}
		for (Map.Entry<String, WordTypeData> entry:DataSet.tokenTable.entrySet()){
			String firstW = entry.getKey();
			HashMap<String, Integer> table = entry.getValue().getTable();
			HashMap<String,Double> innerMap = new HashMap<String,Double>();
			
			for(Map.Entry<String, Integer> innerEntry: table.entrySet()){
				String secondW = innerEntry.getKey();
				double freq = smoothFreqTable.get(innerEntry.getValue());
				innerMap.put(secondW, (double)freq/entry.getValue().getFreq());
			}
			
			
			smoothModel.put(firstW, innerMap);
			
		}
		
	
		
	}
	
	}
	
	

