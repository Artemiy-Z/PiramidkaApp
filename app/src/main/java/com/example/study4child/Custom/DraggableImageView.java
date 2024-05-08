package com.example.study4child.Custom;

import android.content.Context;

public class DraggableImageView extends androidx.appcompat.widget.AppCompatImageView {

    public DraggableImageView(Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        return false;
    }
}
