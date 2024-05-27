package com.allyedge;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javafx.application.Platform;

public class ChatClient extends WebSocketClient {
  private final ChatController chatController;

  public ChatClient(String serverUri, ChatController controller) throws Exception {
    super(new URI(serverUri));

    this.chatController = controller;
  }

  @Override
  public void onOpen(ServerHandshake handshake) {
    System.out.println("Connected to server.");
  }

  @Override
  public void onMessage(String message) {
    Platform.runLater(() -> {
      chatController.addReceivedMessage(message);
    });
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("Connection closed.");

    Platform.runLater(() -> {
      chatController.addReceivedMessage("Connection closed.");
    });
  }

  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();
  }
}
