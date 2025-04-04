package es.udc.redes.webserver;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket;
    private Path basePath;

    public ServerThread(Socket socket, Path basePath) {
        this.socket = socket;
        this.basePath = basePath;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || !requestLine.startsWith("GET") && !requestLine.startsWith("HEAD")) {
                sendError(out, 400, "Bad Request");
                return;
            }

            StringTokenizer tokens = new StringTokenizer(requestLine);
            String method = tokens.nextToken();
            String resource = tokens.nextToken();

            Path filePath = basePath.resolve(resource.substring(1)).normalize();
            if (!filePath.startsWith(basePath) || !Files.exists(filePath)) {
                sendError(out, 404, "Not Found");
                return;
            }

            sendResponse(out, filePath, method.equals("HEAD"));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void sendResponse(OutputStream out, Path filePath, boolean headOnly) throws IOException {
        String contentType = Files.probeContentType(filePath);
        long fileSize = Files.size(filePath);
        String lastModified = Files.getLastModifiedTime(filePath).toString();

        String header = "HTTP/1.0 200 OK\r\n" +
                "Date: " + getServerTime() + "\r\n" +
                "Server: MySimpleServer\r\n" +
                "Content-Length: " + fileSize + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Last-Modified: " + lastModified + "\r\n\r\n";
        out.write(header.getBytes());

        if (!headOnly) {
            Files.copy(filePath, out);
        }
    }

    private void sendError(OutputStream out, int code, String message) throws IOException {
        String header = "HTTP/1.0 " + code + " " + message + "\r\n" +
                "Date: " + getServerTime() + "\r\n" +
                "Server: serverFonsi\r\n\r\n";
        out.write(header.getBytes());
    }

    private String getServerTime() {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
    }
}