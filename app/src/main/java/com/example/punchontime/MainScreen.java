package com.example.punchontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainScreen extends AppCompatActivity {
    private static final String TAG = "DocSnippets";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle bundle = getIntent().getExtras();
        final String UserUID = bundle.getString("UserUID");

        Map data;

        DocumentReference docRef = db.collection("users").document(UserUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Entre",Toast.LENGTH_SHORT).show();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getData(document.getData());
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

    protected  void onStart(){
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        final String UserUID = bundle.getString("UserUID");
        Button btnChange = findViewById(R.id.btnCambiar);
        btnChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Spinner spinner = (Spinner)findViewById(R.id.spnOptionsWork);
                String newState = spinner.getSelectedItem().toString();
                writeCurrentStateAndPopulateSpinner(newState);

                if (newState.equals("Iniciar turno")){
                    newState="Tiempo de trabajo";
                }

                DocumentReference docRef = db.collection("users").document(UserUID);

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



    public void getData(Map data){
        writeHello(data.get("Nombre").toString());
        writeCurrentStateAndPopulateSpinner(data.get("CurrentState").toString());
    }

    public void writeHello(String employeeName){
        TextView labelHello= findViewById(R.id.tvHello);
        labelHello.setText("Hola, "+employeeName+".");
    }

    public void writeCurrentStateAndPopulateSpinner(String currentState){
        if (currentState.equals("Iniciar turno")){
            currentState="Tiempo de trabajo";
        }

        TextView labelCurrentState= findViewById(R.id.tvActualState);
        labelCurrentState.setText(currentState);
        List<String> spinnerArray =  new ArrayList<String>();
        if (currentState.equals("Tiempo de trabajo")){
            spinnerArray.add("Tiempo de lunch");
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

}
