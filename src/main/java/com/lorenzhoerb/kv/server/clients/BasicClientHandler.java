package com.lorenzhoerb.kv.server.clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class BasicClientHandler implements IClientHandler {
    private final Socket socket;
    private final static String PUT_COMMAND = "PUT";
    private final static String ALL_COMMAND = "ALL";
    private final static String CLEAR_COMMAND = "CLEAR";
    private final static String GET_COMMAND = "GET";
    private final static String DEL_COMMAND = "DEL";
    private final static String OK_RES = "OK";
    private final ConcurrentHashMap<String, String> keyValue = new ConcurrentHashMap<>();

    public BasicClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void handle(InputStream in, OutputStream out) {
        Scanner inScanner = new Scanner(in);
        PrintWriter outWriter = new PrintWriter(out);

        outWriter.println(OK_RES);
        outWriter.flush();

        while (inScanner.hasNextLine()) {
            try {
                handleCommandLine(inScanner, outWriter);
                outWriter.println(OK_RES);
            } catch (IllegalArgumentException ex) {
                outWriter.println("ERROR: " + ex.getMessage());
                inScanner.nextLine();
            } finally {
                outWriter.flush();
            }
        }
    }

    private void handleCommandLine(Scanner scanner, PrintWriter writer) {
        if(!scanner.hasNext()) return;
        String command = scanner.next().toUpperCase();
        System.out.printf("handleCommand(%s)\n", command);
        switch (command) {
            case PUT_COMMAND -> keyValue.put(scanner.next(), scanner.next());
            case ALL_COMMAND -> sendStore(writer);
            case CLEAR_COMMAND -> keyValue.clear();
            case DEL_COMMAND -> handleDelete(scanner.next());
            case GET_COMMAND -> handleGet(scanner.next(), writer);
            default -> throw new IllegalArgumentException("Invalid command: '" + command + "'");
        }
    }

    private void handleDelete(String key) {
        keyValue.remove(key);
    }


    private void handleGet(String key, PrintWriter writer) {
        if(keyValue.containsKey(key)) {
            writer.println(keyValue.get(key));
        } else {
            throw new IllegalArgumentException("Couldn't get value of key '" + key + "'");
        }
    }

    private void sendStore(PrintWriter writer) {
        keyValue.forEach((key, value) -> writer.printf("%s: %s\n", key, value));
    }

    @Override
    public void run() {
        try {
            handle(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Ensure the socket is closed after handling the exception
            try {
                socket.close();
            } catch (IOException ex) {
                // Handle socket closing error if needed
                ex.printStackTrace();
            }
        }
    }
}
