package com.example.study4child.Custom;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.rtp.AudioStream;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.study4child.R;
import com.example.study4child.Tools.MyApplication;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifOptions;

public class CongratulationGifImage extends GifImageView {

    public CongratulationGifImage(Context context) {
        super(context);

        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);

        this.setImageResource(R.drawable.magic);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility != VISIBLE) {
            return;
        }
        GifDrawable drawable = (GifDrawable) this.getDrawable();
        drawable.reset();
    }
}
