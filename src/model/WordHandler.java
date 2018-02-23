package model;
import java.io.*;
import java.util.*;

public class WordHandler {
	//the file path of the file storing Chinese words
	private static final String SourceFileName = GameProtocol.CHINESE_WORD_PATH;
	//the file reader
	private BufferedReader reader;
	//storing Chinese words 
	private LinkedList<String> wordList;
	//the Random Object used to randomly choose a Chinese word
	private Random randomSeed;
	
	/**
	 * Default constructor
	 * 
	 * This reads the file storing words and save them in the list.
	 */
	public WordHandler(){
		String inputLine;
		this.wordList = new LinkedList<String>();
		
		try{
			this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(SourceFileName),"UTF-8"));
			while((inputLine=this.reader.readLine())!=null){
				String[] array = inputLine.split(",");
				for(int i=1;i<array.length;i++){
					this.wordList.add(array[i]);
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		this.randomSeed = new Random();
	}
	
	/**
	 * @return : the random word
	 * 
	 * This return a random word from the list of possible words.
	 * The chosen word would be removed from the list not to be chosen again next time.
	 */
	public String getChineseWord(){
		int index = this.randomSeed.nextInt(this.wordList.size());
		String result = this.wordList.get(index);
		this.wordList.remove(index);
		return result;
	}
}
