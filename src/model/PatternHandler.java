package model;

import json.*;

import java.io.*;
import java.util.*;

public class PatternHandler {
	//the directory path of files storing patterns
	private static final String DIRECTORY_PATH = GameProtocol.PATTERN_DIR;
	//the extension of the file
	private static final String FILE_EXTENSION = ".txt";
	
	//the name of the pattern attribute in the 'pattern' object
	private static final String PATTERN = "Pattern";
	//the name of the count attribute in the 'pattern' object
	private static final String COUNT = "Count";
	
	/*
	 * Default Constructor
	 */
	public PatternHandler(){
	}
	
	/**
	 * @param patternToSave : the array recording the pattern of a specific word
	 * @param chineseWord 	: the specific word
	 * 
	 * This save the pattern of a specific word to a file.
	 * The pattern is saved in the file called "$word./txt", where $word represents the word
	 * When a pattern is to be saved, all patterns and their counts are saved in the file would be searched.
	 * If the pattern is new to those in the file, write it in the file.
	 * Otherwise renew its count.
	 */
	public void savePattern(int[] patternToSave, String chineseWord){
		final String filename = DIRECTORY_PATH + chineseWord + FILE_EXTENSION;
		HashMap<Integer,int[]> patternList;
		HashMap<Integer,Integer> patternCountList;
		
		//if the file does not exist, create one
		try{
			File file = new File(filename);
			if(file.exists()==false)
				file.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		patternList = new HashMap<Integer,int[]>();
		patternCountList = new HashMap<Integer,Integer>();
		getPatternByWord(chineseWord,patternList,patternCountList);
		
		//save pattern
		boolean hasPatternToSaveExisted = false;
		for(int i=0;i<patternList.size();i++){
			if(isPatternEqual(patternToSave,patternList.get(i))==true){
				patternCountList.put(i, patternCountList.get(i)+1);
				hasPatternToSaveExisted = true;
			}
		}
		if(hasPatternToSaveExisted==false){
			patternList.put(patternList.size(), patternToSave);
			patternCountList.put(patternCountList.size(), new Integer(1));
		}
		
		JSONObject[] jsonList = new JSONObject[patternList.size()];
		for(int i=0;i<patternList.size();i++){
			jsonList[i] = new JSONObject();
			jsonList[i].put(PATTERN, patternList.get(i));
			jsonList[i].put(COUNT, patternCountList.get(i));
		}
		JSONObject output = new JSONObject();
		output.put(chineseWord, jsonList);
		
		//write the file back
		try{
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename,false),"UTF-8"));
			writer.write(output.toString());
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param word : the specific word
	 * @param patternList : the hashmap storing patterns of the specific word
	 * @param patternCountList : the hashmap storing counts of patterns
	 * 
	 *  This reads all patterns and counts of a specific word, and store them in the lists.
	 *  The pattern and the count is stored in order of their order saved in the file.
	 */
	public void getPatternByWord(String word,HashMap<Integer,int[]> patternList,HashMap<Integer,Integer> patternCountList){
		String line;
		JSONObject json;
		StringBuilder input = new StringBuilder();
		
		//Read the file storing pattern of the given word 
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(
									new FileInputStream(DIRECTORY_PATH + word + FILE_EXTENSION),"UTF-8"));
			while((line=reader.readLine())!=null)
				input.append(line);
			reader.close();
		}
		catch(IOException e){
			System.out.println("No patterns of the word" + word + "are found");
		}
		
		//if no contents, we return immediately, otherwise keep going
		String jsonString=input.toString();
		System.out.println(jsonString);
		if(jsonString.equals(""))
			return;
		
		//get rid of all white space characters
		jsonString.replaceAll("[\\s]", "");
		json = new JSONObject(jsonString);
		//get array of patterns
		JSONArray patternArray = (JSONArray) json.get(word);
		

		//reset list of patterns and count of patterns
		if(patternList==null)
			patternList = new HashMap<Integer,int[]>();
		else
			patternList.clear();		
		if(patternCountList==null)
			patternCountList = new HashMap<Integer,Integer>();
		else
			patternCountList.clear();
		
		//parse the array of patterns and store them to lists
		for(int i=0;i<patternArray.length();i++){
			JSONArray pattern = (JSONArray) patternArray.getJSONObject(i).get(PATTERN);
			int[] array = new int[pattern.length()];
			for(int j=0;j<pattern.length();j++){
				array[j]=pattern.getInt(j);
			}
			patternList.put(i,array);
			
			int count = Integer.parseInt(patternArray.getJSONObject(i).get(COUNT).toString());
			patternCountList.put(i,count);
		}
	}
	
	/**
	 * @param src : the pattern needed to be compared
	 * @param ref : the reference pattern
	 * @return	  : true if 2 patterns are equal, false otherwise.
	 */
	private boolean isPatternEqual(int[] src, int[] ref){
		if(src.length!=ref.length)
			return false;
		for(int i=0;i<src.length;i++){
			if(src[i]!=ref[i])
				return false;
		}
		return true;
	}
	
	/*public static void main(String args[]){
		String str = "字";
		int[] array1 = {0,0,0,0,1,0,0,0,0};
		int[] array2 = {1,0,0,0,1,0,0,0,0};
		int[] array3 = {0,0,0,0,1,0,0,0,0};
		
		PatternHandler handler = new PatternHandler();
		handler.savePattern(array1, str);
		handler.savePattern(array2, str);
		handler.savePattern(array3, str);
	}*/
}
