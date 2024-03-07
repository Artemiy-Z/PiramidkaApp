 package com.example.study4child.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.study4child.Custom.*;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.AlphabetLoader;
import com.google.firebase.database.DatabaseReference;
import org.jetbrains.annotations.NotNull;
import pl.droidsonroids.gif.GifImageView;

import java.util.ArrayList;

public class AlphabetActivity extends AppCompatActivity {

    private ArrayList<AlphabetData> cards = new ArrayList<>(); // list of downloaded cards// private
    private int currentIndex = -1;
    private int loadedIndex = -1;

    private AlphabetVHAdapter adapter;
    private ViewPager2 pager;
    private LoadScreenView loading;
    private GifImageView congratsView;
    private Button exit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        RelativeLayout root = new RelativeLayout(ctx);
        root.setBackgroundColor(ctx.getColor(R.color.cyan));

        //TODO: add ViewPager2 or something else to animate cards switch from current to next
        pager = new ViewPager2(ctx);
        adapter = new AlphabetVHAdapter(ctx, this);

        pager.setAdapter(adapter);

        pager.setOffscreenPageLimit(4);
        pager.setEnabled(false);

        root.addView(pager);

        loading = new LoadScreenView(ctx);
        loading.setVisibility(View.VISIBLE);

        congratsView = new GifImageView(ctx);
        congratsView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        congratsView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        congratsView.setImageResource(R.drawable.sparkles);
        congratsView.setVisibility(View.INVISIBLE);

        root.addView(congratsView);
        root.addView(loading);

        exit_button = new Button(ctx);
        exit_button.setText("Выход");
        exit_button.setTextSize(24);
        exit_button.setTextColor(ctx.getColor(R.color.red));
        exit_button.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        exit_button.setBackground(ctx.getDrawable(R.drawable.button_bg));
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage(R.string.thanks_for_playing);
                builder.setNeutralButton("Ага!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                finish();
            }
        });
        exit_button.setVisibility(View.GONE);

        root.addView(exit_button);

        // get path from intent
        String path = getIntent().getStringExtra("path");

        new AlphabetLoader().Load(ctx, AlphabetActivity.this, path); // (1)

        setContentView(root);
    }

    public void onDataObtained(ArrayList<DatabaseReference> data) { // (2)
        for(DatabaseReference ref : data) {
            cards.add(new AlphabetData(getApplicationContext(), ref, AlphabetActivity.this));
        }

        LoadNextCard();
    }

    public void onDataFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void LoadNextCard() { // (3)
        currentIndex++;
        if(currentIndex > cards.size()-1) {
            onCardsEnded();
            return;
        }

        cards.get(currentIndex).Load();
    }

    public void onCardLoaded(AlphabetData data) { // (4)
        if(data == null) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage(getApplicationContext().getString(R.string.error))
                    .setNeutralButton("Окей", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            finish();
        }

        addCard(data);
    }

    public void addCard(AlphabetData data) { // (6)
        adapter.addDataItem(data);
        adapter.notifyItemInserted(currentIndex);
        loadedIndex++;

        if(currentIndex==0) {
            LoadNextCard();
        }
        else if(currentIndex==1) {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    public void onCardClicked(View v) {

        v.setOnClickListener(null);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setFillAfter(true);
        fadeIn.setDuration(500);
        congratsView.setVisibility(View.VISIBLE);
        congratsView.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setFillAfter(true);
                fadeOut.setDuration(500);
                congratsView.setVisibility(View.INVISIBLE);
                congratsView.startAnimation(fadeOut);
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setOnClickListener(AlphabetActivity.this::onCardExited);
            }
        }, 2500);
    }

    public void onCardExited(View v) {
        if(pager.getCurrentItem() < loadedIndex) {
            pager.setCurrentItem(adapter.getItemCount(), true);
            LoadNextCard();
        }
    }

    private void onCardsEnded() {
        exit_button.setVisibility(View.VISIBLE);
    }
}