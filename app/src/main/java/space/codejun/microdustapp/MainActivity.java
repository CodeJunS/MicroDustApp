package space.codejun.microdustapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArcProgress pm25Progress = findViewById(R.id.pm25_progress);
        final ArcProgress pm10Prograss = findViewById(R.id.pm10_progress);

        final DocumentReference docRef = db.collection("AirCondition").document("MicroDust");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current PM 2.5: " + snapshot.getString("pm_25"));
                    Log.d(TAG, "Current PM 10: " + snapshot.getString("pm_10"));
                    // cutNumber(String.valueOf(snapshot.getData()));
                    String pm25 = snapshot.getString("pm_25");
                    String pm10 = snapshot.getString("pm_10");
                    pm25Progress.setProgress(Integer.parseInt(pm25));
                    pm10Prograss.setProgress(Integer.parseInt(pm10));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
}