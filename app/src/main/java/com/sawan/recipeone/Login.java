package com.sawan.recipeone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button but_login;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    User user;

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            setContentView(R.layout.activity_login);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_edit_login);
        editTextPassword = findViewById(R.id.email_edit_login);
        but_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
    }
    public void registerListener(View view) {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login success.",
                                    Toast.LENGTH_SHORT).show();

                            Login.this.user = new User(null,email, 0, null, null);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("User");
                            Query query = usersRef.whereEqualTo("userID", email);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
 @Override
public void onComplete(@NonNull Task<QuerySnapshot> task) {
 if (task.isSuccessful()) {
// Get the user data from the query snapshot
 QuerySnapshot querySnapshot = task.getResult();
if (!querySnapshot.isEmpty()) {
DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
String name = documentSnapshot.getString("name");
    Login.this.user.setName(name);
    Intent intent = new Intent(getApplicationContext(), Homepage.class);
    intent.putExtra("user",Login.this.user);

    startActivity(intent);
    finish();

 }
} else { Toast.makeText(Login.this, "Authentication failed.",
         Toast.LENGTH_SHORT).show();
// Handle the error
 }
 }
                            });


                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}