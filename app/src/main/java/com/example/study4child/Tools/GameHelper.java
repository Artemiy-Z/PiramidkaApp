package com.example.study4child.Tools;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study4child.Activities.AlphabetActivity;

public class GameHelper {
    public static Class ActivityFromGameName(String name) {
        switch (name) {
            default: return null;
            case "игра_азбука": return AlphabetActivity.class;
        }
    }
}
