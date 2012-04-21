package miniRSAChat;


/**
 * @author Jie Qi
 * @version April 20, 2012
 * This class provides RSA algorithm implementation and encrypt/decrypt on it. 
 */
/**
 * @author dianachih
 *
 */
/**
 * @author dianachih
 *
 */

public class RSA {
	/*
	 * Public Key class contains N, e
	 */
	public static class PublicKey {
		public final long N;
		public final long e;
		PublicKey(long N, long e) {
			this.N = N;
			this.e = e;
		}
	}
	
	/*
	 * Private Key class contains N, d
	 */
	public static class PrivateKey {
		public final long N;
		public final long d;
		PrivateKey(long N, long d) {
			this.N = N;
			this.d = d;
		}
	}
	
	/*
	 * Key Pair class
	 */
	public static class RSAKeyPair {
		public final PublicKey publicKey;
		public final PrivateKey privateKey;
		RSAKeyPair(PublicKey publicKey, PrivateKey privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}
	}
	
	static public long encrypt(PublicKey publicKey, char ch) {
		long chValue = (long)ch;
		long resultValue = 1;
		for (int i = 0; i < publicKey.e; i++) {
			resultValue *= chValue;
			resultValue = resultValue % publicKey.N;
		}
		return resultValue;
	}
	
	static public long[] encrypt(PublicKey publicKey, String cleartext) {
		long[] ciphertext = new long[cleartext.length()];
		char[] clearChars = cleartext.toCharArray();
		for (int i = 0; i < clearChars.length; i++) {
			ciphertext[i] = encrypt(publicKey, clearChars[i]);
		}
		return ciphertext;
	}
	
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
	
	static public String decrypt(PrivateKey privateKey, long[] ciphertext) {
		String cleartext = "";
		for (int i = 0; i < ciphertext.length; i++) {
			cleartext += String.valueOf(decrypt(privateKey, ciphertext[i]));
		}
		return cleartext;
	}
	
	static public String decrypt(PrivateKey privateKey, byte[] ciphertext) {
		long[] ciphertextLong = new long[ciphertext.length / 8];
		for (int i = 0; i < ciphertextLong.length; i++) {
			
			ciphertextLong[i] = (ciphertext[i * 8] & 0x00000000000000ffl) << 56 | 
								(ciphertext[i * 8 + 1] & 0x00000000000000ffl) << 48|
								(ciphertext[i * 8 + 2] & 0x00000000000000ffl) << 40|
								(ciphertext[i * 8 + 3] & 0x00000000000000ffl) << 32|
								(ciphertext[i * 8 + 4] & 0x00000000000000ffl) << 24|
								(ciphertext[i * 8 + 5] & 0x00000000000000ffl) << 16|
								(ciphertext[i * 8 + 6] & 0x00000000000000ffl) << 8|
								(ciphertext[i * 8 + 7] & 0x00000000000000ffl);
		}
		return decrypt(privateKey, ciphertextLong);
	}

	/*
	 * Get gcd of a and b, using Stein's binary gcd algorithm
	 * http://en.wikipedia.org/wiki/Binary_GCD_algorithm
	 */
	static long getGCD(long a, long b) {
		if (a == 0) {
			return b;
		}
		if (b == 0) {
			return a;
		}
		if (a == b) {
			return a;
		}
		if (a % 2 == 0 && b % 2 == 0) {
			return 2 * getGCD(a >> 1, b >> 1);
		}
		if (a % 2 == 0 && b % 2 != 0) {
			return getGCD(a >> 1, b);
		}
		if (a % 2 != 0 && b % 2 == 0) {
			return getGCD(a, b >> 1);
		}
		if (a % 2 != 0 && b % 2 != 0) {
			return getGCD(Math.min(a, b), Math.abs(a - b) >> 1);
		}
		return 1;
	}
	
	static public RSAKeyPair genKeyPair() {
		long N = 0;
		long e = 0;
		long d = 0;
		
		// Generate two different random numbers within primeThreshold
		int r1 = Global.rand.nextInt(Global.primeThreshold) + 1;
		int r2 = 0;
		do {
			r2 = Global.rand.nextInt(Global.primeThreshold) + 1;
 		}
		while (r2 == r1);
		
		// Get a pair of prime numbers
		long p = primeNumber(r1);
		long q = primeNumber(r2);
		
		N = p * q;
		long m = (p - 1) * (q - 1);
		
		// Randomly choose e which is 
		do {
			e = Math.abs(Global.rand.nextLong());
			e = e % m;
		} while (getGCD(e, m) != 1);

		int i = 1;
		while ((i * m + 1) % e != 0) {
			i++;
		}
		d = (i * m + 1) / e;

		RSAKeyPair newKey = new RSAKeyPair(new PublicKey(N, e), new PrivateKey(N, d));
		return newKey;
	}
	
	static public class Encryptor {
		private PublicKey publicKey = null;
		Encryptor(PublicKey publicKey) {
			this.publicKey = publicKey;
		}
		
		public byte[] encrypt(String cleartext) {
			long[] ciphertextLong = RSA.encrypt(publicKey, cleartext);
			byte[] ciphertext = new byte[ciphertextLong.length * 8];
			for (int i = 0; i < ciphertextLong.length; i++) {
				ciphertext[8 * i] = (byte)(ciphertextLong[i] >> 56);
				ciphertext[8 * i + 1] = (byte)(ciphertextLong[i] >> 48);
				ciphertext[8 * i + 2] = (byte)(ciphertextLong[i] >> 40);
				ciphertext[8 * i + 3] = (byte)(ciphertextLong[i] >> 32);
				ciphertext[8 * i + 4] = (byte)(ciphertextLong[i] >> 24);
				ciphertext[8 * i + 5] = (byte)(ciphertextLong[i] >> 16);
				ciphertext[8 * i + 6] = (byte)(ciphertextLong[i] >> 8);
				ciphertext[8 * i + 7] = (byte)(ciphertextLong[i]);
			}
			return ciphertext;
		}
		public long encrypt(char ch) {
			return RSA.encrypt(publicKey, ch);
		}
	}
	
	static public class Decryptor {
		private PrivateKey privateKey = null;
		Decryptor(PrivateKey privateKey) {
			this.privateKey = privateKey;
		}
		
		public String decrypt(long[] ciphertext) {
			return RSA.decrypt(privateKey, ciphertext);
		}
		
		public String decrypt(byte[] ciphertext) {
			return RSA.decrypt(privateKey, ciphertext);
		}
		
		public char decrypt(long ch) {
			return RSA.decrypt(privateKey, ch);
		}
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
	
	// Get the prime number in the position of given index
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
	

