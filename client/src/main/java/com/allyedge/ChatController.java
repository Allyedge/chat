package com.allyedge;

import java.io.IOException;

import com.allyedge.util.CustomFilter;
import com.allyedge.util.Util;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;

public class ChatController {
  private final GlobalState globalState = GlobalState.getInstance();

  @FXML
  private ListView<String> messageList;

  @FXML
  private TextField messageInput;

  @FXML
  private Label roomNameLabel;

  @FXML
  private Button sendButton;

  private ObservableList<String> messages = FXCollections.observableArrayList();

  private ChatClient chatClient;

  public void initialize() {
    try {
      chatClient = new ChatClient(
          Constants.SERVER_URI + "?username=" + globalState.getUsername() + "&room=" + globalState.getRoom(), this);

      chatClient.connect();

      roomNameLabel.setText(globalState.getRoom());
    } catch (Exception e) {
      e.printStackTrace();
    }

    messageList.setItems(messages);

    sendButton.disableProperty().bind(Bindings.createBooleanBinding(
        () -> Util.checkTextFields(messageInput.getText()),
        messageInput.textProperty()));

    messageInput.setTextFormatter(new TextFormatter<>(new CustomFilter(60, false)));

    messageInput.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        sendMessage();
      }
    });
  }

  @FXML
  private void sendMessage() {
    String message = messageInput.getText().trim();

    if (!message.isEmpty()) {
      chatClient.send(message);
      messageInput.clear();
    }
  }

  @FXML
  private void exitRoom() {
    chatClient.close();
    globalState.setRoom(null);

    try {
      App.setRoot("home");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addReceivedMessage(String message) {
    messages.add(message);
  }
}