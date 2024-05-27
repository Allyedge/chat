package com.allyedge;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {
  private final Map<String, Set<WebSocket>> rooms = new HashMap<>();
  private final Map<WebSocket, String> usernames = new HashMap<>();

  public ChatServer(int port) throws UnknownHostException {
    super(new InetSocketAddress(port));
  }

  public ChatServer(InetSocketAddress address) {
    super(address);
  }

  public ChatServer(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    String resourceDescriptor = handshake.getResourceDescriptor();

    Map<String, String> params = getQueryParameters(resourceDescriptor);

    String room = params.get("room");
    String username = params.get("username");

    if (room == null || username == null) {
      conn.close();
      return;
    }

    usernames.put(conn, username);

    synchronized (rooms) {
      rooms.computeIfAbsent(room, k -> new HashSet<>()).add(conn);
    }

    conn.send("Welcome to the room: " + room + "!");

    broadcastToRoom(room, username + " has joined the room!");

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room " + room + "!");
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    String room = findRoomForConnection(conn);
    String username = usernames.get(conn);

    if (room != null) {
      synchronized (rooms) {
        rooms.get(room).remove(conn);

        if (rooms.get(room).isEmpty()) {
          rooms.remove(room);
        }
      }

      broadcastToRoom(room, username + " has left the room!");
    }

    usernames.remove(conn);

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " left the room " + room + "!");
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    String room = findRoomForConnection(conn);
    String username = usernames.get(conn);

    if (room != null && username != null) {
      broadcastToRoom(room, username + ": " + message);

      System.out.println(username + ": " + message);
    }
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    String room = findRoomForConnection(conn);
    @SuppressWarnings("unused")
    String username = usernames.get(conn);

    if (room != null) {
      broadcastToRoom(room, message.array());
    }
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();

    if (conn != null) {
      System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");

    setConnectionLostTimeout(0);
    setConnectionLostTimeout(100);
  }

  private void broadcastToRoom(String room, String message) {
    synchronized (rooms) {
      Set<WebSocket> roomConnections = rooms.get(room);

      if (roomConnections != null) {
        for (WebSocket conn : roomConnections) {
          conn.send(message);
        }
      }
    }
  }

  private void broadcastToRoom(String room, byte[] message) {
    synchronized (rooms) {
      Set<WebSocket> roomConnections = rooms.get(room);

      if (roomConnections != null) {
        for (WebSocket conn : roomConnections) {
          conn.send(message);
        }
      }
    }
  }

  private Map<String, String> getQueryParameters(String resourceDescriptor) {
    Map<String, String> params = new HashMap<>();

    if (resourceDescriptor.contains("?")) {
      String query = resourceDescriptor.split("\\?", 2)[1];

      String[] pairs = query.split("&");

      for (String pair : pairs) {
        String[] keyValue = pair.split("=", 2);

        if (keyValue.length == 2) {
          params.put(keyValue[0], keyValue[1]);
        }
      }
    }

    return params;
  }

  private String findRoomForConnection(WebSocket conn) {
    synchronized (rooms) {
      for (Map.Entry<String, Set<WebSocket>> entry : rooms.entrySet()) {
        if (entry.getValue().contains(conn)) {
          return entry.getKey();
        }
      }
    }

    return null;
  }
}
