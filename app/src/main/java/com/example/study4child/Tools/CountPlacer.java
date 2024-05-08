package com.example.study4child.Tools;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class CountPlacer {
    public static Vec2[] getFormation(int count) {
        return list.get(count);
    }

    private static HashMap<Integer, Vec2[]> list = new HashMap<>();
    static {
        list.put(1, new Vec2[]{
                new Vec2(0.5f, 0.5f)});
        list.put(2, new Vec2[]{
                new Vec2(0f, 1f),
                new Vec2(1f, 0f)});
        list.put(3, new Vec2[]{
                new Vec2(0.5f, 0f),
                new Vec2(0f, 1f),
                new Vec2(1f, 1f)});
        list.put(4, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0f, 1f),
                new Vec2(1f, 0f),
                new Vec2(1f, 1f)});
        list.put(5, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0f, 1f),
                new Vec2(1f, 0f),
                new Vec2(1f, 1f),
                new Vec2(0.5f, 0.5f)});
        list.put(6, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0.5f, 0f),
                new Vec2(1f, 0f),
                new Vec2(0f, 1f),
                new Vec2(0.5f, 1f),
                new Vec2(1f, 1f)});
        list.put(7, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0.5f, 0f),
                new Vec2(1f, 0f),
                new Vec2(0f, 1f),
                new Vec2(0.5f, 1f),
                new Vec2(1f, 1f),
                new Vec2(0.5f, 0.5f)});
        list.put(8, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0.5f, 0f),
                new Vec2(1f, 0f),
                new Vec2(0f, 1f),
                new Vec2(0.5f, 1f),
                new Vec2(1f, 1f),
                new Vec2(0f, 0.5f),
                new Vec2(1f, 0.5f)});
        list.put(9, new Vec2[]{
                new Vec2(0f, 0f),
                new Vec2(0.5f, 0f),
                new Vec2(1f, 0f),
                new Vec2(0f, 1f),
                new Vec2(0.5f, 1f),
                new Vec2(1f, 1f),
                new Vec2(0f, 0.5f),
                new Vec2(1f, 0.5f),
                new Vec2(0.5f, 0.5f)});
    }
}
