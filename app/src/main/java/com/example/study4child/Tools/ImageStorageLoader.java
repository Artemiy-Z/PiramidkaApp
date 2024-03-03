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

import java.util.Objects;

public class ImageStorageLoader {

    public static Bitmap emptyImage(Context ctx) {
        return BitmapFactory.decodeResource(ctx.getResources(), R.drawable.noimage);
    }

    public static void loadFromUrl(Context ctx, String url, ImageDownloadInterface listener) {

        try {
            if(url.equals("")) {
                throw new Exception("fucking location was empty");
            }

            StorageReference ref = FirebaseFactory.getStorage(ctx).getReferenceFromUrl(url);

            final long ONE_MEGABYTE = 1024 * 1024;
            ref.getBytes(3 * ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            listener.onImageObtained(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            listener.onImageObtained(emptyImage(ctx));
                        }
                    });
        }
        catch (Exception e) {
            listener.onImageObtained(emptyImage(ctx));
        }
    }
}
