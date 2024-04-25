package com.lorenzhoerb.kv.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class KeyValueSession implements IKeyValueSession {

    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    public KeyValueSession(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream());
        handleResponse();
    }

    private void handleResponse() {
        String status = in.nextLine();
        if (status.startsWith("ERROR")) throw new IllegalStateException("Session failed");
    }


    @Override
    public void put(String key, String value) {
        out.printf("PUT %s %s\n", key, value);
        out.flush();
        handleResponse();
    }

    @Override
    public HashMap<String, String> getAll() {
        out.printf("ALL\n");
        out.flush();

        HashMap<String, String> keyValues = new HashMap<>();
        while (true) {
            String line = in.nextLine();

            if (line.startsWith("ERROR:")) {
                throw new IllegalStateException("All failed: " + line.substring(line.charAt(':')));
            }

            if (line.equals("OK")) return keyValues;


            String[] keyValue = paresKeyValue(line);
            keyValues.put(keyValue[0], keyValue[1]);
        }
    }

    private String[] paresKeyValue(String firstIn) {
        var result = firstIn.split(":");
        result[0] = result[0].trim();
        result[1] = result[1].trim();
        return result;
    }

    @Override
    public String get(String key) {
        out.printf("GET %s\n", key);
        out.flush();
        String res = in.nextLine();
        if(res.startsWith("ERROR:")) return null;
        handleResponse();
        return res;
    }

    @Override
    public void clear() {
        out.println("CLEAR");
        out.flush();
        handleResponse();
    }

    @Override
    public void del(String key) {
        out.printf("DEL %s\n", key);
        out.flush();
        handleResponse();
    }
}
