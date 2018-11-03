package com.vsb.tamz.goaltracker;

import android.widget.EditText;

public class ValidationUtils {
    public static boolean isFieldBlank(EditText editText) {
        return editText.length() == 0 || editText.getText().toString().matches("/s");
    }

    public static void setBlankFieldError(EditText editText) {
        editText.setError("Field cannot be blank!");
    }

    public static void resetError(EditText editText) {
        editText.setError(null);
    }
}
