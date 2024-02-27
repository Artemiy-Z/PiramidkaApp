package com.example.study4child.Custom;

import android.content.Context;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.Converter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import org.jetbrains.annotations.NotNull;

public class AlphabetCard extends CardView {

    public MediaStore.Audio audio = null;

    public AlphabetCard(@NonNull @NotNull Context ctx, AlphabetData data) {
        super(ctx);

        this.setBackground(ctx.getDrawable(R.drawable.button_bg));
        int dp4 = Converter.Pixels(ctx, 4);
        this.setPadding(dp4, dp4, dp4, dp4);

        ShapeableImageView image = new ShapeableImageView(ctx);
        int cornerFamily = CornerFamily.ROUNDED;
        image.setShapeAppearanceModel(image.getShapeAppearanceModel().toBuilder()
                .setAllCorners(cornerFamily, 16f)
                .build());
        image.setImageBitmap(data.image);

        this.addView(image);
    }
}
