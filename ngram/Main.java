package ngram;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws IOException{
		File folder = new File( args[0] );      //read files from a directory
		File[] files = folder.listFiles();
		
		DataSet.initializeDataSet( files );
		System.out.println("Trained with " +args[0]);
		System.out.println( "tokens: " + DataSet.numTokens + " word types: " + DataSet.numWordTypes);
		System.out.println();
		
		HashMap<String, WordTypeData> table = DataSet.tokenTable;
	/*	for( Map.Entry<String, WordTypeData> entry : table.entrySet() ) {
			String key = entry.getKey();
			WordTypeData val = entry.getValue();
			
			System.out.print( key + " => ");
			for( Map.Entry<String, Integer> innerEntry : val.getTable().entrySet() ) {
				String k = innerEntry.getKey();
				int v = innerEntry.getValue();
				System.out.print( k + " " + v + ", ");
			}
			System.out.println();
		}*/
		
//		int limit = 15;
		Unigram.buildNoSmoothModel();
//		Bigram.buildNoSmoothModel();
//		System.out.println("10 Sentences generated without seeding and limit length of 15 words:"+"\n"+"Unigram:");
//		for(int i =0;i<11;i++){
//			Unigram.genSentence(limit);
//		}
//		System.out.println("Bigram");
//		for(int i =0;i<11;i++){
//			Bigram.genSentence(limit);
//		}
//		System.out.println();
//		System.out.println("10 Sentences generated with seeding and limit length of 15 words:"+"\n"+"Unigram:");
//		for(int i =0;i<11;i++){
//			String seed = DataSet.randomWord();
//			Unigram.genSenWithSeeding(seed, limit);
//		}
//		System.out.println("Bigram");
//		for(int i =0;i<11;i++){
//			String seed = DataSet.randomWord();			
//			Bigram.genSenWithSeeding(seed, limit);
//		}
//		
//      Bigram.goodTuringSmooth();
		Unigram.goodTuringSmooth();
		
//		for(Map.Entry<String, HashMap<String, Double>> entry:Bigram.smoothModel.entrySet()){
//			String firstW = entry.getKey();
//			HashMap<String, Double> innerEntry = entry.getValue();
//			for(Map.Entry<String, Double> item:innerEntry.entrySet()){
//				System.out.println(firstW + "=>" +item.getKey()+": "+item.getValue());
//			}
//		}
		
		for(Map.Entry<String, Double> entry : Unigram.smoothModel.entrySet()){
			System.out.println(entry.getKey()+" => "+entry.getValue());
		}
		
	}
	

}
