import java.lang.*;
import java.io.*;
import java.net.*;

class Client {
   public static void main(String args[]) {
      try {
         Socket clientSocket = new Socket("localhost", 1234);
         BufferedReader inReader = new BufferedReader(new
            InputStreamReader(clientSocket.getInputStream()));
         System.out.print("Received string: ");

         while (!inReader.ready()) {}
         System.out.println(inReader.readLine()); // Read one line and output it

         System.out.print("\n");
         inReader.close();
      }
      catch(Exception e) {
         System.out.print("Whoops! It didn't work! Please make sure the server runs first.\n");
      }
   }
}