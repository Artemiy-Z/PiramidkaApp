package com.example.study4child.Tools;

import android.util.Log;

public class Letter2Word {
    public static String convert(String l) {
        switch(l.toLowerCase()) {
            case "а":
                return "А";
            case "б":
                return "Бэ";
            case "в":
                return "Вэ";
            case "г":
                return "Гэ";
            case "д":
                return "Дэ";
            case "е":
                return "Е";
            case "ж":
                return "Жэ";
            case "з":
                return "Зэ";
            case "и":
                return "И";
            case "й":
                return "И краткая";
            case "к":
                return "Ка";
            case "л":
                return "Эл";
            case "м":
                return "Эм";
            case "н":
                return "Эн";
            case "о":
                return "О";
            case "п":
                return "Пэ";
            case "р":
                return "Эр";
            case "с":
                return "Эс";
            case "т":
                return "Тэ";
            case "у":
                return "У";
            case "ф":
                return "Эф";
            case "х":
                return "Ха";
            case "ц":
                return "Цэ";
            case "ч":
                return "Чэ";
            case "ш":
                return "Ша";
            case "щ":
                return "Ща";
            case "ь":
                return "Ь";
            case "ы":
                return "Ы";
            case "ъ":
                return "Ъ";
            case "э":
                return "Э";
            case "ю":
                return "Ю";
            case "я":
                return "Я";
        }

        Log.e("LetterConvert", "the letter '" + l + "' is invalid");
        return "Nan";
    }
}
