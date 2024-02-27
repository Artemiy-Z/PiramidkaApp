package com.example.study4child.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import com.example.study4child.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

public class GamePreviewData implements ImageDownloadInterface {
    public String title;
    public Bitmap image;
    public String gameplay_root;
    public String root_name;

    private GameLoadLevelPreviews listener;
    private int index;

    public int getIndex() {
        return index;
    }

    @Override
    public void onImageObtained(Bitmap _image) {
        image = _image;
        onDataRecieved();
    }

    private void onDataRecieved() {
        if(title.equals("") || image == null)
            return;
        listener.onDataObtained(GamePreviewData.this);
    }

    public void LoadData(Context ctx, DatabaseReference root, int _index, GameLoadLevelPreviews _listener) {
        listener = _listener;
        index = _index;

        root_name = root.getKey();

        gameplay_root = root_name + "/gameplay";

        DatabaseReference preview = root.child("preview");
        preview.child("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                try {
                    title = dataSnapshot.getValue(String.class);
                    onDataRecieved();
                }
                catch (Exception e) {
                    listener.onDataFailed(e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                listener.onDataFailed(databaseError.getMessage());
            }
        });

        preview.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                try {
                    ImageStorageLoader.loadFromUrl( ctx, dataSnapshot.getValue(String.class),
                                                   (ImageDownloadInterface) GamePreviewData.this );
                }
                catch (Exception e) {
                    listener.onDataFailed(e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                listener.onDataFailed(databaseError.getMessage());
            }
        });
    }
}
