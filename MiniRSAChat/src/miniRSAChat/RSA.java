/**
 * 
 */
package miniRSAChat;

/**
 * @author Jie Qi
 * @version April 20, 2012
 * This class provides RSA algorithm implementation and encrypt/decrypt on it. 
 */
public class RSA {
	// Public key contains N and e
	public static class PublicKey {
		public final long N;
		public final long e;
		// Constructs a PublicKey
		PublicKey(long N, long e) {
			this.N = N;
			this.e = e;
		}
	}
	
	// Private key contains N and d
	public static class PrivateKey {
		public final long N;
		public final long d;
		// Constructs a PrivateKey
		PrivateKey(long N, long d) {
			this.N = N;
			this.d = d;
		}
	}

	// Key pair of public and private key
	public static class RSAKeyPair {
		public final PublicKey publicKey;
		public final PrivateKey privateKey;
		RSAKeyPair(PublicKey publicKey, PrivateKey privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}
	}
	
	// Encrpyt char to ciphertext with public key e in RSA algorithm
	static public long encryptChar(PublicKey publicKey, char ch) {
		long chValue = (long)ch;
		long resultValue = 1;
		// encryption in mode N
		for (int i = 0; i < publicKey.e; i++) {
			resultValue *= chValue;
			resultValue = resultValue % publicKey.N;
		}
		return resultValue;
	}
	
	// Encrypt plaintext string to ciphertext in char unit with public key e in RSA
	static public long[] encrpyt(PublicKey publicKey, String plaintext) {
		long[] ciphertext = new long[plaintext.length()];
		char[] plainChars = plaintext.toCharArray();
		// encryption char by char
		for (int i = 0; i < plainChars.length; i++) {
			ciphertext[i] = encryptChar(publicKey, plainChars[i]);
		}
		return ciphertext;
	}
	
	// Decrypt with private key in RSA
	static public char decrypt(PrivateKey privateKey, long ch) {
		long chValue = (long)ch;
		long resultValue = 1;
		for (int i = 0; i < privateKey.d; i++) {
			resultValue *= chValue;
			resultValue = resultValue % privateKey.N;
		}
		char result = (char)resultValue;
		return result;
	}
	
}
