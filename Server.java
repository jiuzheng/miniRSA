import java.lang.*;
import java.io.*;
import java.net.*;

class Server {
   public static void main(String args[]) {
      String data = "Meow meow!";
      try {
         ServerSocket serverSocket = new ServerSocket(1234);
         Socket clientSocket = serverSocket.accept();
         System.out.print("Server has connected!\n");
         PrintWriter outWriter = new PrintWriter(clientSocket.getOutputStream(), true);
         System.out.print("Sending string: '" + data + "'\n");
         outWriter.print(data);
         outWriter.close();
         clientSocket.close();
         serverSocket.close();
      }
      catch(Exception e) {
         System.out.print("Whoops! It didn't work!\n");
      }
   }
}