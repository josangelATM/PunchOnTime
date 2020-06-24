package com.example.punchontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectEmploye extends AppCompatActivity {
    private static final String TAG = "DocSnippets";
    private String UserUID="";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_employe);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> spinnerArray =  new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map data=document.getData();
                                spinnerArray.add(data.get("Nombre").toString()+" "+data.get("Apellido").toString());
                            }
                            populateEmployeeSpn(spinnerArray);
                        } else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    protected void onStart(){
        super.onStart();
        Log.d("Entre", "Entre a onStart()");

        Button btMostrarInfo=findViewById(R.id.btnMostrarInfo);

        btMostrarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Entre", "Entre a MostrarInfo");
                Spinner spinner = (Spinner)findViewById(R.id.spnEmployees);
                String selectedName = spinner.getSelectedItem().toString();

                String patternNames = "(\\w*)\\s(\\w*)";
                Pattern r = Pattern.compile(patternNames);
                Matcher m = r.matcher(selectedName);
                String name="";
                String lastname="";
                if (m.find( )) {
                    name=m.group(1);
                    lastname=m.group(2);
                }

                db.collection("users")
                        .whereEqualTo("Nombre", name).whereEqualTo("Apellido",lastname)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        displayEmployeeInfo(document.getData());
                                        Map mapData=document.getData();
                                        UserUID=mapData.get("UID").toString();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        Button btDetalles=findViewById(R.id.btnVerDetalles);

        btDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectEmploye.this, timeSheet.class);
                intent.putExtra("UID", UserUID);
                intent.putExtra("Mode","HR");
                startActivity(intent);
            }
        });
    }

    public void populateEmployeeSpn( List<String> spinnerArray){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spnEmployees);
        sItems.setAdapter(adapter);

    }

    public void displayEmployeeInfo(Map data){
        String horasTrabajadas = data.get("workedHours").toString();
        String horasLunch = data.get("lunchHours").toString();
        String RxH = data.get("RxH").toString();

        float totalAPagarSinRestar = Float.parseFloat(horasTrabajadas)*Float.parseFloat(RxH);
        float totalARestar= Float.parseFloat(horasLunch)*Float.parseFloat(RxH);

        float totalTotal= totalAPagarSinRestar-totalARestar;

        String totalFinalString= String.valueOf(totalTotal);

        BigDecimal bd = new BigDecimal(horasTrabajadas).setScale(2, RoundingMode.HALF_UP);
        horasTrabajadas=bd.toString();
        bd = new BigDecimal(horasLunch).setScale(2, RoundingMode.HALF_UP);
        horasLunch=bd.toString();
        bd = new BigDecimal(totalFinalString).setScale(2, RoundingMode.HALF_UP);
        totalFinalString=bd.toString();

        TextView tvHorasTrabajadas = findViewById(R.id.tvHHorasWorked);
        TextView tvHorasLunch = findViewById(R.id.tvHHorasLunch);
        TextView tvRxH = findViewById(R.id.tvPagoXHora);
        TextView tvTotal = findViewById(R.id.tvPagoTotal);

        tvHorasTrabajadas.setText(horasTrabajadas);
        tvHorasLunch.setText(horasLunch);
        tvRxH.setText(RxH);
        tvTotal.setText(totalFinalString);

    }

}

