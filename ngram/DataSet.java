package ngram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class DataSet {
	public static int numWordTypes;
	public static int numTokens;
	
	public static HashMap<String, WordTypeData> tokenTable; // keyed by word types
	
	public static boolean wordTypeExists( String w ) {
		return tokenTable.containsKey(w);
	}
	
	/* publicly accessible function to initialize all fields in data set */
	public static void initializeDataSet( File[] trainFiles ) throws IOException {
		numWordTypes = 0;
		numTokens = 0;
		tokenTable = new HashMap<String, WordTypeData>();	
		
		for( int i = 0; i < trainFiles.length; i++ ){
			FileInputStream i_s = new FileInputStream( trainFiles[i] );
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			
			String lastWord = "<s>";
			incrementTokens();
			incrementWordTypes( lastWord );
			
			while( (line = in.readLine()) != null ){
				//substitute the last symbol in a sentence the mark for the beginning for next sentence
				line = line.toLowerCase().replaceAll("\\. ", " . <s> ").replaceAll("\\! ", " ! <s> ").replaceAll("\\? ", " ? <s> "); 
				StringTokenizer cur = new StringTokenizer(line);
				StringTokenizer next = new StringTokenizer(line); 
				
				if( next.hasMoreTokens() ) {
					String nextWord = next.nextToken();
					updateTable( lastWord, nextWord );
				}
				
				while( cur.hasMoreTokens() ) {
					String curWord = cur.nextToken();
					incrementTokens();
					incrementWordTypes( curWord );
					
					if( next.hasMoreTokens() ) {
						String nextWord = next.nextToken();
						updateTable( curWord, nextWord );
					} else {
						lastWord = curWord;
					}
				}
			}
			updateTable( lastWord, "<s>" );
			in.close();
		}
	}
	
	public static String randomWord() {
		int randIdx = new Random().nextInt(DataSet.numWordTypes);
		String curW = (String) tokenTable.keySet().toArray()[randIdx];
		return curW;
	}
	
	private static void incrementTokens() {
		numTokens++;
	}
	
	private static void incrementWordTypes( String w ) {
		if( !wordTypeExists(w) ) numWordTypes++;
	}
	
	private static void updateTable( String firstW, String secondW ) {
		if( tokenTable.containsKey(firstW) ) 
			tokenTable.get( firstW ).update( secondW );
		else 
			tokenTable.put( firstW, new WordTypeData( firstW, secondW ) );
	}
	
	public static void CountUnseenWord(){
		HashSet<String> wordType = new HashSet(tokenTable.keySet());
		
		for(Map.Entry<String, WordTypeData> firstW: tokenTable.entrySet()){
		for(String secondW:wordType){
			if(!firstW.getValue().getTable().keySet().contains(secondW)){
				firstW.getValue().getTable().put(secondW, 0);
			}
		}
		}
	}
	
	
}
