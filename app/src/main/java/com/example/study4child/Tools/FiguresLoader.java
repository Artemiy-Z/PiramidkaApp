package com.example.study4child.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.study4child.Activities.FiguresActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FiguresLoader {

    private Context ctx;
    private FiguresActivity listener;
    private HashMap<String, Bitmap> mains = new HashMap<>();
    private HashMap<String, Bitmap> holes = new HashMap<>();
    private int size;

    public void onMainObtained(String name, Bitmap image) {
        mains.put(name, image);
        checkCompletion();
    }
    public void onHoleObtained(String name, Bitmap image) {
        holes.put(name, image);
        checkCompletion();
    }

    private void checkCompletion() {
        if(mains.keySet().size() < size) {
            return;
        }
        if(holes.keySet().size() < size) {
            return;
        }

        listener.onDataObtained(mains, holes);
    }

    public void Load(Context _ctx, FiguresActivity _listener, String path) {
        ctx = _ctx;
        listener = _listener;

        DatabaseReference root = FirebaseFactory.getDataBase(ctx).getReference();

        root.child(path).child("figures").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    new MainLoader(FiguresLoader.this, child.getRef()).Load();
                    new HoleLoader(FiguresLoader.this, child.getRef()).Load();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                listener.onDataFailed();
            }
        });
    }

    private class MainLoader implements ImageDownloadInterface {

        private FiguresLoader parent;
        private DatabaseReference ref;

        public MainLoader(FiguresLoader _parent, DatabaseReference _ref) {
            parent = _parent;
            ref = _ref;
        }

        private void Load() {
            ref.child("main").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue(String.class);
                    ImageStorageLoader.loadFromUrl(ctx, url, MainLoader.this);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                    Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onDataFailed();
                }
            });
        }

        @Override
        public void onImageObtained(Bitmap _image) {
            parent.onMainObtained(ref.getKey(), _image);
        }
    }
    private class HoleLoader implements ImageDownloadInterface {

        private FiguresLoader parent;
        private DatabaseReference ref;

        public HoleLoader(FiguresLoader _parent, DatabaseReference _ref) {
            parent = _parent;
            ref = _ref;
        }

        private void Load() {
            ref.child("hole").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue(String.class);
                    ImageStorageLoader.loadFromUrl(ctx, url, HoleLoader.this);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                    Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onDataFailed();
                }
            });
        }

        @Override
        public void onImageObtained(Bitmap _image) {
            parent.onHoleObtained(ref.getKey(), _image);
        }
    }
}
