/*
 * We deal with socket programming based on the client-server model.
 * Server side is the passive socket which will first be created
 * and wait for the client to connect.
 * Client side is the active socket which will be created as needed
 * and connect to the available server socket.
 */
package echoserver;
import java.net.*; // network package
import java.io.*; // IO package
import java.util.Random;

/**
 *
 * @author yizhu
 */
public class EchoServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345);
            // server socket with port #.
            // 1) JVM will create the server socket with given port number and the current IP address of the server.
            // 2) JVM will bind the port with the IP address. binding procedure
            // 3) sever socket has been done and listen to the port.
            // 4) server socket will wait until a client socket connection is comming.
            System.out.println("The server socket is created!");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 12345.");
            System.exit(1);
        }

        Socket connectionSocket = null;
        System.out.println ("Waiting for connection.....");
        // We have created a server socket with port # 12345 and now the server socket is waiting for connection.

        try {
            connectionSocket = serverSocket.accept();
            // server socket accept the client connection and create an variable to indicate the client.
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        System.out.println ("Connection successful");
        System.out.println ("Waiting for input.....");

        PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        String inputLine;
        boolean done = false;
        Random random = new Random();   // create random object
        int x = random.nextInt(900)+100;    // generate random 3 digit number
        String num = String.valueOf(x); // convert random number to string
        while ((inputLine = in.readLine()) != null) {
            System.out.println ("Server: " + inputLine);
            out.println(inputLine);
            String fileName = "Server" + num + ".txt";   // concatenate Server with num string numbers
            InputStream is = connectionSocket.getInputStream(); //create inputstream object
            FileOutputStream fo = new FileOutputStream("src/serverFiles/"+fileName);
            System.out.println("Creating file: " + fileName);
            byte[] fb = new byte[100];  // create byte array object
            is.read(fb,0, fb.length);   // read file from stream
            System.out.println("Reading file...");
            fo.write(fb,0, fb.length);  // write file to outputstream
            long size = (long)fileName.length();    // size of file in bytes
            System.out.println("Your file named " + fileName + " with the size " + size + " bytes has been uploaded" +
                    " correctly.");
            if (inputLine.equalsIgnoreCase("Bye"))
                break;
        }
        out.close();
        in.close();
        connectionSocket.close();
        serverSocket.close();
    }
}
