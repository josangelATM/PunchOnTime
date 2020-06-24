package com.example.punchontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScreen extends AppCompatActivity {
    private static final String TAG = "DocSnippets";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Intent intent=this.getIntent();
        final String UserUID = intent.getStringExtra("UID");



        Map data;

        DocumentReference docRef = db.collection("users").document(UserUID.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getData(document.getData(),UserUID);

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Button btnChange = findViewById(R.id.btnCambiar);
        btnChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Spinner spinner = (Spinner)findViewById(R.id.spnOptionsWork);
                String newState = spinner.getSelectedItem().toString();

                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                getData(document.getData(),UserUID);

                                Map map = document.getData();

                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

                if (newState.equals("Iniciar turno")){
                    //newState="Tiempo de trabajo";
                    /*Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
                    String currentDateTimeString = sdf.format(d);*/

                    LocalTime nowTime= LocalTime.now();
                    String nowTimeString=nowTime.toString();
                    String patternHours = "^[0-9]+:[0-9]+";
                    Pattern r = Pattern.compile(patternHours);
                    Matcher m = r.matcher(nowTimeString);
                    String horaEntrada="";
                    TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                    tvHorasLunch.setText("00:00");

                    if (m.find( )) {
                        horaEntrada=m.group(0);
                    }

                    docRef = db.collection("users").document(UserUID);
                    docRef
                            .update("horaEntrada", horaEntrada)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                    docRef
                            .update("lunchTomado", false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                    docRef
                            .update("horaLunchI", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                    docRef
                            .update("horaLunchF", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                    docRef
                            .update("horaSalida", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });

                    docRef
                            .update("tiempoTrabajado", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                    docRef
                            .update("tiempoLunch", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });
                }

                if (newState.equals("Finalizar turno")){

                    LocalTime nowTime= LocalTime.now();
                    String nowTimeString=nowTime.toString();
                    String patternHours = "^[0-9]+:[0-9]+";
                    Pattern r = Pattern.compile(patternHours);
                    Matcher m = r.matcher(nowTimeString);
                    String horaSalida="";

                    if (m.find( )) {
                        horaSalida=m.group(0);
                    }

                    Log.d("String", horaSalida);

                    docRef = db.collection("users").document(UserUID);
                    docRef
                            .update("horaSalida", horaSalida)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document Finalizar", e);
                                }
                            });

                    docRef = db.collection("users").document(UserUID.toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    populateDates(document.getData(),UserUID);

                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    updateTimers(document.getData(),UserUID);
                                    updateWorkedHours(document.getData(),UserUID);
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

                if (newState.equals("Tiempo de trabajo")){
                    /*Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
                    String currentDateTimeString = sdf.format(d);*/

                    LocalTime nowTime= LocalTime.now();
                    String nowTimeString=nowTime.toString();
                    String patternHours = "^[0-9]+:[0-9]+";
                    Pattern r = Pattern.compile(patternHours);
                    Matcher m = r.matcher(nowTimeString);
                    String horaLunchFinal="";

                    if (m.find( )) {
                        horaLunchFinal=m.group(0);
                    }

                    docRef = db.collection("users").document(UserUID);
                    docRef
                            .update("horaLunchF", horaLunchFinal)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });

                    docRef
                            .update("lunchTomado", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentUpdated yeyo!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Ahh verg*, no se subió.", e);
                                }
                            });

                }

                if (newState.equals("Tiempo de lunch")){

                    /*Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
                    String currentDateTimeString = sdf.format(d);*/
                    TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                    tvHorasLunch.setText("00:00");

                    LocalTime nowTime= LocalTime.now();
                    String nowTimeString=nowTime.toString();
                    String patternHours = "^[0-9]+:[0-9]+";
                    Pattern r = Pattern.compile(patternHours);
                    Matcher m = r.matcher(nowTimeString);
                    String horaInicioLunch="";

                    if (m.find( )) {
                        horaInicioLunch=m.group(0);
                    }

                    Log.d("String", horaInicioLunch);

                    docRef = db.collection("users").document(UserUID);
                    docRef
                            .update("horaLunchI", horaInicioLunch)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document Finalizar", e);
                                }
                            });
                }

                docRef = db.collection("users").document(UserUID);
                docRef
                        .update("CurrentState", newState)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.Help:

                Intent intent = new Intent(MainScreen.this, Help.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    protected  void onStart(){
        super.onStart();
        Intent intent=this.getIntent();
        final String UserUID = intent.getStringExtra("UID");

        ImageButton ibUpdateClock = (ImageButton)findViewById(R.id.ibtUpdateClock);
        ibUpdateClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainScreen.this, "Tiempos actualizados", Toast.LENGTH_SHORT).show();
                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                updateTimers(document.getData(),UserUID);
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

        ImageButton ibTimeSheet = findViewById(R.id.ibtTimeSheet);

        ibTimeSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, timeSheet.class);
                intent.putExtra("UID", UserUID);
                intent.putExtra("Mode","Emp");
                startActivity(intent);

            }
        });

    }

    public void getData(Map data, String UserUID){
        writeHello(data.get("Nombre").toString());

        String horaEntrada= data.get("horaEntrada").toString();
        String horaLunchI= data.get("horaLunchI").toString();
        String horaLunchF= data.get("horaLunchF").toString();
        String tiempoLunch= data.get("tiempoLunch").toString();
        String tiempoTrabajado= data.get("tiempoTrabajado").toString();

        writeCurrentStateAndPopulateSpinner(data.get("CurrentState").toString(),Boolean.parseBoolean(data.get("lunchTomado").toString()));

        writeTimers(horaEntrada,horaLunchI,horaLunchF,tiempoLunch,tiempoTrabajado,UserUID,Boolean.parseBoolean(data.get("lunchTomado").toString()));
    }

    public void populateDates(Map data, String UserUID){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String horaEntrada= data.get("horaEntrada").toString();
        String horaSalida= data.get("horaSalida").toString();
        String tiempoLunch= data.get("tiempoLunch").toString();
        String finalTotalTimeWork= "";

        if(!horaEntrada.equals("")){
            LocalTime horaEntradaLocal = LocalTime.parse(horaEntrada);
            LocalTime nowTime= LocalTime.now();
            nowTime=LocalTime.parse(nowTime.toString());
            Duration dur = Duration.between(horaEntradaLocal,nowTime);
            String duration = dur.toString();
            String patternHours = "";
            if(dur.toHours()==0){
                patternHours="PT([0-9]+)M";
            }
            else{
                patternHours = "([0-9]+)H([0-9]+)M";
            }

            Pattern r = Pattern.compile(patternHours);
            Matcher m = r.matcher(duration);
            String hoursDurationWork="";
            String minutesDurationWork="";


            Log.d("Value of Duration",String.valueOf(dur.toHours()));

            if(dur.toHours()==0){
                if (m.find( )) {
                    minutesDurationWork=m.group(1);
                }
            }
            else{
                if (m.find( )) {
                    hoursDurationWork=m.group(1);
                    minutesDurationWork=m.group(2);
                }
            }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/


            if (dur.toHours()==0) {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = "00:" + "0" + minutesDurationWork;
                } else {

                    if (dur.toMinutes()==0){
                        finalTotalTimeWork = "00:00";
                    }

                    else {
                        finalTotalTimeWork = "00:" + minutesDurationWork;
                    }

                }

            }
            else {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = hoursDurationWork + ":0" + minutesDurationWork;
                } else {
                    finalTotalTimeWork = hoursDurationWork +":"+ minutesDurationWork;
                }
            }

        }



        Map<String, Object> dateData = new HashMap<>();
        dateData.put("Entrada",horaEntrada);
        dateData.put("Salida",horaSalida);
        dateData.put("TLunch",tiempoLunch);
        dateData.put("TTrabajado",finalTotalTimeWork);




        db.collection("dates").document(UserUID+String.valueOf(day)+String.valueOf(month)+String.valueOf(year))
                .set(dateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toast=Toast.makeText(getApplicationContext(),"Populate First", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        //toast.show();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    public void writeHello(String employeeName){
        TextView labelHello= findViewById(R.id.tvHello);
        labelHello.setText("Hola, "+employeeName+".");
    }

    public void writeTimers( String horaEntrada, String horaLunchI, String horaLunchF,String tiempoLunch, String tiempoTrabajado,String UserUID,Boolean lunchTomado){

        if(!horaEntrada.equals("")){
            LocalTime horaEntradaLocal = LocalTime.parse(horaEntrada);
            LocalTime nowTime= LocalTime.now();
            nowTime=LocalTime.parse(nowTime.toString());
            Duration dur = Duration.between(horaEntradaLocal,nowTime);
            String duration = dur.toString();
            String patternHours = "";
            if(dur.toHours()==0){
                patternHours="PT([0-9]+)M";
            }
            else{
                patternHours = "([0-9]+)H([0-9]+)M";
            }

            Pattern r = Pattern.compile(patternHours);
            Matcher m = r.matcher(duration);
            String hoursDurationWork="";
            String minutesDurationWork="";


            Log.d("Value of Duration",String.valueOf(dur.toHours()));

            if(dur.toHours()==0){
                if (m.find( )) {
                    minutesDurationWork=m.group(1);
                }
            }
            else{
                if (m.find( )) {
                    hoursDurationWork=m.group(1);
                    minutesDurationWork=m.group(2);
                }
            }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
            String finalTotalTimeWork="";

            if (dur.toHours()==0) {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = "00:" + "0" + minutesDurationWork;
                } else {

                    if (dur.toMinutes()==0){
                        finalTotalTimeWork = "00:00";
                    }

                    else {
                        finalTotalTimeWork = "00:" + minutesDurationWork;
                    }

                }

            }
            else {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = hoursDurationWork + ":0" + minutesDurationWork;
                } else {
                    finalTotalTimeWork = hoursDurationWork +":"+ minutesDurationWork;
                }
            }


            TextView tvHorasTrabajadas = findViewById(R.id.tvwTimeWork);
            tvHorasTrabajadas.setText(finalTotalTimeWork);

            DocumentReference docRef = db.collection("users").document(UserUID);
            docRef
                    .update("tiempoTrabajado", finalTotalTimeWork)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });


        }

        if(!horaLunchI.equals("")){
            if(lunchTomado){
                LocalTime horaInicioLunch = LocalTime.parse(horaLunchI);
                LocalTime horaFinLunch = LocalTime.parse(horaLunchF);
                Duration durLunch = Duration.between(horaInicioLunch,horaFinLunch);
                String duration = durLunch.toString();
                String patternHours = "";
                if(durLunch.toHours()==0){
                    patternHours="PT([0-9]+)M";
                }
                else{
                    patternHours = "([0-9]+)H([0-9]+)M";
                }

                Pattern r = Pattern.compile(patternHours);
                Matcher m = r.matcher(duration);
                String hoursDurationLunch="";
                String minutesDurationLunch="";


                if(durLunch.toHours()==0){
                    if (m.find( )) {
                        minutesDurationLunch=m.group(1);
                    }
                }
                else{
                    if (m.find( )) {
                        hoursDurationLunch=m.group(2);
                        minutesDurationLunch=m.group(1);
                    }
                }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
                String finalTotalTimeLunch="";

                if (durLunch.toHours()==0) {
                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = "00:" + "0" + minutesDurationLunch;
                    } else {
                        if(durLunch.toMinutes()==0){


                            finalTotalTimeLunch="00:00";
                        }
                        else{

                            finalTotalTimeLunch = "00:" + minutesDurationLunch;
                        }

                    }
                }
                else {


                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = hoursDurationLunch + ":0" + minutesDurationLunch;
                    } else {
                        finalTotalTimeLunch = hoursDurationLunch +":"+ minutesDurationLunch;
                    }
                }

                TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                tvHorasLunch.setText(finalTotalTimeLunch);

                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef
                        .update("tiempoLunch", finalTotalTimeLunch)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

            }

            else {
                LocalTime nowTime= LocalTime.now();
                LocalTime horaInicioLunch = LocalTime.parse(horaLunchI);
                Duration durLunch = Duration.between(horaInicioLunch,nowTime);
                String duration = durLunch.toString();
                String patternHours = "";
                if(durLunch.toHours()==0){
                    patternHours="PT([0-9]+)M";
                }
                else{
                    patternHours = "([0-9]+)H([0-9]+)M";
                }

                Pattern r = Pattern.compile(patternHours);
                Matcher m = r.matcher(duration);
                String hoursDurationLunch="";
                String minutesDurationLunch="";


                if(durLunch.toHours()==0){
                    if (m.find( )) {
                        minutesDurationLunch=m.group(1);
                    }
                }
                else{
                    if (m.find( )) {
                        hoursDurationLunch=m.group(2);
                        minutesDurationLunch=m.group(1);
                    }
                }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
                String finalTotalTimeLunch="";

                if (durLunch.toHours()==0) {
                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = "00:" + "0" + minutesDurationLunch;
                    } else {
                        if(durLunch.toMinutes()==0){

                            finalTotalTimeLunch="00:00";
                        }
                        else{

                            finalTotalTimeLunch = "00:" + minutesDurationLunch;
                        }

                    }
                }
                else {


                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = hoursDurationLunch + ":0" + minutesDurationLunch;
                    } else {
                        finalTotalTimeLunch = hoursDurationLunch +":"+ minutesDurationLunch;
                    }
                }

                TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                tvHorasLunch.setText(finalTotalTimeLunch);

                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef
                        .update("tiempoLunch", finalTotalTimeLunch)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });


            }
        }





    }

    public void writeCurrentStateAndPopulateSpinner(String currentState, boolean lunchTomado){
        if (currentState.equals("Iniciar turno")){
            currentState="Tiempo de trabajo";
        }

        TextView labelCurrentState= findViewById(R.id.tvActualState);
        labelCurrentState.setText(currentState);
        List<String> spinnerArray =  new ArrayList<String>();
        if (currentState.equals("Tiempo de trabajo") && lunchTomado==false){
            spinnerArray.add("Tiempo de lunch");
            spinnerArray.add("Finalizar turno");
        }
        else if((currentState.equals("Tiempo de trabajo")) && (lunchTomado==true)){
            spinnerArray.add("Finalizar turno");
        }
        else if(currentState.equals("Finalizar turno")){
            spinnerArray.add("Iniciar turno");
        }
        else if(currentState.equals("Tiempo de lunch")){
            spinnerArray.add("Tiempo de trabajo");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spnOptionsWork);
        sItems.setAdapter(adapter);
    }

    public void updateTimers(Map data, String UserUID){
        String horaEntrada= data.get("horaEntrada").toString();
        String horaLunchI= data.get("horaLunchI").toString();
        String horaLunchF= data.get("horaLunchF").toString();
        String tiempoLunch= data.get("tiempoLunch").toString();
        String tiempoTrabajado= data.get("tiempoTrabajado").toString();
        Boolean lunchTomado = Boolean.parseBoolean(data.get("lunchTomado").toString());

        if(!horaEntrada.equals("")){
            LocalTime horaEntradaLocal = LocalTime.parse(horaEntrada);
            LocalTime nowTime= LocalTime.now();
            nowTime=LocalTime.parse(nowTime.toString());
            Duration dur = Duration.between(horaEntradaLocal,nowTime);
            String duration = dur.toString();
            String patternHours = "";
            if(dur.toHours()==0){
                patternHours="PT([0-9]+)M";
            }
            else{
                patternHours = "([0-9]+)H([0-9]+)M";
            }

            Pattern r = Pattern.compile(patternHours);
            Matcher m = r.matcher(duration);
            String hoursDurationWork="";
            String minutesDurationWork="";


            Log.d("Value of Duration",String.valueOf(dur.toHours()));

            if(dur.toHours()==0){
                if (m.find( )) {
                    minutesDurationWork=m.group(1);
                }
            }
            else{
                if (m.find( )) {
                    hoursDurationWork=m.group(1);
                    minutesDurationWork=m.group(2);
                }
            }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
            String finalTotalTimeWork="";

            if (dur.toHours()==0) {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = "00:" + "0" + minutesDurationWork;
                } else {

                    if (dur.toMinutes()==0){
                        finalTotalTimeWork = "00:00";
                    }

                    else {
                        finalTotalTimeWork = "00:" + minutesDurationWork;
                    }

                }

            }
            else {
                if (minutesDurationWork.length() == 1) {
                    finalTotalTimeWork = hoursDurationWork + ":0" + minutesDurationWork;
                } else {
                    finalTotalTimeWork = hoursDurationWork +":"+ minutesDurationWork;
                }
            }


            TextView tvHorasTrabajadas = findViewById(R.id.tvwTimeWork);
            tvHorasTrabajadas.setText(finalTotalTimeWork);

            DocumentReference docRef = db.collection("users").document(UserUID);
            docRef
                    .update("tiempoTrabajado", finalTotalTimeWork)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(getApplicationContext(),"Update First", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }

        if(!horaLunchI.equals("")){
            if(lunchTomado){
                LocalTime horaInicioLunch = LocalTime.parse(horaLunchI);
                LocalTime horaFinLunch = LocalTime.parse(horaLunchF);
                Duration durLunch = Duration.between(horaInicioLunch,horaFinLunch);
                String duration = durLunch.toString();
                String patternHours = "";
                if(durLunch.toHours()==0){
                    patternHours="PT([0-9]+)M";
                }
                else{
                    patternHours = "([0-9]+)H([0-9]+)M";
                }

                Pattern r = Pattern.compile(patternHours);
                Matcher m = r.matcher(duration);
                String hoursDurationLunch="";
                String minutesDurationLunch="";


                if(durLunch.toHours()==0){
                    if (m.find( )) {
                        minutesDurationLunch=m.group(1);
                    }
                }
                else{
                    if (m.find( )) {
                        hoursDurationLunch=m.group(2);
                        minutesDurationLunch=m.group(1);
                    }
                }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
                String finalTotalTimeLunch="";


                if (durLunch.toHours()==0) {
                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = "00:" + "0" + minutesDurationLunch;
                    } else {
                        if(durLunch.toMinutes()==0){


                            finalTotalTimeLunch="00:00";
                        }
                        else{

                            finalTotalTimeLunch = "00:" + minutesDurationLunch;
                        }

                    }
                }
                else {


                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = hoursDurationLunch + ":0" + minutesDurationLunch;
                    } else {
                        finalTotalTimeLunch = hoursDurationLunch +":"+ minutesDurationLunch;
                    }
                }


                TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                tvHorasLunch.setText(finalTotalTimeLunch);

                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef
                        .update("tiempoLunch", finalTotalTimeLunch)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });


            }

            else {
                LocalTime nowTime= LocalTime.now();
                LocalTime horaInicioLunch = LocalTime.parse(horaLunchI);
                Duration durLunch = Duration.between(horaInicioLunch,nowTime);
                String duration = durLunch.toString();
                String patternHours = "";
                if(durLunch.toHours()==0){
                    patternHours="PT([0-9]+)M";
                }
                else{
                    patternHours = "([0-9]+)H([0-9]+)M";
                }

                Pattern r = Pattern.compile(patternHours);
                Matcher m = r.matcher(duration);
                String hoursDurationLunch="";
                String minutesDurationLunch="";


                if(durLunch.toHours()==0){
                    if (m.find( )) {
                        minutesDurationLunch=m.group(1);
                    }
                }
                else{
                    if (m.find( )) {
                        hoursDurationLunch=m.group(2);
                        minutesDurationLunch=m.group(1);
                    }
                }


        /*
        Log.d("HoraEntrada",String.valueOf(horaEntradaLocal));
        Log.d("HoraNow",String.valueOf(nowTime));
        Log.d("DurationString",duration);
        Log.d("HoraDurationHours",String.valueOf(hoursDurationWork));
        Log.d("HoraDurationHours",String.valueOf(minutesDurationWork));*/
                String finalTotalTimeLunch="";

                if (durLunch.toHours()==0) {
                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = "00:" + "0" + minutesDurationLunch;
                    } else {
                        if(durLunch.toMinutes()==0){


                            finalTotalTimeLunch="00:00";
                        }
                        else{

                            finalTotalTimeLunch = "00:" + minutesDurationLunch;
                        }

                    }
                }
                else {


                    if (minutesDurationLunch.length() == 1) {
                        finalTotalTimeLunch = hoursDurationLunch + ":0" + minutesDurationLunch;
                    } else {
                        finalTotalTimeLunch = hoursDurationLunch +":"+ minutesDurationLunch;
                    }
                }


                TextView tvHorasLunch = findViewById(R.id.tvwTimeLunch);
                tvHorasLunch.setText(finalTotalTimeLunch);

                DocumentReference docRef = db.collection("users").document(UserUID);
                docRef
                        .update("tiempoLunch", finalTotalTimeLunch)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });


            }



        }
    }

    public void updateWorkedHours(Map data, String UserUID){
        String timeWorkedStr=data.get("tiempoTrabajado").toString(); //Tiempo trabajado today.
        String workedHoursStr=data.get("workedHours").toString(); //Tiempo trabajado historical.
        float workedHoursFloat= Float.parseFloat(workedHoursStr); // Tiempo trabajado today to float.

        String patternHours = "([0-9]{1,2}):([0-9]{2})";
        Pattern r = Pattern.compile(patternHours);
        Matcher m = r.matcher(timeWorkedStr);

        float hoursWork=0.0f;
        float minutesWork=0.0f;

        if (m.find()) {
            hoursWork=Float.parseFloat(m.group(1));
            minutesWork=Float.parseFloat(m.group(2));
        }

        minutesWork=minutesWork/60;

        float todayWorkedTime=hoursWork+minutesWork;

        float newWorkedTime=todayWorkedTime+workedHoursFloat;

        DocumentReference docRef;
        docRef = db.collection("users").document(UserUID);
        docRef
                .update("workedHours", String.valueOf(newWorkedTime))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentUpdated yeyo!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Ahh verg*, no se subió.", e);
                    }
                });

        //Mismo proceso para las horas lunch

        String timeLunchStr=data.get("tiempoLunch").toString(); //Tiempo trabajado today.
        String lunchHoursStr=data.get("lunchHours").toString(); //Tiempo trabajado historical.
        float lunchHoursFloat= Float.parseFloat(lunchHoursStr); // Tiempo trabajado today to float.

        patternHours = "([0-9]{1,2}):([0-9]{2})";
        r = Pattern.compile(patternHours);
        m = r.matcher(timeLunchStr);

        float hoursLunch=0.0f;
        float minutesLunch=0.0f;

        if (m.find()) {
            hoursLunch=Float.parseFloat(m.group(1));
            minutesLunch=Float.parseFloat(m.group(2));
        }

        minutesLunch=minutesLunch/60;

        float todayLunchTime=hoursLunch+minutesLunch;

        float newLunchTime=todayLunchTime+lunchHoursFloat;

        docRef = db.collection("users").document(UserUID);
        docRef
                .update("lunchHours", String.valueOf(newLunchTime))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentUpdated yeyo!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Ahh verg*, no se subió.", e);
                    }
                });

    }

}


