import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class CipherTest {

	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.() '\"![]/%-_;?=:" + '\n' + '\r';
	
	@Test
	public void crackVigenere1() {
		String plaintext = readFileAsString("//home//cindy//workspace//APCS//Tide//CipherCode//text.txt");
		String ciphertext = Cipher.vigenereCipherEncrypt(plaintext, "code", ALPHABET);
		String testDecrypted = Cipher.vigenereCipherCrack(ciphertext, 4, ALPHABET);
		assertEquals(testDecrypted, plaintext);
	}
	
	@Test
	public void crackVigenere2() {
		String plaintext = readFileAsString("//home//cindy//workspace//APCS//Tide//CipherCode//text.txt");
		String ciphertext = Cipher.vigenereCipherEncrypt(plaintext, "code", ALPHABET);
		String testDecrypted = Cipher.vigenereCipherCrack(ciphertext, ALPHABET);
		assertEquals(testDecrypted, plaintext);
	}
	
	@Test
	public void crackRotation1() {
		String plaintext = readFileAsString("//home//cindy//workspace//APCS//Tide//CipherCode//text.txt");
		String correctCipherText = Cipher.rotationCipherEncrypt(plaintext, 6, ALPHABET);
		String testCipherText = Cipher.rotationCipherCrack(correctCipherText, ALPHABET);
		assertEquals(testCipherText, plaintext);
	}
	
	public static String readFileAsString(String filepath){
		StringBuilder output = new StringBuilder();
		try (Scanner scanner = new Scanner (new File(filepath))){
			while (scanner.hasNext()){
				String line=scanner.nextLine();
				output.append(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return output.toString();
	}
}
