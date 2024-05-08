 package com.example.study4child.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.study4child.Custom.*;
import com.example.study4child.R;
import com.example.study4child.Tools.*;
import com.google.firebase.database.DatabaseReference;
import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;
import org.jetbrains.annotations.NotNull;
import pl.droidsonroids.gif.GifImageView;

import java.util.ArrayList;
import java.util.Locale;

 public class AlphabetActivity extends MyActivity {

    private ArrayList<AlphabetData> cards = new ArrayList<>(); // list of downloaded cards// private
    private int currentIndex = -1;
    private int loadedIndex = -1;

    private AlphabetVHAdapter adapter;
    private ViewPager2 pager;
    private LoadScreenView loading;
    private GifImageView congratsView;
    private Button exit_button;
    private GifImageView clickIcon;
    private boolean speechInitialized = false;

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
        pager.setUserInputEnabled(false);

        root.addView(pager);

        loading = new LoadScreenView(ctx);
        loading.setVisibility(View.VISIBLE);

        congratsView = new CongratulationGifImage(ctx);
        congratsView.setVisibility(View.INVISIBLE);

        root.addView(congratsView);
        root.addView(loading);

        exit_button = new Button(ctx);

        RelativeLayout.LayoutParams exit_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        exit_params.addRule(RelativeLayout.ALIGN_PARENT_START);
        exit_params.addRule(RelativeLayout.ALIGN_PARENT_END);
        exit_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        exit_params.setMargins(0, 0, 0, Converter.Pixels(ctx, 64));
        exit_button.setLayoutParams(exit_params);

        exit_button.setText("Выход");
        exit_button.setTextSize(24);
        exit_button.setTextColor(ctx.getColor(R.color.red));
        exit_button.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        exit_button.setBackground(ctx.getDrawable(R.drawable.button_bg));
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getPoolInstance().play("bubble");
                Speech.getInstance().say(ctx.getResources().getString(R.string.thanks_for_playing));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        });
        exit_button.setVisibility(View.GONE);

        root.addView(exit_button);

        clickIcon = new GifImageView(ctx);
        RelativeLayout.LayoutParams click_params = new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 128),
                                                                                   Converter.Pixels(ctx, 128));
        click_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        clickIcon.setLayoutParams(click_params);
        clickIcon.setImageResource(R.drawable.click);
        clickIcon.setVisibility(View.INVISIBLE);

        root.addView(clickIcon);

        // get path from intent
        String path = getIntent().getStringExtra("path");

        new AlphabetLoader().Load(ctx, AlphabetActivity.this, path); // (1)

        setContentView(root);

        Speech.init(ctx, getPackageName(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        switch (status) {
                            case TextToSpeech.SUCCESS:
                                Speech.getInstance().setLocale(new Locale("RU"));
                                speechInitialized = true;
                                break;
                            default:
                                Toast.makeText(ctx, "Произошла ошибка во время инициализации", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        // prevent memory leaks when activity is destroyed
        super.onDestroy();
        Speech.getInstance().shutdown();
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
            ErrorDialog(AlphabetActivity.this).show();
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
            clickIcon.setVisibility(View.VISIBLE);
        }
    }

    public void onCardClicked(View v) {

        if(!speechInitialized) {
            return;
        }

        v.setOnClickListener(null);
        clickIcon.setVisibility(View.INVISIBLE);

        View title = ((View) v.getParent()).findViewById(R.id.card_title);
        String[] tags = ((String)title.getTag()).split(";");
        String sasText = "Буква " + Letter2Word.convert(tags[0]) + ". " + tags[1];

        Speech.getInstance().say(sasText,
                new TextToSpeechCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {
                        v.setOnClickListener(AlphabetActivity.this::onCardExited);
                        clickIcon.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void onCardExited(View v) {
        if(pager.getCurrentItem() < loadedIndex) {
            congratsView.setVisibility(View.VISIBLE);

            MyApplication.getPoolInstance().play("swipe");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    congratsView.setVisibility(View.INVISIBLE);
                }
            }, 1500);

            pager.setCurrentItem(adapter.getItemCount(), true);
            LoadNextCard();
        }
    }

    private void onCardsEnded() {
        exit_button.setVisibility(View.VISIBLE);
    }

    public static AlertDialog.Builder ErrorDialog(AppCompatActivity act) {
        return new AlertDialog.Builder(act)
                .setMessage(act.getString(R.string.error))
                .setNeutralButton("Окей", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        act.finish();
                    }
                });
    }
}