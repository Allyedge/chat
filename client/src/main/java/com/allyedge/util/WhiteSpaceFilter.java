package com.allyedge.util;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

public class WhiteSpaceFilter implements UnaryOperator<TextFormatter.Change> {
  public WhiteSpaceFilter() {
  }

  @Override
  public TextFormatter.Change apply(TextFormatter.Change change) {
    if (change.getText().equals(" ")) {
      return null;
    }

    return change;
  }
}
