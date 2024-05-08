package com.example.study4child.Custom;

import android.content.Context;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.Converter;
import org.jetbrains.annotations.NotNull;

public class AlphabetCard extends RelativeLayout {


    public AlphabetCard(@NonNull @NotNull Context ctx, AlphabetData data) {
        super(ctx);

        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setBackgroundColor(ctx.getColor(R.color.cyan));
        this.setGravity(Gravity.CENTER);

        RelativeLayout root2 = new RelativeLayout(ctx);
        root2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        root2.setBackgroundColor(ctx.getColor(R.color.cyan));
        root2.setGravity(Gravity.CENTER);

        RelativeLayout container = new RelativeLayout(ctx);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                Math.round(ctx.getResources().getDisplayMetrics().widthPixels * 1.3f) );
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.setLayoutParams(params);
        container.setBackground(ctx.getDrawable(R.drawable.button_bg));
        int dp4 = Converter.Pixels(ctx, 4);
        container.setPadding(dp4, dp4, dp4, dp4);
        container.setGravity(Gravity.CENTER);

        ImageView image = new ImageView(ctx);
        image.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        image.setId(R.id.card_image);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        TextView title = new TextView(ctx);
        RelativeLayout.LayoutParams title_params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        title_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        title_params.setMargins(0, Converter.Pixels(ctx, 16), 0, 0);
        title.setLayoutParams(title_params);
        title.setTextSize(22);
        title.setTextColor(ctx.getColor(R.color.red));
        title.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        title.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        title.setId(R.id.card_title);

        container.addView(image);
        container.addView(title);

        root2.addView(container);

        this.addView(root2);
    }
}
