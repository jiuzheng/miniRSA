import java.io.*;
import java.net.*;

class SingleServer implements Runnable {
	
	static ServerSocket serverSocket = null;
	static Socket clientSocket = null;
	static PrintStream os = null;
	static BufferedReader is = null;
	static BufferedReader inputLine = null;
	static boolean closed = false;
	
	public static void main(String args[]) {
        
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
                
				new Thread(new SingleServer()).start();
                
				while (!closed) {
					System.out.print("Server: ");
					String message = inputLine.readLine();
					os.println(message);
					if(message.startsWith(".quit")) break;
				}
                
				os.close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}
    
	@Override
	public void run() {
		String responseLine;
        
		try {
			while ((responseLine = is.readLine()) != null) {
				System.out.println("\nClient: " + responseLine);
			}
			closed = true;
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}