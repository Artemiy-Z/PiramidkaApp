package com.example.study4child.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.study4child.Activities.CountActivity;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CountLoadImages implements ImageDownloadInterface {
    private ArrayList<Bitmap> data_list = new ArrayList<>();
    private long expectedSize = 0;
    private CountActivity listener;

    @Override
    public void onImageObtained(Bitmap _image) {
        onImageLoaded(_image);
    }

    private void onImageLoaded(Bitmap image) {
        if(image == null) {
            expectedSize--;

            if(data_list.isEmpty() && expectedSize == 0) {
                listener.onDataFailed();
                return;
            }

            if(data_list.size() == expectedSize) {
                listener.onDataObtained(data_list);
            }

            return;
        }

        data_list.add(image);

        if(data_list.size() < expectedSize)
            return;

        listener.onDataObtained(data_list);
    }

    public void Load(Context ctx, CountActivity _listener, String path) {
        listener = _listener;

        FirebaseDatabase database = FirebaseFactory.getDataBase(ctx);
        DatabaseReference ref = database.getReference(path);

        ref.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                expectedSize = dataSnapshot.getChildrenCount();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LoadImage(snapshot.getRef());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                listener.onDataFailed();
            }
        });
    }

    private void LoadImage(DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                  String url = dataSnapshot.getValue(String.class);
                ImageStorageLoader.loadFromUrl(listener, url, CountLoadImages.this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                onImageLoaded(null);
            }
        });
    }
}
