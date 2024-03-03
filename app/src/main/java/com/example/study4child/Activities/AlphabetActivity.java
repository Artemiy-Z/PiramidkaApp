 package com.example.study4child.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.study4child.Custom.AlphabetCard;
import com.example.study4child.Custom.AlphabetVHAdapter;
import com.example.study4child.Custom.CustomVP2Adapter;
import com.example.study4child.Custom.ViewFragment;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.AlphabetLoader;
import com.google.firebase.database.DatabaseReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlphabetActivity extends AppCompatActivity {

    private ArrayList<AlphabetData> cards = new ArrayList<>(); // list of downloaded cards// private
    private int currentIndex = -1;

    private ArrayList<ViewFragment> loadedCards = new ArrayList<>();
    private AlphabetVHAdapter adapter;
    private ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        //TODO: add ViewPager2 or something else to animate cards switch from current to next
        pager = new ViewPager2(ctx);
        adapter = new AlphabetVHAdapter(ctx, this);

        pager.setAdapter(adapter);

        pager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull @NotNull View page, float position) {
                if(position <= -1 || position >= 1) {
                    page.setVisibility(View.INVISIBLE);
                }
                else {
                    page.setVisibility(View.VISIBLE);
                }

                Log.v("page transformer", position + "");

                // first state
                if(position > -0.5f && position < 0) {
                    // current card
                    page.bringToFront();
                    page.getParent().requestLayout();
                    float t = -position * 2;
                    page.setTranslationX(-page.getWidth()*0.5f * t + page.getWidth()*-position);
                    page.setRotation(-15*t);
                }
                if(position > 0.5f && position < 1) {
                    // next card
                    float t = (1-position) * 2;
                    page.setTranslationX(page.getWidth()*0.5f * t - page.getWidth()*-position);
                    page.setRotation(15*t);
                }

                // second state
                if(position > -1 && position < -0.5f) {
                    // current card
                    float t = (position+1)*2;
                    page.setTranslationX(-page.getWidth()*0.5f * t + page.getWidth()*-position);
                    page.setRotation(-15*t);
                }
                if(position > 0 && position < 0.5f) {
                    // next card
                    page.bringToFront();
                    page.getParent().requestLayout();
                    float t = position * 2;
                    page.setTranslationX(page.getWidth()*0.5f * t - page.getWidth()*-position);
                    page.setRotation(15*t);
                }
            }
        });

        // get path from intent
        String path = getIntent().getStringExtra("path");

        new AlphabetLoader().Load(ctx, AlphabetActivity.this, path); // (1)

        setContentView(pager);
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
        adapter.notifyDataSetChanged();

        if(currentIndex==0) {
            LoadNextCard();
        }
        else if(currentIndex==1) {
            // hide loader
        }
    }

    public void onCardClicked(View v) {
        if(adapter.getItemCount() > 1) {
            pager.setCurrentItem(1, true);
        }
    }
}