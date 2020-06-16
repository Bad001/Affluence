package com.example.affluence;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class myService extends Service {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Marker");
    private HashMap<String, Object> updatedValues = new HashMap<>();
    private Bundle extras;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        extras = intent.getExtras();
        /*updatedValues.put("latitude",marker.getLatitude());
        updatedValues.put("longitude",marker.getLongitude());
        reference.child(marker.getId()).child(marker.getId()).updateChildren(updatedValues);
        reference.updateChildren(updatedValues);
        */
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}