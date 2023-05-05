package com.sawan.recipeone;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Homepage extends AppCompatActivity {
    TextView textViewWelcomeLabel;
    User user;
    ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            Log.d("User", "Logged in");
            Toast.makeText(this, "User Logged in.", Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
        }

        textViewWelcomeLabel = findViewById(R.id.welcomeLabel);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_Linear_Layout);
        this.user = (User) getIntent().getSerializableExtra("user");
        textViewWelcomeLabel.setText("Welcome " + user.getName());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout newLinearLayout = new LinearLayout(Homepage.this);
                                LinearLayout.LayoutParams newLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                newLinearLayout.setLayoutParams(newLayout);
                                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                int padding_in_dp = 16;  // 6 dps
                                final float scale = getResources().getDisplayMetrics().density;
                                int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                                newLinearLayout.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                                TextView newTextView = new TextView(Homepage.this);
                                newTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                String recipeName = document.getString("imageName");
                                newTextView.setText(recipeName);
                                newTextView.setTextSize(26);
                                newTextView.setPadding(padding_in_px, 0, 0, 0);
                                int padding_in_dp_I = 200;  // 6 dps
                                final float scaleI = getResources().getDisplayMetrics().density;
                                int padding_in_pxI = (int) (padding_in_dp_I * scale + 0.5f);
                                ImageView newImage = new ImageView(Homepage.this);
                                GridView.LayoutParams newLayoutText = new GridView.LayoutParams(padding_in_pxI, padding_in_pxI);
                                newImage.setLayoutParams(newLayoutText);
                                String recipeImage = document.getString("imageURL");
                                Picasso.get().load(recipeImage).into(newImage);
                                newLinearLayout.addView(newImage);
                                newLinearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Homepage.this, DetailActivity.class);
                                        intent.putExtra("recipeName",recipeName);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                newLinearLayout.addView(newTextView);

                                linearLayout.addView(newLinearLayout);
                            }
                        } else {
                         // TODO
                        }
                    }
                });
        userImage = findViewById(R.id.userIcon);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Profile.class);
                intent.putExtra("user", Homepage.this.user);
                startActivity(intent);
                finish();
            }
        });

    }

}
