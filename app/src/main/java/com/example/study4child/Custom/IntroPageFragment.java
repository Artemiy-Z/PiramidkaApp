package com.example.study4child.Custom;

import android.os.Bundle;
import android.content.Context;

import androidx.fragment.app.Fragment;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.view.Gravity;
import android.widget.TextView;
import com.example.study4child.R;
import com.example.study4child.Tools.Converter;

public class IntroPageFragment extends Fragment {
    private View view;

    public IntroPageFragment(Context ctx, int background_id, int text_id, int color_id, int gravity, float margin) {
        this(ctx, background_id, text_id, color_id, gravity, margin, -1);
    }

    public IntroPageFragment(Context ctx, int background_id, int text_id, int color_id, int gravity, float margin, int app_name) {
        RelativeLayout container = new RelativeLayout(ctx);
		container.setBackgroundColor(ctx.getResources().getColor(R.color.green));
		container.setGravity(Gravity.CENTER);
		
		ImageView background = new ImageView(ctx);
		background.setLayoutParams(new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.MATCH_PARENT,
			RelativeLayout.LayoutParams.MATCH_PARENT));
        background.setImageResource(background_id);
	background.setScaleType(ImageView.ScaleType.FIT_XY);

		TextView text = new TextView(ctx);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
                                                                RelativeLayout.LayoutParams.MATCH_PARENT);
		
        switch(gravity) {
            case 0:
                text.setGravity(Gravity.CENTER); break;
            case 1:
                text.setGravity(Gravity.TOP);
                params.setMargins(0, Converter.Pixels(ctx, margin), 0, 0); break;
            case 2:
                text.setGravity(Gravity.BOTTOM);
                params.setMargins(0, 0, 0, Converter.Pixels(ctx, margin));  break;
        }

        text.setLayoutParams(params);

		text.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
		text.setText(text_id);
        if(app_name != -1) {
            String name = ctx.getResources().getString(app_name);
            String s = text.getText() + " " + name.substring(0, name.length()-1) + "у!";
            text.setText(s);
        }
		text.setTextColor(ctx.getResources().getColor(color_id));
		text.setTextSize(28);
        text.setTypeface(ctx.getResources().getFont(R.font.font_family1));

        text.requestLayout();

		container.addView(background);
		container.addView(text);

        view = container;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return view;
    }
}