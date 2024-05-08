package com.example.study4child.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager2.widget.ViewPager2;
import com.example.study4child.Custom.*;
import com.example.study4child.R;
import com.example.study4child.Tools.*;

import java.util.ArrayList;

public class GameListActivity extends MyActivity implements GameListSelectInterface {

    GameListVHAdapter adapter;
    LoadScreenView loadScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        int level = getIntent().getIntExtra("level", 1);

        ConstraintLayout root = new ConstraintLayout(ctx);
        root.setBackgroundColor(ctx.getColor(R.color.cyan));
        root.setId(R.id.root);

        ViewPager2 pager = new ViewPager2(ctx);
        pager.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Math.round(ctx.getResources().getDisplayMetrics().heightPixels * 0.7f)));
        pager.setId(R.id.pager);

        adapter = new GameListVHAdapter(ctx, (GameListSelectInterface) this);
        pager.setAdapter(adapter);

        TextView title = new TextView(ctx);
        title.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        title.setGravity(View.TEXT_ALIGNMENT_CENTER);
        title.setTextSize(22);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTextColor(ctx.getColor(R.color.red));
        String test_string = ctx.getString(R.string.game_list_descr);
        title.setText(test_string);
        title.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        title.setId(R.id.main_title);

        root.addView(pager);
        root.addView(title);

        loadScreen = new LoadScreenView(ctx);
        loadScreen.setId(R.id.load);
        root.addView(loadScreen);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(root);

        int dp16 = Converter.Pixels(ctx, 16);
        int dp32 = Converter.Pixels(ctx, 32);

        constraintSet.connect(pager.getId(), ConstraintSet.BOTTOM, root.getId(), ConstraintSet.BOTTOM, dp32);
        constraintSet.connect(title.getId(), ConstraintSet.TOP, root.getId(), ConstraintSet.TOP, dp16);
        constraintSet.connect(title.getId(), ConstraintSet.START, root.getId(), ConstraintSet.START, dp16);
        constraintSet.connect(title.getId(), ConstraintSet.END, root.getId(), ConstraintSet.END, dp16);

        constraintSet.applyTo(root);

        GameLoadLevelPreviews loader = new GameLoadLevelPreviews();
        loader.Start(ctx, GameListActivity.this, level);

        setContentView(root);
    }

    @Override
    public void onGameSelected(GamePreviewData data) {
        Class activity = GameHelper.ActivityFromGameName(data.root_name);
        if(activity == null)
            return;

        MyApplication.getPoolInstance().play("open");

        Intent i = new Intent(GameListActivity.this, activity);
        i.putExtra("path", data.gameplay_root);

        startActivity(i);
    }

    public void onDataObtained(ArrayList<GamePreviewData> data_list) {
        new CardCreateTask().execute(data_list);
    }

    private class CardCreateTask extends AsyncTask<ArrayList<GamePreviewData>, Integer, String> {

        @Override
        protected String doInBackground(ArrayList<GamePreviewData>... data_list) {

            for (GamePreviewData data:
                    data_list[0]) {
                adapter.addDataItem(data);
            }

            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            adapter.notifyDataSetChanged();
            loadScreen.setVisibility(View.GONE);
        }
    }

    public void onDataFailed(String errorMessage) {
        //TODO: hide the loading window and display that something gone wrong
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private TextView noItemsText(Context ctx) {
        TextView test = new TextView(ctx);

        test.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        test.setGravity(View.TEXT_ALIGNMENT_CENTER);
        test.setTextSize(22);
        test.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        test.setTextColor(ctx.getColor(R.color.red));
        int level_id = getIntent().getIntExtra("level_id", -1);
        String test_string = ctx.getString(R.string.test_descr) + ", " + (level_id!=-1? ctx.getString(level_id) : "none");
        test.setText(test_string);
        test.setTypeface(ctx.getResources().getFont(R.font.font_family1));

        return test;
    }
}