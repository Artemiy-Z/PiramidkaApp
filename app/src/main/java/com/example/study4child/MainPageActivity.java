package com.example.study4child;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		Context ctx = getApplicationContext();

        RelativeLayout root = new RelativeLayout(ctx);
        root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(ctx.getResources().getColor(R.color.green));

        LinearLayout linear_container = new LinearLayout(ctx);
        linear_container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        linear_container.setOrientation(LinearLayout.VERTICAL);
        linear_container.setGravity(Gravity.CENTER);

        linear_container.addView(MainPageCardCreator.Create(ctx, R.drawable.level_1, R.string.level1_desc, R.string.level1_title, R.drawable.cyan_bg));
        linear_container.addView(MainPageCardCreator.Create(ctx, R.drawable.level_2, R.string.level2_desc, R.string.level2_title, R.drawable.pink_bg));
        linear_container.addView(MainPageCardCreator.Create(ctx, R.drawable.level_3, R.string.level3_desc, R.string.level3_title, R.drawable.yellow_bg));

        root.addView(linear_container);

        setContentView(root);
    }
}