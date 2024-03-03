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

                list = Sort(list);

                listener.onDataObtained(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                listener.onDataFailed(databaseError.getMessage());
            }
        });
    }

    private ArrayList<DatabaseReference> Sort(ArrayList<DatabaseReference> input) {
        // if size == 1 then just return
        if(input.size() == 1)
            return input;

        // bubble sort
        ArrayList<DatabaseReference> sorted = new ArrayList<>();
        for (DatabaseReference d:
             input) {
            sorted.add(d);
        }

        int max_iterations = 1000;
        int i = 0;

        while(i <= max_iterations) {
            i++;

            boolean swapped = false;

            for(int j = 0; j < sorted.size()-1; j++) {
                if(Character.getNumericValue(sorted.get(j).getKey().charAt(0)) >
                        Character.getNumericValue(sorted.get(j+1).getKey().charAt(0))) {
                    DatabaseReference temp = sorted.get(j);
                    sorted.set(j, sorted.get(j+1));
                    sorted.set(j+1, temp);
                    swapped = true;
                }
            }

            if(!swapped) {
                break;
            }
        }

        // assuming that the list is sorted
        return sorted;
    }
}
