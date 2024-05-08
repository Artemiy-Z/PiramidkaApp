package com.example.study4child.Activities;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.study4child.Custom.MainPageCardCreator;
import com.example.study4child.R;
import com.example.study4child.Tools.MyActivity;
import com.example.study4child.Tools.MyApplication;

public class MainPageActivity extends MyActivity {

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

        View card1 = MainPageCardCreator.Create(ctx, R.drawable.level_1,
                R.string.level1_desc, R.string.level1_title, R.drawable.cyan_bg);
        card1.setId(R.id.card1);
        card1.setOnClickListener(this::CardClick);

        View card2 = MainPageCardCreator.Create(ctx, R.drawable.level_2,
                R.string.level2_desc, R.string.level2_title, R.drawable.pink_bg);
        card2.setId(R.id.card2);
        card2.setOnClickListener(this::CardClick);

        View card3 = MainPageCardCreator.Create(ctx, R.drawable.level_3,
                R.string.level3_desc, R.string.level3_title, R.drawable.yellow_bg);
        card3.setId(R.id.card3);
        card3.setOnClickListener(this::CardClick);


        linear_container.addView(card1);
        linear_container.addView(card2);
        linear_container.addView(card3);

        root.addView(linear_container);

        setContentView(root);
    }

    private void CardClick(View view) {
        View button = view.findViewById(R.id.card_button);
        if(button == null)
            return;

        Intent i = new Intent(MainPageActivity.this, GameListActivity.class);

        MyApplication.getPoolInstance().play("open");

        if(view.getId() == R.id.card1)
            i.putExtra("level", 1);
        else if(view.getId() == R.id.card2)
            i.putExtra("level", 2);
        else if(view.getId() == R.id.card3)
            i.putExtra("level", 3);

        startActivity(i);
    }
}