package com.sawan.recipeone;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcomepage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
