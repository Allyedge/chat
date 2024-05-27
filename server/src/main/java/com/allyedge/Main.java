package com.allyedge;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 4000;

        ChatServer server = new ChatServer(port);
        server.start();

        System.out.println("ChatServer started on port: " + server.getPort());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    server.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("ChatServer stopped.");
            }
        });
    }
}