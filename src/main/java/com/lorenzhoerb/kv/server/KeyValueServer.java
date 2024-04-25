package com.lorenzhoerb.kv.server;

import com.lorenzhoerb.kv.server.clients.BasicClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KeyValueServer extends Thread {

    private final int port;
    private boolean running = true;

    public KeyValueServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server is running and waiting for connections...");
            while (running) {
                Socket clientSocket = socket.accept();
                new Thread(new BasicClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}