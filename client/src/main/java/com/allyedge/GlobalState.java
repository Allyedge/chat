package com.allyedge;

import org.java_websocket.client.WebSocketClient;

public class GlobalState {
  private static GlobalState instance = null;

  private String username = null;
  private String room = null;
  private WebSocketClient webSocketClient = null;

  private GlobalState() {
  }

  public static synchronized GlobalState getInstance() {
    if (instance == null) {
      instance = new GlobalState();
    }

    return instance;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public WebSocketClient getWebSocketClient() {
    return webSocketClient;
  }

  public void setWebSocketClient(WebSocketClient webSocketClient) {
    this.webSocketClient = webSocketClient;
  }
}
