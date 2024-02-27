package com.example.study4child.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import com.example.study4child.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

public class ImageStorageLoader {
    public static void loadFromUrl(Context ctx, String url, ImageDownloadInterface listener) {

        final Bitmap[] image = {null};

        // handle empty image
        if(url == null) {
            image[0] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.noimage);
            listener.onImageObtained(image[0]);
            return;
        }

        StorageReference ref = FirebaseFactory.getStorage(ctx).getReferenceFromUrl(url);

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(3 * ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image[0] = bitmap;
                        listener.onImageObtained(image[0]);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        image[0] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.noimage);
                        listener.onImageObtained(image[0]);
                    }
                });
    }
}
