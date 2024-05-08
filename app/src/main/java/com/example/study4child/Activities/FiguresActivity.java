package com.example.study4child.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.example.study4child.Custom.CongratulationGifImage;
import com.example.study4child.Custom.DraggableImageView;
import com.example.study4child.Custom.FiguresTable;
import com.example.study4child.Custom.LoadScreenView;
import com.example.study4child.R;
import com.example.study4child.Tools.Converter;
import com.example.study4child.Tools.FiguresLoader;
import com.example.study4child.Tools.MyActivity;
import com.example.study4child.Tools.MyApplication;
import net.gotev.speech.Speech;
import pl.droidsonroids.gif.GifImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class FiguresActivity extends MyActivity {

    private Random random;
    private LoadScreenView loading;
    private ConstraintLayout root;

    private HashMap<String, Bitmap> main_map, hole_map;
    private ArrayList<View> holesList;
    private ConstraintLayout holesContainer;
    private Button exit_button;

    private DraggableImageView figure;
    private ArrayList<Integer> colors = new ArrayList<>();
    private float centerX, centerY;
    private float dx, dy;
    private int randomInt; // current int
    private int lastInt = -1; // last int

    private boolean speechReady = false;
    private boolean dataReady = false;

    private GifImageView congratsView;

    @Override
    protected void onDestroy() {
        // prevent memory leaks when activity is destroyed
        super.onDestroy();
        Speech.getInstance().shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random();

        Context ctx = getApplicationContext();
        String path = getIntent().getStringExtra("path");

        root = new ConstraintLayout(ctx);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        root.setBackgroundColor(ctx.getColor(R.color.green));

        // colors to set tint to figure randomly
        colors.add(ctx.getColor(R.color.red));
        colors.add(ctx.getColor(R.color.blue));
        colors.add(ctx.getColor(R.color.orange));
        colors.add(ctx.getColor(R.color.cyan));
        colors.add(ctx.getColor(R.color.purple));

        loading = new LoadScreenView(ctx);
        loading.setVisibility(View.VISIBLE);
        loading.setId(R.id.load);

        holesContainer = new ConstraintLayout(ctx);
        holesContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        holesContainer.setId(R.id.container);

        root.addView(holesContainer);

        figure = new DraggableImageView(ctx);
        figure.setLayoutParams(new ViewGroup.LayoutParams(
                Converter.Pixels(ctx, 120),
                Converter.Pixels(ctx, 120)
        ));
        int dp16 = Converter.Pixels(ctx, 16);
        figure.setPadding(dp16, dp16, dp16, dp16);
        figure.setScaleType(ImageView.ScaleType.FIT_CENTER);
        figure.setVisibility(View.INVISIBLE);
        figure.setId(R.id.card1);
        root.addView(figure);

        congratsView = new CongratulationGifImage(ctx);
        congratsView.setVisibility(View.INVISIBLE);

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

        root.addView(exit_button);

        root.addView(loading);
        //root.addView(congratsView);

        ConstraintSet set = new ConstraintSet();
        set.clone(root);

        set.centerHorizontally(R.id.bottom_button, R.id.root);
        set.connect(R.id.bottom_button, ConstraintSet.BOTTOM, R.id.root, ConstraintSet.BOTTOM, Converter.Pixels(ctx, 64));

        set.applyTo(root);

        setContentView(root);

        Speech.init(ctx, getPackageName(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                switch (status) {
                    case TextToSpeech.SUCCESS:
                        Speech.getInstance().setLocale(new Locale("RU"));
                        speechReady = true;
                        if(dataReady) {
                            loading.setVisibility(View.INVISIBLE);
                            showFigure();
                        }
                        break;
                }
            }
        });

        new FiguresLoader().Load(ctx, FiguresActivity.this, path);
    }

    public void onDataObtained(HashMap<String, Bitmap> mains, HashMap<String, Bitmap> holes) {
        main_map = mains;
        hole_map = holes;

        dataReady = true;

        Context ctx = getApplicationContext();

        FiguresTable holesView = new FiguresTable(ctx, hole_map);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().widthPixels
        );
        holesView.setLayoutParams(params);
        holesView.setGravity(Gravity.CENTER_VERTICAL);

        holesList = holesView.getViews();

        holesContainer.addView(holesView);
        root.requestLayout();

        // set center position coordinates
        centerX = root.getMeasuredWidth() / 2f - Converter.Pixels(ctx, 49);
        centerY = 3f * (root.getMeasuredHeight() / 4f) - Converter.Pixels(ctx, 49);
        // show figure
        if(speechReady) {
            loading.setVisibility(View.INVISIBLE);
            showFigure();
        }
    }

    private int count = 0;

    private void showFigure() {
        MyApplication.getPoolInstance().play("swipe");

        String[] names = main_map.keySet().toArray(new String[0]);

        count++;
        if(count > 5)
            exit_button.setVisibility(View.VISIBLE);

        Context ctx = getApplicationContext();

        randomInt = random.nextInt(names.length);

        // beatify the random so that it don't repeat
        if(randomInt == lastInt) {
            randomInt++;
            if(randomInt >= names.length)
                randomInt -= names.length;
        }

        figure.setImageBitmap(main_map.get(names[randomInt]));
        figure.setTag(names[randomInt]);

        figure.setScaleX(1f);
        figure.setScaleY(1f);

        int randomInt2 = random.nextInt(colors.size());
        figure.setColorFilter(colors.get(randomInt2));

        figure.setTranslationX(centerX);
        figure.setTranslationY(ctx.getResources().getDisplayMetrics().heightPixels + figure.getMeasuredHeight());
        figure.animate()
                .translationX(centerX)
                .translationY(centerY)
                .setDuration(300)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                figure.setOnTouchListener(figureTouchListener);
            }
        }, 300);

        figure.setVisibility(View.VISIBLE);
        root.requestLayout();
    }

    public void onDataFailed() {
        finish();
    }

    private View.OnTouchListener figureTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Context ctx = getApplicationContext();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    v.clearAnimation();
                    v.setScaleX(0.9f);
                    v.setScaleY(0.9f);
                    dx = v.getTranslationX() - event.getRawX();
                    dy = v.getTranslationY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.clearAnimation();
                    v.setScaleX(0.9f);
                    v.setScaleY(0.9f);
                    v.setTranslationX(event.getRawX() + dx);
                    v.setTranslationY(event.getRawY() + dy);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setScaleX(1f);
                    v.setScaleY(1f);

                    collision c = checkCollision(v.getTranslationX(), v.getTranslationY());
                    if(c != null) {
                        int[] pos = new int[2];
                        c.view.getLocationOnScreen(pos);
                        pos[1] -= Converter.Pixels(ctx, 24);
                        v.animate()
                                .translationX(pos[0])
                                .translationY(pos[1])
                                .setDuration(300)
                                .start();
                        v.setOnTouchListener(null);
                        Toast.makeText(ctx, c.tag, Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkTagAndProcess(c.tag);
                            }
                        }, 300);

                        break;
                    }

                    if (v.getAnimation() == null) {
                        animateViewToCenter(v);
                    }
                    return false;
            }

            return true;
        }
    };

    private collision checkCollision(float x, float y) {
        float minDistance = -1;
        String tag = "";
        View outView = null;

        for(View v : holesList) {
            int[] pos = new int[2];
            v.getLocationOnScreen(pos);

            float distance = (float) Math.sqrt( Math.pow(x-pos[0], 2)+Math.pow(y-pos[1], 2) );
            if (distance < minDistance || minDistance == -1) {
                minDistance = distance;
                tag = v.getTag().toString();
                outView = v;
            }
        }

        if(minDistance != -1 && minDistance < Converter.Pixels(getApplicationContext(), 100)) {
            collision col = new collision();
            col.distance = minDistance;
            col.tag = tag;
            col.view = outView;

            return col;
        }

        return null;
    }

    private class collision {
        public float distance;
        public String tag;
        public View view;
    }

    private void checkTagAndProcess(String tag) {
        if(tag.equals(figure.getTag())) {
            if(speechReady)
                Speech.getInstance().say(tag);

            //congratsView.setVisibility(View.VISIBLE);
            //new Handler().postDelayed(new Runnable() {
            //    @Override
            //    public void run() {
            //        congratsView.setVisibility(View.INVISIBLE);
            //    }
            //}, 1500);

            MyApplication.getPoolInstance().play("fairy");

            lastInt = randomInt;
            figure.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(500)
                    .start();
            new Handler().postDelayed(this::showFigure, 1000);
        }
        else {
            if(speechReady)
                Speech.getInstance().say("Попробуй еще раз");

            figure.animate()
                    .rotation(360)
                    .setDuration(500)
                    .start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    figure.setRotation(0);
                    figure.setOnTouchListener(figureTouchListener);
                    animateViewToCenter(figure);
                }
            }, 1000);
        }
    }

    private void animateViewToCenter(View v) {
        v.animate().translationX(centerX).translationY(centerY).setDuration(300).start();
    }
}