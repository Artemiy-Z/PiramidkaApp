package com.example.study4child.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.example.study4child.Custom.CountTextView;
import com.example.study4child.Custom.LoadScreenView;
import com.example.study4child.R;
import com.example.study4child.Tools.*;
import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import java.util.*;

public class CountActivity extends MyActivity {

    private Random random;
    private RelativeLayout container;
    private CountTextView countView;
    private LoadScreenView loading;
    private Button exit_button;

    private ArrayList<Bitmap> images = new ArrayList<>();

    private boolean imagesLoaded = false;
    private boolean speechInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        random = new Random();

        ConstraintLayout root = new ConstraintLayout(ctx);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        root.setId(R.id.root);
        root.setBackgroundColor(getColor(R.color.green));

        container = new RelativeLayout(ctx);
        container.setLayoutParams(new ConstraintLayout.LayoutParams(
                ctx.getResources().getDisplayMetrics().widthPixels - Converter.Pixels(ctx, 32),
                ctx.getResources().getDisplayMetrics().widthPixels - Converter.Pixels(ctx, 32)
        ));
        container.setId(R.id.container);

        countView = new CountTextView(ctx);
        countView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        countView.setTextSize(32);
        countView.setTextColor(getColor(R.color.red));
        countView.setTypeface(ctx.getResources().getFont(R.font.font_family1));
        countView.setText("-1");
        countView.setId(R.id.count_text);
        countView.setOnCountListener(this::countComplete);

        loading = new LoadScreenView(ctx);
        loading.setVisibility(View.VISIBLE);
        loading.setId(R.id.load);

        exit_button = new Button(ctx);
        exit_button.setLayoutParams(new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Converter.Pixels(ctx, 48)
        ));

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
        exit_button.setId(R.id.bottom_button);

        root.addView(container);
        root.addView(countView);
        root.addView(exit_button);
        root.addView(loading);

        ConstraintSet set = new ConstraintSet();
        set.clone(root);

        set.centerHorizontally(R.id.container, R.id.root);
        set.connect(R.id.container, ConstraintSet.TOP, R.id.root, ConstraintSet.TOP);
        set.connect(R.id.container, ConstraintSet.BOTTOM, R.id.root, ConstraintSet.BOTTOM);

        set.centerHorizontally(R.id.count_text, R.id.root);
        set.connect(R.id.count_text, ConstraintSet.BOTTOM, R.id.container, ConstraintSet.TOP, Converter.Pixels(ctx, 16));

        set.centerHorizontally(R.id.bottom_button, R.id.root);
        set.connect(R.id.bottom_button, ConstraintSet.BOTTOM, R.id.root, ConstraintSet.BOTTOM, Converter.Pixels(ctx, 64));

        set.applyTo(root);
        setContentView(root);

        new CountLoadImages().Load(ctx, CountActivity.this, getIntent().getStringExtra("path"));

        Speech.init(ctx, getPackageName(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                switch (status) {
                    case TextToSpeech.SUCCESS:
                        speechInit = true;
                        Speech.getInstance().setLocale(new Locale("RU"));
                        if(imagesLoaded) {
                            ShowNewSet(1);

                            loading.setVisibility(View.INVISIBLE);
                        }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Speech.getInstance().shutdown();
    }

    public void onDataObtained(ArrayList<Bitmap> data_list) {
        images.addAll(data_list);

        imagesLoaded = true;

        if(speechInit) {
            ShowNewSet(1);

            loading.setVisibility(View.INVISIBLE);
        }
    }

    public void onDataFailed() {
        AlphabetActivity.ErrorDialog(CountActivity.this).show();
    }

    private void ShowNewSet(int count) {
        Context ctx = getApplicationContext();

        Bitmap randomImage = images
                .get(random.nextInt(images.size()));

        if(count == -1)
            count = random.nextInt(10);

        // reset countView
        countView.reset(count);

        for(int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            v.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .translationX(container.getMeasuredWidth())
                    .translationY(container.getMeasuredHeight())
                    .setDuration(500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    container.removeView(v);
                }
            }, 500);
        }

        int itemWidth = (container.getMeasuredWidth() / 3) - Converter.Pixels(ctx, 24);

        Vec2[] positions = CountPlacer.getFormation(count);
        for(Vec2 pos : positions) {
            ImageView image = new ImageView(ctx);
            image.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));

            image.setImageBitmap(randomImage);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Vec2 temp = new Vec2(2*pos.x, 2*pos.y); // remap from 0 to 1 to 0 to 2
            temp.x *= container.getMeasuredWidth() * 0.5f - itemWidth * 0.5f;
            temp.y *= container.getMeasuredHeight() * 0.5f - itemWidth * 0.5f;

            temp.x += (random.nextFloat() * 2f - 1f)*Converter.Pixels(ctx, 8);
            temp.y += (random.nextFloat() * 2f - 1f)*Converter.Pixels(ctx, 8);

            container.addView(image);

            image.setScaleX(0f);
            image.setScaleY(0f);

            image.animate()
                .translationX(temp.x)
                .translationY(temp.y)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(500)
                .start();

            image.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(v.getTag() != null)
                        return false;

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            v.animate()
                                    .scaleX(0.8f)
                                    .scaleY(0.8f)
                                    .setDuration(200)
                                    .start();
                            return true;
                        case MotionEvent.ACTION_UP:
                            v.animate()
                                    .scaleX(1.1f)
                                    .scaleY(1.1f)
                                    .setDuration(150)
                                    .start();

                            MyApplication.getPoolInstance().play("bubble");

                            countView.countUp();
                            v.setTag("clicked");
                            return false;
                    }

                    return false;
                }
            });

        }
    }

    private void countComplete(int count) {
        Speech.getInstance().say(String.valueOf(count),
                new TextToSpeechCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyApplication.getPoolInstance().play("coin");
                        if(count == 9) {
                            ShowNewSet(1);
                            exit_button.setVisibility(View.VISIBLE);
                        }
                        else {
                            ShowNewSet(count + 1);
                        }
                    }
                }, 150);
            }

            @Override
            public void onError() {

            }
        });
    }
}