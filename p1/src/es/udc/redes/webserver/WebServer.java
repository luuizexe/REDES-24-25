package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public class WebServer {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java WebServer <port> <directory>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        String basePath = args[1];

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket, Paths.get(basePath)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
