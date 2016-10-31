import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class Cipher {

	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.() '\"![]/%-_;?=:" + '\n' + '\r';
	private static final String SIMPLE_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	// Set this variable to the default alphabet you wish to use
	private static final String DEFAULT_ALPHABET = ALPHABET;
	
	private static final Dictionary d = Dictionary.buildDictionary("words.txt");
	
	public static String shiftCharacter(String letter, int shift, String alphabet){
		if(shift > alphabet.length()) shift = shift % alphabet.length();
		else if (shift < 0) shift = alphabet.length() - Math.abs(shift)%alphabet.length();

		int index = alphabet.indexOf(letter);
		int start = (index+shift)%alphabet.length();
		int end = (index+shift+1)%alphabet.length();
		if(end == 0) end = alphabet.length();
		return alphabet.substring(start, end);
	}
	
	public static String rotationCipherEncrypt(String plainText, int shift, String alphabet) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < plainText.length(); i++){
			String letter = plainText.substring(i, i+1);
			String shiftedLetter = shiftCharacter(letter, shift, alphabet);
			output.append(shiftedLetter);
		}
		return output.toString();
	}
 
	/**
	 * Returns a the result of decrypting cipherText by shiftAmount using the rotation cipher.
	 * @param alphabet the alphabet to be used for the encryption
	 * @param cipherText the encrypted text.
	 * @param shiftAmount the key to decrypt the cipher.
	 * @return returns the decrypted cipherText.
	 */
	public static String rotationCipherDecrypt(String cipherText, int shiftAmount, String alphabet) {
		return rotationCipherEncrypt(cipherText, -shiftAmount, alphabet);
	}
 
	/**
	 * Returns plaintext encrypted by the vigenere cipher encoded with the String code
	 * @param alphabet the alphabet to be used for the encryption
	 * @param plainText the plain text to be encrypted.
	 * @param code the code to use as the encryption key.
	 * @return returns the encrypted plainText.
	 */
	public static String vigenereCipherEncrypt(String plainText, String code, String alphabet) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < plainText.length(); i++){
			int shift = alphabet.indexOf(code.substring(i%code.length(), i%code.length()+1));
			String letter = plainText.substring(i, i+1);
			String shiftedLetter = shiftCharacter(letter, shift, alphabet);
			output.append(shiftedLetter);
		}
		return output.toString();
	}
 
	/**
	 * Returns the result of decrypting cipherText with the key code.
	 * @param alphabet the alphabet to be used for the encryption
	 * @param cipherText the encrypted text.
	 * @param code the decryption key
	 * @return returns the decrypted cipherText.
	 */
	public static String vigenereCipherDecrypt(String cipherText, String code, String alphabet) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < cipherText.length(); i++){
			int shift = -alphabet.indexOf(code.substring(i%code.length(), i%code.length()+1));
			String letter = cipherText.substring(i, i+1);
			String shiftedLetter = shiftCharacter(letter, shift, alphabet);
			output.append(shiftedLetter);
		}
		return output.toString();
	}
	
	public static String rotationCipherCrack(String cipher, String alphabet){
		for(int i = 0; i < alphabet.length(); i++){
			String decrypted = Cipher.rotationCipherDecrypt(cipher, i, alphabet);
			if(isEnglish(decrypted)) return decrypted;
		}
		return "unable to decrypt through rotation cipher";
	}
	
	public static String[] getWords(String str){
		ArrayList<Integer> spaces = new ArrayList<Integer>();
		str = str.trim();
		spaces.add((Integer)(-1));
		
		for(int i = 1; i < str.length(); i++){
			String previousCharacter = str.substring(i-1, i);
			String character = str.substring(i, i+1);
			if(character.equals(" ") && !previousCharacter.equals(" "))
				spaces.add((Integer)i);
		}
		
		String[] output = new String[spaces.size()];
		int i = 0;
		while(i < spaces.size()-1)
			output[i] = str.substring(spaces.get(i)+1, spaces.get(++i));
		output[i] = str.substring(spaces.get(i)+1, str.length());
		return output;
	}

	/**
	 * returns true if plaintext is valid English.
	 * 
	 * @param plaintext
	 *          the text you wish to test for whether it's valid English
	 * @return boolean returns true if plaintext is valid English.
	 */
	public static boolean isEnglish(String plaintext) {
		String[] words = getWords(plaintext);
		int englishWords = 0;
		for(int i = 0; i <  words.length; i++){
			if(d.isWord(words[i])) englishWords++;
		}
		if((double)(englishWords)/(double)(words.length) > 0.7) return true;
		return false;
	}
	
	public static String vigenereCipherCrackThreeLetter(String cipher, String alphabet){
		return vigenereCipherCrack(cipher, 3, alphabet);
	}
	
	public static String vigenereCipherCrack(String cipher, String alphabet){
		double minScore = 1000;
		String best = "";
		for(int i = 2; i < 50; i++){
			String decrypted = vigenereCipherCrack(cipher, i, alphabet);
			LetterBag b = new LetterBag(decrypted);
			double score = similarityToEnglish(b, decrypted.length(), alphabet);
			if(Math.abs(score) < minScore){
				minScore = Math.abs(score);
				best = decrypted;
			}
		}
		return best;
	}
	
	public static String vigenereCipherCrack(String cipher, int passwordLength, String alphabet){
		String[] groups = new String[passwordLength];
		StringBuilder code = new StringBuilder();
		String decrypted = cipher;
		
		for(int i = 0; i < passwordLength; i++)
			groups[i] = getGroup(cipher, i, passwordLength);
		for(int i = 0; i < passwordLength; i++)
			code.append(findCodeLetter(groups[i], alphabet, i));
				
		decrypted = vigenereCipherDecrypt(cipher, code.toString(), alphabet);
		return decrypted;
	}
	
	public static String getGroup(String cipherText, int index, int codeLength){
		StringBuilder g = new StringBuilder();
		for(int i = 0; i+index < cipherText.length(); i+=codeLength){
			g.append(cipherText.substring(i+index, i+index+1));
		}
		return g.toString();
	}
	
	public static String findCodeLetter(String group, String alphabet, int num){
		double minScore = 500;
		int bestShift = 0;
		for(int shift = 0; shift < alphabet.length(); shift++){
			String rotated = rotationCipherDecrypt(group.toString(), shift, alphabet);
			LetterBag b = new LetterBag(rotated);
			String letter = alphabet.substring(shift, shift+1);
			double score = similarityToEnglish(b, rotated.length(), alphabet);
			if(Math.abs(score) < minScore && isLetter(letter, alphabet)){
				minScore = Math.abs(score);
				bestShift = shift;
			}
		}
		return alphabet.substring(bestShift, bestShift+1);
	}
	
	public static boolean isLetter(String l, String alphabet){
		int index = alphabet.indexOf(l);
		if(index < 52)return true;
		return false;
	}
	
	public static double similarityToEnglish(LetterBag b, int stringLength, String alphabet){
		double score = 0;
		double[] englishFrequency = {8.17, 1.49, 2.78, 4.25, 12.7, 2.23, 2.01, 6.09, 6.97, 0.15, 0.77, 4.03, 2.40, 6.75, 7.50, 1.93, 0.09, 5.99, 6.33, 9.1, 2.8, 0.98, 2.36, 0.15, 1.98, 0.07};
		score += (18.3 - getFrequency(b, " ", stringLength));
		for(int i = 0; i < 26; i++){
			String letter = alphabet.substring(i, i+1);
			double diff = (englishFrequency[i] - getFrequency(b, letter, stringLength));
			score += diff;
		}
		return score;
	}
 
	public static double getFrequency(LetterBag b, String letter, int stringLength){
		return (double)b.getNumOccurrences(letter)/(double)stringLength * 100.0;
	}
}
