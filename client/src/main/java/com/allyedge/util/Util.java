package com.allyedge.util;

public class Util {
  public static boolean checkTextFields(String... textFields) {
    for (String textField : textFields) {
      if (textField.trim().isEmpty()) {
        return true;
      }
    }

    return false;
  }
}
