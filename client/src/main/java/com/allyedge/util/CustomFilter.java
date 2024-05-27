package com.allyedge.util;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

public class CustomFilter implements UnaryOperator<TextFormatter.Change> {
  private final int maxLength;
  private final boolean blockWhiteSpace;

  public CustomFilter(int maxLength, boolean blockWhiteSpace) {
    this.maxLength = maxLength;
    this.blockWhiteSpace = blockWhiteSpace;
  }

  @Override
  public TextFormatter.Change apply(TextFormatter.Change change) {
    if (change.isAdded()) {
      if (blockWhiteSpace && change.getText().contains(" ")) {
        return null;
      }

      if (change.getControlNewText().length() > maxLength) {
        return null;
      }
    }

    return change;
  }
}
