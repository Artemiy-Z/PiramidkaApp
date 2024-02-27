package com.example.study4child.Tools;

import android.content.Context;
import com.example.study4child.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseFactory {
    public static FirebaseDatabase getDataBase(Context ctx) {
        return FirebaseDatabase.getInstance(ctx.getString(R.string.realtime_database));
    }

    public static FirebaseStorage getStorage(Context ctx) {
        return FirebaseStorage.getInstance(ctx.getString(R.string.cloud_storage));
    }
}
