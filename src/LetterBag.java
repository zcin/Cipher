import java.util.ArrayList;
 
public class LetterBag {
	private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.() '\"![]/%-_;?=:" + '\n' + '\r';
	private int[] letterFrequencies = new int[ALPHABET.length()];
	
	public LetterBag(){
	}
	
	public LetterBag(String str){
		for(int i = 0; i <  str.length(); i++)
			add(str.substring(i, i+1));
	}
	
	public void add(String letter){
		String lower = letter.toLowerCase();
		int index = getIndexForLetter(lower);
		letterFrequencies[index]++;
	}
	
	private int getIndexForLetter(String lower){
		return ALPHABET.indexOf(lower);
	}
	
	public void getTotalWords(){
 
	}
	
	public int getNumUniqueWords(){
		int count = 0;
		for(int i = 0; i <  letterFrequencies.length; i++)
			if(letterFrequencies[i] != 0) count++;
		return count;
	}
	
	public int getNumOccurrences(String letter){
		String lower = letter.toLowerCase();
		int index = getIndexForLetter(lower);
		return letterFrequencies[index];
	}
	
	public String getMostFrequent(){
		int max = 0;
		for(int i = 0; i <  letterFrequencies.length; i++)
			if(letterFrequencies[i] > letterFrequencies[max]) max = i;
		return ALPHABET.substring(max, max+1);
	}
}