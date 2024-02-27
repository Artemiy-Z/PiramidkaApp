package com.example.study4child.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.study4child.Activities.AlphabetActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

public class AlphabetData implements ImageDownloadInterface {

    private Context ctx;
    private DatabaseReference reference;
    private AlphabetActivity listener;

    public String letter;
    public String title;
    public Bitmap image;

    public AlphabetData(Context _ctx, DatabaseReference _reference, AlphabetActivity _listener) {
        ctx = _ctx;
        reference = _reference;
        listener = _listener;
    }

    public void Load() {
        letter = reference.getKey();

        reference.child("answer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                title = dataSnapshot.getValue(String.class);
                onDataRecieved();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                onDataFailed();
            }
        });

        reference.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                ImageStorageLoader.loadFromUrl( ctx, dataSnapshot.getValue(String.class),
                        (ImageDownloadInterface) AlphabetData.this );
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                onDataFailed();
            }
        });
    }

    @Override
    public void onImageObtained(Bitmap _image) {
        image = _image;
        onDataRecieved();
    }

    private void onDataRecieved() {
        if(title == null || image == null)
            return;

        listener.onCardLoaded(AlphabetData.this);
    }

    private void onDataFailed() {
        listener.onCardLoaded(null);
    }

}
