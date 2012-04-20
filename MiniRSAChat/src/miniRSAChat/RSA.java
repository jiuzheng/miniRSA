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
	
	// Encrpyt char plaintext to long ciphertext with public key e in RSA algorithm
	public static long encryptChar(PublicKey publicKey, char ch) {
		long chValue = (long)ch;
		long resultValue = 1;
		// encryption in mode N
		for (int i = 0; i < publicKey.e; i++) {
			resultValue *= chValue;
			resultValue = resultValue % publicKey.N;
		}
		return resultValue;
	}
	
	// Encrypt plaintext string to long ciphertext with public key e in RSA
	public static long[] encrpyt(PublicKey publicKey, String plaintext) {
		long[] ciphertext = new long[plaintext.length()];
		char[] plainChars = plaintext.toCharArray();
		// encryption char by char
		for (int i = 0; i < plainChars.length; i++) {
			ciphertext[i] = encryptChar(publicKey, plainChars[i]);
		}
		return ciphertext;
	}
	
	// Decrypt long ciphertext to plaintext char with private key in RSA
	public static char decrypt(PrivateKey privateKey, long ciphertext) {
		long cipherValue = ciphertext;
		long resultValue = 1;
		for (int i = 0; i < privateKey.d; i++) {
			resultValue *= cipherValue;
			resultValue = resultValue % privateKey.N;
		}
		char result = (char)resultValue;
		return result;
	}
	
	// Decrypt long array of ciphertext to string plaintext with private key in RSA
	public static String decrypt(PrivateKey privateKey, long[] ciphertext) {
		String plaintext = "";
		for (int i = 0; i < ciphertext.length; i++) {
			plaintext += String.valueOf(decrypt(privateKey, ciphertext));
		}
		return plaintext;
	}
	
	// Transferred message is in byte, decrypt it in byte with RSA
	public static String decrypt(PrivateKey privateKey, byte[] ciphertext) {
		long[] ciphertextLong = new long[ciphertext.length / 8];
		for (int i = 0; i < ciphertextLong.length; i++) {
			ciphertextLong[i] = (ciphertext[i * 8] & 0x00000000000000ffl) << 56 |
					            (ciphertext[i * 8 + 1] & 0x00000000000000ffl) << 48|
					            (ciphertext[i * 8 + 2] & 0x00000000000000ffl) << 40 |
					            (ciphertext[i * 8 + 3] & 0x00000000000000ffl) << 32 |
					            (ciphertext[i * 8 + 4] & 0x00000000000000ffl) << 24 |
					            (ciphertext[i * 8 + 5] & 0x00000000000000ffl) << 16 |
					            (ciphertext[i * 8 + 6] & 0x00000000000000ffl) << 8 |
					            (ciphertext[i * 8 + 7] & 0x00000000000000ffl);
		}
		return decrypt(privateKey, ciphertextLong);
	}
	
	// Check if a number is a prime number
	private static boolean isPrime(long number) {
		if (number % 2 == 0) return false;
		for (long i = 3; i < Math.sqrt(number); i+= 2) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	// Get the prime numer in the position of given index
	private static long primeNumber(int index) {
		long primeNumber = 1;
		long count = 0;
		while (count < index) {
			primeNumber++;
			if (isPrime(primeNumber)) {
				count++;
			}
		}
		return primeNumber;
	}
	
	// Crack a private key by inputting its public key
	public static PrivateKey bruteForce(PublicKey publicKey) {
		
		long N = publicKey.N;
		long e = publicKey.e;
		PrivateKey privateKey = null;
		Encryptor encryptor = new Encryptor(publicKey);
		Decryptor decryptor;
		
		int iteration = 1;
		
		while(iteration < N) {
			iteration++;
			long prime = primeNumber(iteration);
			if(N % prime == 0) {
				long p = prime;
				long q = N / prime;
				long m = (p - 1) * (q - 1);
				int i = 1;
				while ((i * m + 1) % e != 0) {
					i++;
				}
				long d = (i * m + 1) / e;
				privateKey = new PrivateKey(N, d);
				decryptor = new Decryptor(privateKey);
				if("Hello!".equals(decryptor.decrypt(encryptor.encrypt("Hello!")))){
					break;
				}
			}
		}
		
		return privateKey;
	}
}
