package com.example.punchontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.Map;

public class timeSheet extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet);
        Intent intent=this.getIntent();
        final String UserUID = intent.getStringExtra("UID");

        CalendarView calendarView=(CalendarView) findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                DocumentReference docRef = db.collection("dates").document(UserUID+String.valueOf(dayOfMonth)+String.valueOf(month)+String.valueOf(year));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                displayData(document.getData());
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


            }
        });


        TextView tvReclamoEmail = (TextView) findViewById(R.id.tvReclamo);
        tvReclamoEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String to="joseangel19.lol@gmail.com";
                String subject="Reclamo";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Elija su cliente de Correo:"));

            }
        });
    }

    public void displayData(Map data){
        Log.d("Data Map", data.toString());

        String horaSalida= data.get("Salida").toString();
        String lunchTime= data.get("TLunch").toString();
        String workTime= data.get("TTrabajado").toString();
        String horaEntrada= data.get("Entrada").toString();

        TextView tvEntrada = findViewById(R.id.tvHoraEntrada);
        TextView tvSalida = findViewById(R.id.tvHoraSalida);
        TextView tvLunch = findViewById(R.id.tvTiempoLunch);
        TextView tvWork = findViewById(R.id.tvTiempoTrabajado);

        tvEntrada.setText(horaEntrada);
        tvSalida.setText(horaSalida);
        tvLunch.setText(lunchTime);
        tvWork.setText(workTime);

    }
}
