package miniRSAChat;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Jiuzheng Chen, Jie Qi
 * @version April 20, 2012
 * This class implements operations of chat server.
 */
class ChatServer implements Runnable {
	static ServerSocket serverSocket = null;
	static Socket clientSocket = null;
	static PrintStream os = null;
	static BufferedReader is = null;
	static BufferedReader inputLine = null;
	static boolean closed = false;
	static RSA.RSAKeyPair keys;
	
	/**
	 * Main method in ChatServer class.
	 * @param args
	 */
	public static void main(String args[]) {
        // Code added for RSA.
		Global.rand = new Random();
		keys = RSA.genKeyPair();
		System.out.println("Public key of Sever is: " + String.valueOf(keys.publicKey.e)
				           + " " + String.valueOf(keys.publicKey.N));
		System.out.println("Please enter the client's public key: ");
		Scanner scan = new Scanner(System.in);
		long rsaN = 0;
		long rsaE = 0;

		// Read in Client's public keys
		try {
			rsaE = scan.nextLong();
			rsaN = scan.nextLong();
		}
		catch (Exception ex) {
			System.out.print(ex);
		}
		RSA.PublicKey serverKey = new RSA.PublicKey(rsaN, rsaE);
		RSA.Encryptor encryptor = new RSA.Encryptor(serverKey);
		// End of RSA.
				
		try {
			serverSocket = new ServerSocket(1234);
			System.out.println("Waiting for connection...");
			clientSocket = serverSocket.accept();
			System.out.print("Server has connected!\n");
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(clientSocket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
		} catch (Exception e) {
			System.err.print("Exception:  " + e);
		}
		
		if (clientSocket != null && os != null && is != null) {
			try {
                
				// Create a thread to read from the client
                
				new Thread(new ChatServer()).start();
                
				while (!closed) {
					System.out.print("Server: ");
					String message = inputLine.readLine();
					// Encrypt
					byte[] ciphertext = encryptor.encrypt(message);
					os.write(ciphertext);
					// End of modification.
					if (message.startsWith(".quit")) break;
					if (message.startsWith(".brute force")) {
						RSA.PrivateKey crackedOwnKey = RSA.bruteForce(keys.publicKey);
				        System.out.println("The server's private key is: " + String.valueOf(keys.privateKey.d) + " " +
								 String.valueOf(keys.privateKey.N));
				        System.out.println("You cracked your own key!: " + String.valueOf(crackedOwnKey.d) + " " +
								 String.valueOf(crackedOwnKey.N));
				        
				        RSA.PrivateKey crackedClientKey = RSA.bruteForce(serverKey);
				        System.out.println("You cracked your client's key!: " + String.valueOf(crackedClientKey.d) + " " +
								 String.valueOf(crackedClientKey.N));
					}
				}
				os.close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}
    
	/**
	 * Thread calls run() asynchronously
	 */
	@Override
	public void run() {
		int bufferSize = 1024;
		byte[] responseBuffer = new byte[bufferSize];
		RSA.Decryptor decryptor = new RSA.Decryptor(keys.privateKey);
        int responseSize = 0;
		try {
			while ((responseSize = clientSocket.getInputStream().read(responseBuffer)) != 0) {
				byte[] response = new byte[responseSize];
				System.arraycopy(responseBuffer, 0, response, 0, responseSize);
				String plaintext = decryptor.decrypt(response);
				System.out.println("\nClient: " + plaintext);
                System.out.print("Server: ");
				if (plaintext.startsWith(".quit")) {
					break;
				}
			}
			closed = true;
		} catch (IOException e) {
			//System.err.println("IOException:  " + e);
		}
	}
}