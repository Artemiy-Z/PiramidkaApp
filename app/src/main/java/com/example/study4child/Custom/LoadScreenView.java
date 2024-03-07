package com.example.study4child.Custom;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import com.example.study4child.R;
import com.example.study4child.Tools.Converter;
import pl.droidsonroids.gif.GifImageView;

public class LoadScreenView extends RelativeLayout {

    public LoadScreenView(Context ctx) {
        super(ctx);

        this.setBackgroundColor(ctx.getColor(R.color.green));
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.MATCH_PARENT));

        GifImageView image = new GifImageView(ctx);
        RelativeLayout.LayoutParams image_params =
                new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 128), Converter.Pixels(ctx, 128));
        image_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        image.setLayoutParams(image_params);
        image.setImageResource(R.drawable.loading);
        this.addView(image);
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility == VISIBLE) {
            super.setVisibility(visibility);
            return;
        }

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation a = new AlphaAnimation(1.0f, 0.0f);
                a.setFillAfter(true);
                a.setDuration(500);

                LoadScreenView.super.setVisibility(INVISIBLE);
                LoadScreenView.this.startAnimation(a);
            }
        }, 500);
    }
}
