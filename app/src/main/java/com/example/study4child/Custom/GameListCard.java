package com.example.study4child.Custom;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.example.study4child.R;
import com.example.study4child.Tools.Converter;
import com.example.study4child.Tools.GamePreviewData;
import org.jetbrains.annotations.NotNull;

public class GameListCard extends CardView {
    public GameListCard(@NonNull @NotNull Context ctx, GamePreviewData data) {
        super(ctx);

        this.setBackground(ctx.getDrawable(R.drawable.button_bg));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Math.round(ctx.getResources().getDisplayMetrics().heightPixels * 0.7f));
        int dp16 = Converter.Pixels(ctx, 16);
        params.setMargins(dp16, dp16, dp16, dp16);
        this.setLayoutParams(params);

        LinearLayout container = new LinearLayout(ctx);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.MATCH_PARENT));
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp16, dp16, dp16, dp16);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        container.setId(R.id.card_root);

        ImageView image = new ImageView(ctx);
        image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageBitmap(data.image);
        image.post(new Runnable() {
            @Override
            public void run() {
                image.setLayoutParams(new LinearLayout.LayoutParams(image.getMeasuredWidth(), image.getMeasuredWidth()));
            }
        });
        image.setId(R.id.card_image);

        TextView title = new TextView(ctx);
        LinearLayout.LayoutParams title_params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        title_params.setMargins(0, 0, 0, Converter.Pixels(ctx, 16));
        title.setLayoutParams(title_params);
        title.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        title.setTextSize(24);
        title.setTextColor(ctx.getColor(R.color.red));
        title.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        title.setText(data.title);
        title.setId(R.id.card_title);

        Button button = new Button(ctx);
        LinearLayout.LayoutParams button_params =
                new LinearLayout.LayoutParams(Converter.Pixels(ctx, 128f), Converter.Pixels(ctx, 64f));
        button_params.setMargins(0, Converter.Pixels(ctx, 32), 0, 0);
        button.setLayoutParams(button_params);
        button.setId(R.id.card_button);
        button.setBackgroundResource(R.drawable.cyan_bg);
        button.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        button.setText(R.string.button_play);
        button.setTextColor(ctx.getResources().getColor(R.color.red));
        button.setTextSize(18);
        button.setTypeface(ctx.getResources().getFont(R.font.font_family1));

        container.addView(title);
        container.addView(image);
        container.addView(button);

        this.addView(container);
    }
}
