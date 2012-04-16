import java.io.*;
import java.net.*;

class SingleClient implements Runnable {
    
	static Socket clientSocket = null;
	static PrintStream os = null;
	static BufferedReader is = null;
	static BufferedReader inputLine = null;
	static boolean closed = false;
    
	public static void main(String[] args) {
        
		int port_number = 1234;
		String host = "localhost";
        
		try {
			clientSocket = new Socket(host, port_number);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(clientSocket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(
                                                          clientSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err
            .println("Couldn't get I/O for the connection to the host "
                     + host);
		}
        
		if (clientSocket != null && os != null && is != null) {
			try {
                
				// Create a thread to read from the server
                
				new Thread(new SingleClient()).start();
                
				while (!closed) {
					System.out.print("Client: ");
					String message = inputLine.readLine();
					os.println(message);
					if (message.startsWith(".quit"))
						break;
				}
                
				os.close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}
    
	public void run() {
		String responseLine;
        
		try {
			while ((responseLine = is.readLine()) != null) {
				System.out.println("\nServer: " + responseLine);
			}
			closed = true;
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}