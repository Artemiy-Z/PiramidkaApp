package com.example.study4child.Custom;

import android.content.Context;
import android.widget.TextView;

public class CountTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int count;
    private int target;

    private onCountListener mOnCountListener;

    public CountTextView(Context ctx) {
        super(ctx);
    }

    public void reset(int _target) {
        target = _target;
        count = 0;
        this.setText( String.valueOf(count) );
    }

    public void countUp() {
        count++;
        this.setText( String.valueOf(count) );
        if(count == target)
            this.mOnCountListener.onComplete(count);
    }

    public void setOnCountListener(onCountListener listener) {
        mOnCountListener = listener;
    }
    public interface onCountListener {

        public void onComplete(int count);
    }
}
