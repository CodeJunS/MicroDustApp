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

    private ArcProgress pm25Progress;
    private ArcProgress pm10Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        pm25Progress = findViewById(R.id.pm25_progress);
        pm10Progress = findViewById(R.id.pm10_progress);

        final DocumentReference docRef = db.collection("AirCondition").document("MicroDust");
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current PM 2.5: " + snapshot.getString("pm_25"));
                Log.d(TAG, "Current PM 10: " + snapshot.getString("pm_10"));

                float nNumber = Float.parseFloat(snapshot.getString("pm_25"));
                String pm25 = String.format("%.0f", nNumber);
                String pm10 = "0"; //nothing here

                initProgress(pm25, pm10);
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    public void initProgress(String pm_25, String pm_10) {

        int pm25 = Integer.parseInt(pm_25);
        int pm10 = Integer.parseInt(pm_10);

        if (0 <= pm25 && pm25 <= 15) {
            pm25Progress.setFinishedStrokeColor(Color.parseColor("#00B0FF"));
            pm25Progress.setTextColor(Color.parseColor("#00B0FF"));
        } else if (15 < pm25 && pm25 <= 35) {
            pm25Progress.setFinishedStrokeColor(Color.parseColor("#00E676"));
            pm25Progress.setTextColor(Color.parseColor("#00E676"));
        } else if (35 < pm25 && pm25 <= 75) {
            pm25Progress.setFinishedStrokeColor(Color.parseColor("#FF9100"));
            pm25Progress.setTextColor(Color.parseColor("#FF9100"));
        } else if (75 < pm25) {
            pm25Progress.setFinishedStrokeColor(Color.parseColor("#FF1744"));
            pm25Progress.setTextColor(Color.parseColor("#FF1744"));
        }
        pm25Progress.setProgress(pm25);
        pm10Progress.setProgress(pm10);
    }
}