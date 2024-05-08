package com.example.study4child.Activities;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import androidx.viewpager2.widget.ViewPager2;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.animation.ObjectAnimator;
import android.view.View;
import com.example.study4child.Tools.Converter;
import com.example.study4child.Custom.CustomVP2Adapter;
import com.example.study4child.Custom.IntroPageFragment;
import com.example.study4child.R;
import com.example.study4child.Tools.MyActivity;
import com.example.study4child.Tools.MyApplication;
import com.example.study4child.Tools.SFXPool;
import pl.droidsonroids.gif.GifImageView;

import java.lang.Runnable;


public class SplashIntroActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		Context ctx = getApplicationContext();

		// Initializing SFX and putting it into application, so all the activities can access it
		SFXPool pool = new SFXPool(ctx);
		MyApplication.setPoolInstance(pool);

		// start the music
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				  MyApplication.getPoolInstance().backgroundMusic("music");
			}
		}, 2500);

		//root view
		ConstraintLayout root = new ConstraintLayout(ctx);
		root.setId(R.id.root);
		ConstraintSet root_set = new ConstraintSet();

		// setting up splash screen
		RelativeLayout splash_container = 
			new RelativeLayout(ctx);
		
		RelativeLayout.LayoutParams icon_params = 
			new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.MATCH_PARENT,
			RelativeLayout.LayoutParams.MATCH_PARENT);
		
		splash_container.setLayoutParams(icon_params);
		splash_container.setGravity(Gravity.CENTER);
		splash_container.setBackgroundColor(ctx.getResources().getColor(R.color.cyan));
        splash_container.setId(R.id.splash);
		
		ImageView ring = new ImageView(ctx);
		RelativeLayout.LayoutParams ring_params = new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 128f), Converter.Pixels(ctx, 128f));
		ring_params.addRule(RelativeLayout.CENTER_IN_PARENT);
		ring.setLayoutParams(ring_params);
		ring.setImageResource(R.drawable.ring);
		ring.setColorFilter(ctx.getResources().getColor(R.color.red));

		ImageView piramid = new ImageView(ctx);
		RelativeLayout.LayoutParams piramid_params = new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 45f), Converter.Pixels(ctx, 70f));
		piramid_params.addRule(RelativeLayout.CENTER_IN_PARENT);
		piramid.setLayoutParams(piramid_params);
		piramid.setImageResource(R.drawable.piramid_full);
		piramid.setColorFilter(ctx.getResources().getColor(R.color.red));

		GifImageView click_me = new GifImageView(ctx);
		click_me.setImageResource(R.drawable.click);
		RelativeLayout.LayoutParams clickParams = new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 140), Converter.Pixels(ctx, 140));
		clickParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		click_me.setLayoutParams(clickParams);

		splash_container.addView(ring);
		splash_container.addView(piramid);
		splash_container.addView(click_me);

		//setting up ViewPager
		LinearLayout linear_container = new LinearLayout(ctx);
		linear_container.setLayoutParams(new LinearLayout.LayoutParams( ctx.getResources().getDisplayMetrics().widthPixels,
										 			ctx.getResources().getDisplayMetrics().heightPixels + Converter.Pixels(ctx, 42f)));

		linear_container.setOrientation(LinearLayout.VERTICAL);
        linear_container.setId(R.id.linear);
		
		ImageView wave = new ImageView(ctx);
		wave.setLayoutParams(new RelativeLayout.LayoutParams(
			ctx.getResources().getDisplayMetrics().widthPixels, 
			Converter.Pixels(ctx, 42f)));
		wave.setImageResource(R.drawable.wave);
		wave.setScaleType(ImageView.ScaleType.FIT_XY);

		ViewPager2 vp = new ViewPager2(ctx);
		ViewGroup.LayoutParams vp_params = new ViewGroup.LayoutParams(ctx.getResources().getDisplayMetrics().widthPixels,
										 			ctx.getResources().getDisplayMetrics().heightPixels);
		vp.setLayoutParams(vp_params);

		CustomVP2Adapter adapter = new CustomVP2Adapter(this);
		
		adapter.addItem(new IntroPageFragment(ctx, R.drawable.background1, R.string.intro_page1, R.color.red, 0, 0, R.string.app_name));
		adapter.addItem(new IntroPageFragment(ctx, R.drawable.background2, R.string.intro_page2, R.color.green, 1, 140));
		adapter.addItem(new IntroPageFragment(ctx, R.drawable.background3, R.string.intro_page3, R.color.yellow, 2, 140));
		adapter.addItem(new IntroPageFragment(ctx, R.drawable.background4, R.string.intro_page4, R.color.red, 0, 0));

		// button to go to mainpage
		Button button = new Button(ctx);
		button.setLayoutParams(new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 128f), Converter.Pixels(ctx, 64f)));
		button.setId(R.id.bottom_button);
        button.setBackgroundResource(R.drawable.button_bg);
		button.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
		button.setText(R.string.button_start);
		button.setTextColor(ctx.getResources().getColor(R.color.red));
		button.setTextSize(18);
		button.setTypeface(ctx.getResources().getFont(R.font.font_family1));

		root.addView(button);

		GifImageView swipe = new GifImageView(ctx);
		swipe.setImageResource(R.drawable.swipe);
		RelativeLayout.LayoutParams swipeParams = new RelativeLayout.LayoutParams(Converter.Pixels(ctx, 200), Converter.Pixels(ctx, 112.5f));
		swipeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		swipe.setLayoutParams(swipeParams);
        swipe.setVisibility(View.INVISIBLE);
		swipe.setId(R.id.swipe);

		// finished setup pages and adding them to layout
		vp.setAdapter(adapter);
		vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
                swipe.setVisibility(View.GONE);
                if(position == adapter.getItemCount()-1) {
                    button.setVisibility(View.VISIBLE);
                }
				else {
                    button.setVisibility(View.GONE);
                }
			}
		});

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				MyApplication.getPoolInstance().play("bubble");
				startActivity(new Intent(SplashIntroActivity.this, MainPageActivity.class));
				finish();
			}
		});

		linear_container.addView(wave);
		linear_container.addView(vp);

		// adding onclick to splash screen to animate intro on the screen
		splash_container.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				MyApplication.getPoolInstance().play("swipe");

				ObjectAnimator anim = ObjectAnimator.ofFloat(linear_container, "translationY", -wave.getHeight());
				anim.setDuration(900);
				anim.start();

                swipe.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setVisibility(View.VISIBLE);
                    }
                }, 1100);

                splash_container.setOnClickListener(null);
			}
		});

		//add all views to root and display it
		root.addView(splash_container);
		root.addView(linear_container);
		root.addView(swipe);

		root_set.clone(root);

		root_set.connect(button.getId(), ConstraintSet.START, root.getId(), ConstraintSet.START, 0);
		root_set.connect(button.getId(), ConstraintSet.END, root.getId(), ConstraintSet.END, 0);
		root_set.connect(button.getId(), ConstraintSet.BOTTOM, root.getId(), ConstraintSet.BOTTOM, Converter.Pixels(ctx, 112f));

		root_set.connect(swipe.getId(), ConstraintSet.START, root.getId(), ConstraintSet.START, 0);
		root_set.connect(swipe.getId(), ConstraintSet.END, root.getId(), ConstraintSet.END, 0);
		root_set.connect(swipe.getId(), ConstraintSet.BOTTOM, root.getId(), ConstraintSet.BOTTOM, Converter.Pixels(ctx, 256f));

		root_set.applyTo(root);

		setContentView(root);

		// moving intro off screen
		linear_container.post(new Runnable() {
			@Override
			public void run() {
				linear_container.setTranslationY(linear_container.getMeasuredHeight());
				linear_container.requestLayout();
			}
		});
    }
}