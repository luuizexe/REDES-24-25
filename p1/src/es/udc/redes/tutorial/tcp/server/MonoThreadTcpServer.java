package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {
    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
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
                // Set the input channel
                BufferedReader sInput = new BufferedReader(new InputStreamReader(
                        comSocket.getInputStream()));
                // Set the output channel
                PrintWriter sOutput = new PrintWriter(comSocket.getOutputStream(), true);
                // Receive the client message
                String received = sInput.readLine();
                System.out.println("SERVER: Received " + received
                        + " from " + comSocket.getInetAddress().toString()
                        + ":" + comSocket.getPort());
                // Send response to the client
                System.out.println("SERVER: Sending " + received +
                        " to " + comSocket.getInetAddress().toString() +
                        ":" + comSocket.getPort());
                sOutput.println(received);
                // Close the streams
                sInput.close();
                sOutput.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
