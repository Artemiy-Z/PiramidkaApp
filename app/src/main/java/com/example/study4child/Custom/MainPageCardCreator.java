package com.example.study4child.Custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.study4child.Activities.GameListActivity;
import com.example.study4child.Activities.MainPageActivity;
import com.example.study4child.R;
import com.example.study4child.Tools.Converter;

public class MainPageCardCreator {
    @SuppressLint("UseCompatLoadingForDrawables")
    public static View Create(Context ctx, int image_id, int text_id, int level_id, int background_id) {
        RelativeLayout root = new RelativeLayout(ctx);
        LinearLayout.LayoutParams root_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int dp16 = Converter.Pixels(ctx, 16);
        root_params.setMargins(dp16, dp16, dp16, dp16);
        root.setLayoutParams(root_params);
        root.setBackground(ctx.getResources().getDrawable(R.drawable.button_bg));

        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        TableLayout tableLayout = new TableLayout(ctx);
        RelativeLayout.LayoutParams tableRootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tableRootParams.setMargins(dp16, dp16, dp16, dp16);
        tableLayout.setLayoutParams(tableRootParams);

        TableRow tableRow = new TableRow(ctx);
        tableRow.setLayoutParams(tableParams);
        tableLayout.addView(tableRow);

        ImageView image = new ImageView(ctx);
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams(Converter.Pixels(ctx, 160),
                Converter.Pixels(ctx, 160));
        imageParams.setMargins(0, 0, dp16, 0);
        image.setLayoutParams(imageParams);
        image.setImageResource(image_id);
		image.setBackground(ctx.getResources().getDrawable(background_id));
        tableRow.addView(image);

        LinearLayout desc_container = new LinearLayout(ctx);
        TableRow.LayoutParams descParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        desc_container.setLayoutParams(descParams);
        desc_container.setOrientation(LinearLayout.VERTICAL);

        TextView level = new TextView(ctx);
        LinearLayout.LayoutParams levelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        level.setLayoutParams(levelParams);
        level.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
        level.setTextSize(22);
        level.setTextColor(ctx.getResources().getColor(R.color.red));
        level.setText(level_id);
        level.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        desc_container.addView(level);

        TextView text = new TextView(ctx);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        text.setLayoutParams(textParams);
        text.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
        text.setTextSize(18);
        text.setTextColor(ctx.getResources().getColor(R.color.red));
        text.setText(text_id);
        text.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        desc_container.addView(text);

        tableRow.addView(desc_container);

        // click handler and animator
        RelativeLayout foreground = new RelativeLayout(ctx);
        foreground.setBackground(ctx.getResources().getDrawable(R.drawable.card_ripple));
        foreground.setId(R.id.card_button); // id, for external usage

        root.addView(tableLayout);
        root.addView(foreground);

        root.post(new Runnable() {
            @Override
            public void run() {
                foreground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, root.getMeasuredHeight()));
            }
        });

        return  root;
    }
}
