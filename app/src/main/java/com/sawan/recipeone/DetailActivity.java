package com.sawan.recipeone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    boolean isImageChanged = false;

    ImageView img;
    ImageView mainImage;
    TextView desc ;
    String recipeNm;
    TextView recipeName;
    TextView prepTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_recipe);
        img = findViewById(R.id.favourite);
        desc = findViewById(R.id.desc);
        this.recipeNm = getIntent().getStringExtra("recipeName");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Recipes");
        Query query = usersRef.whereEqualTo("imageName", this.recipeNm);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the user data from the query snapshot
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String imageURL = documentSnapshot.getString("imageURL");
                        String description = documentSnapshot.getString("description");
                        String preparationTime = documentSnapshot.getString("preparationTime");
                        mainImage = findViewById(R.id.ivImage2);
                        Picasso.get().load(imageURL).into(mainImage);
                        recipeName = findViewById(R.id.txtRecipeName);
                        recipeName.setText(DetailActivity.this.recipeNm);
                        desc.setText(description);
                        prepTime = findViewById(R.id.prepTime);
                        prepTime.setText(preparationTime);
                    }
                } else { Toast.makeText(DetailActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
// Handle the error
                }
            };
        });
    }

    public void fav(View view) {
        if (isImageChanged) {
            // Change the image back to the original image
            img.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            isImageChanged = false;
        } else {
            // Change the image to the new image
            img.setImageResource(R.drawable.ic_baseline_favorite);
            isImageChanged = true;
        }
    }

    public void share(View view) {

        String description = desc.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, description);

        startActivity(Intent.createChooser(shareIntent, "Share using"));

    }
}