package com.example.study4child.Tools;

import android.content.Context;
import androidx.annotation.NonNull;
import com.example.study4child.Activities.AlphabetActivity;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlphabetLoader {

    private AlphabetActivity listener;

    public void Load(Context ctx, AlphabetActivity _listener, String path) {
        listener = _listener;

        FirebaseDatabase database = FirebaseFactory.getDataBase(ctx);
        DatabaseReference ref = database.getReference(path).child("variants");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                ArrayList<DatabaseReference> list = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getRef());
                }

                listener.onDataObtained(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                listener.onDataFailed(databaseError.getMessage());
            }
        });
    }
}
