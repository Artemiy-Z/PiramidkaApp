package com.example.study4child.Custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.example.study4child.Tools.Converter;

import java.util.ArrayList;
import java.util.HashMap;

public class FiguresTable extends TableLayout {

    private ArrayList<View> views = new ArrayList<>();
    public ArrayList<View> getViews() {
        return views;
    }

    public FiguresTable(Context ctx, HashMap<String, Bitmap> holes) {
        super(ctx);

        ArrayList<String> keySet = new ArrayList<String>(holes.keySet());

        float itemWidth = 120;
        int dp16 = Converter.Pixels(ctx, 16);

        ArrayList<ArrayList<View>> list2d = new ArrayList<>();
        list2d.add(new ArrayList<>());
        int i = 0;
        for(int j = 0;j<20;j++) {
            ImageView item = new ImageView(ctx);
            item.setLayoutParams(new TableRow.LayoutParams(
                    Converter.Pixels(ctx, itemWidth),
                    Converter.Pixels(ctx, itemWidth)
            ));
            item.setPadding(dp16, dp16, dp16, dp16);
            item.setImageBitmap(holes.get(keySet.get(0)));
            item.setScaleType(ImageView.ScaleType.FIT_CENTER);
            item.setTag(keySet.get(0));
            list2d.get(list2d.size()-1).add(item);
            keySet.remove(0);

            views.add(item);

            i++;

            if(i == 3) {
                list2d.add(new ArrayList<>());
                i = 0;
            }

            if(keySet.isEmpty())
                break;
        }

        for(ArrayList<View> l : list2d) {
            TableRow row = new TableRow(ctx);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Converter.Pixels(ctx, itemWidth)
            ));
            row.setGravity(Gravity.CENTER);
            row.setPadding(0, 0, 0, dp16);

            for(View v : l)
                row.addView(v);

            this.addView(row);
        }
    }
}
