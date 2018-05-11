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

import com.gelitenight.waveview.library.WaveView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private WaveHelper mWaveHelper;

    private static String TAG = "MainActivity";

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 5;

    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        waveView = findViewById(R.id.wave);
        mWaveHelper = new WaveHelper(waveView);
        final TextView textView = findViewById(R.id.dust);

        final DocumentReference docRef = db.collection("pm2_5").document("FpvuP0Plq2vmk8FSWMEh");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getString("pm2_5"));
                    // cutNumber(String.valueOf(snapshot.getData()));
                    String dust = snapshot.getString("pm2_5");
                    textView.setText(dust);
                    loadDustLevel(Integer.valueOf(dust));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void loadDustLevel(Integer level) {
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setBorder(mBorderWidth, mBorderColor);
        waveView.setWaveColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"));
        Log.d("RATIO", String.valueOf(waveView.getWaterLevelRatio()));

        if (level <= 15) {
            waveView.setWaveColor(Color.parseColor("#69F0AE"), Color.parseColor("#00E676"));
            waveView.setWaterLevelRatio(0.2f);
        } else if (level > 16) {
            waveView.setWaterLevelRatio(0.5f);
        }
        if (level > 30) {
            waveView.setWaterLevelRatio(0.8f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }
}