package com.allyedge;

import java.io.IOException;

import com.allyedge.util.CustomFilter;
import com.allyedge.util.Util;
import com.allyedge.util.WhiteSpaceFilter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class HomeController {
  private final GlobalState globalState = GlobalState.getInstance();

  @FXML
  private TextField usernameField;

  @FXML
  private TextField roomNameField;

  @FXML
  private Button enterButton;

  @FXML
  public void initialize() {
    usernameField.setTextFormatter(new TextFormatter<>(new CustomFilter(20, true)));
    roomNameField.setTextFormatter(new TextFormatter<>(new CustomFilter(20, true)));

    enterButton.disableProperty().bind(Bindings.createBooleanBinding(
        () -> Util.checkTextFields(usernameField.getText(), roomNameField.getText()),
        usernameField.textProperty(),
        roomNameField.textProperty()));
  }

  @FXML
  private void enterRoom() {
    String username = usernameField.getText().trim();
    String roomName = roomNameField.getText().trim();

    if (!username.isEmpty() && !roomName.isEmpty()) {
      globalState.setUsername(username);
      globalState.setRoom(roomName);

      try {
        App.setRoot("chat");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}