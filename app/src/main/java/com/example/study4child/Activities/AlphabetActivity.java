 package com.example.study4child.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.study4child.Custom.AlphabetCard;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.AlphabetLoader;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AlphabetActivity extends AppCompatActivity {

    private ArrayList<AlphabetData> cards = new ArrayList<>(); // list of downloaded cards// private
    private int currentIndex = -1;

    private ArrayList<View> loadedCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        //TODO: add ViewPager2 or something else to animate cards switch from current to next


        // get path from intent
        String path = getIntent().getStringExtra("path");

        new AlphabetLoader().Load(ctx, AlphabetActivity.this, path); // (1)
    }

    public void onDataObtained(ArrayList<DatabaseReference> data) { // (2)
        for(DatabaseReference ref : data) {
            cards.add(new AlphabetData(getApplicationContext(), ref, AlphabetActivity.this));
        }

        LoadNextCard();
    }

    public void onDataFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void LoadNextCard() { // (3)
        currentIndex++;

        cards.get(currentIndex).Load();
    }

    public void onCardLoaded(AlphabetData data) { // (4)
        if(data == null) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage(getApplicationContext().getString(R.string.error))
                    .setNeutralButton("Окей", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            finish();
        }

        new CardTask().execute(data);
    }

    private class CardTask extends AsyncTask<AlphabetData, Integer, AlphabetCard> { // (5)
        private AlphabetActivity listener;
        public CardTask(AlphabetActivity _listener) {
            listener = _listener;
        }
        public CardTask() {
            this(null);
        }

        @Override
        protected AlphabetCard doInBackground(AlphabetData... input) {
            return new AlphabetCard(getApplicationContext(), input[0]);
        }

        @Override
        protected void onPostExecute(AlphabetCard card) {
            super.onPostExecute(card);

            loadedCards.add(card);

            if(listener != null) {
                listener.switchCards();
            }
        }
    }

    public void switchCards() { // (6)

    }
}