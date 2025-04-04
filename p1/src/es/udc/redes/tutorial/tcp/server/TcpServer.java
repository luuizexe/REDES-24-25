package es.udc.redes.tutorial.tcp.server;

import java.io.IOException;
import java.net.*;

/**
 * Multithread TCP echo server.
 */

public class TcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }
        ServerSocket socket = null;
        try {
            int serverPort = Integer.parseInt(argv[0]);
            // Create a server socket
            socket = new ServerSocket(serverPort);
            // Set a timeout of 300 secs
            socket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket comSocket = socket.accept();
                // Create a ServerThread object, with the new connection as parameter
                ServerThread thread = new ServerThread(comSocket);
                // Initiate thread using the start() method
                thread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
