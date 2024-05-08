package com.example.study4child.Tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;
import com.example.study4child.R;

import java.util.HashMap;

import static android.media.AudioAttributes.USAGE_MEDIA;

public class SFXPool {
    private Context ctx;
    private SoundPool pool;
    private final HashMap<String, Integer> ids = new HashMap<>();
    private int musicStreamID;

    private void Build() { // in this method you set up sound pool and load sounds that you'll need
        pool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();

        ids.put("fairy", pool.load(ctx, R.raw.magic_effect, 1));
        ids.put("ding", pool.load(ctx, R.raw.ding, 1));
        ids.put("bubble", pool.load(ctx, R.raw.water, 1));
        ids.put("coin", pool.load(ctx, R.raw.coin, 1));
        ids.put("music", pool.load(ctx, R.raw.bg_loop, 1));
        ids.put("swipe", pool.load(ctx, R.raw.swipe, 1));
        ids.put("open", pool.load(ctx, R.raw.open, 1));
    }
    public void pause() {
        pool.autoPause();
    }

    public void resume() {
        pool.autoResume();
    }

    public void play(String name) {
        Integer id = ids.get(name);
        if(id == null) {
            Toast.makeText(ctx, "SFX, The sound you're trying to play (" + name + ") doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }
        pool.play(id, 1, 1, 1, 0, 1.0f);
    }
    public void backgroundMusic(String name) {
        Integer id = ids.get(name);
        if(id == null) {
            Toast.makeText(ctx, "SFX, The sound you're trying to play (" + name + ") doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }
        pool.play(id, 0.8f, 0.8f, 1, -1, 1.0f);
    }

    public SFXPool(Context context) {
        ctx = context;
        this.Build();
    }
}
