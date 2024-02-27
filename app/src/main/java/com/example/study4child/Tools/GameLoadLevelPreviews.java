package com.example.study4child.Tools;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.study4child.Activities.GameListActivity;
import com.example.study4child.R;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GameLoadLevelPreviews {

    private GameListActivity listener;
    private ArrayList<GamePreviewData> data_list;
    private int data_list_expected_size;

    public void Start(Context ctx, GameListActivity _listener, int level) {

        listener = _listener;

        FirebaseDatabase firedata = FirebaseFactory.getDataBase(ctx);
        DatabaseReference names = null;
        switch(level) {
            case 1:
                names = firedata.getReference(ctx.getString(R.string.database_list_name_1)); break;
            case 2:
                names = firedata.getReference(ctx.getString(R.string.database_list_name_2)); break;
            case 3:
                names = firedata.getReference(ctx.getString(R.string.database_list_name_3)); break;
        }

        assert names != null;
        names.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                try {
                    data_list = new ArrayList<>();
                    String value = dataSnapshot.getValue(String.class);
                    assert value != null;
                    String[] keys = value.split(";");
                    for (int i = 0; i < keys.length; i++) {
                        // start card loading sequence for each name in the list
                        new GamePreviewData().LoadData(ctx, firedata.getReference(keys[i]), i, GameLoadLevelPreviews.this);
                    }
                    data_list_expected_size = keys.length;
                }
                catch (Exception e) {
                    Toast.makeText(listener, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                listener.onDataFailed(databaseError.getMessage());
            }
        });
    }

    public void onDataFailed(String errorMessage) {
        Toast.makeText(listener, errorMessage, Toast.LENGTH_SHORT).show();
        data_list.add(null);
    }

    public void onDataObtained(GamePreviewData data_item) {
        data_list.add(data_item);

        if(data_list.size() == data_list_expected_size) {
            Sort();
            listener.onDataObtained(data_list);
        }
    }

    private void Sort() {
        // if size == 1 then just return
        if(data_list.size() == 1)
            return;

        // bubble sort
        ArrayList<GamePreviewData> sorted = new ArrayList<>();
        for (GamePreviewData item:
             data_list) {
            if(item != null)
                sorted.add(item); // checking for null elements
        }

        int max_iterations = 1000;
        int i = 0;

        while(i <= max_iterations) {
            i++;

            boolean swapped = false;

            for(int j = 0; j < sorted.size()-1; j++) {
                if(sorted.get(j).getIndex() > sorted.get(j+1).getIndex()) {
                    GamePreviewData temp = sorted.get(j);
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
        data_list = sorted;
    }
}
