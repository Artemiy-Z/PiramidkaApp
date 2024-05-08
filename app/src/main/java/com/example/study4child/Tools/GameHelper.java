package com.example.study4child.Tools;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study4child.Activities.AlphabetActivity;
import com.example.study4child.Activities.CountActivity;
import com.example.study4child.Activities.FiguresActivity;

public class GameHelper {
    public static Class ActivityFromGameName(String name) {
        switch (name) {
            default: return null;
            case "игра_азбука": return AlphabetActivity.class;
            case "игра_счет": return CountActivity.class;
            case "игра_фигуры": return FiguresActivity.class;
        }
    }
}
