package com.sawan.recipeone;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


public class Profile extends FragmentActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1000;
    private String mImageFilePath;
    private ImageView mImageView;
    User user;
    TextView tv_Email;
    TextView usrNameText;
    FirebaseUser currUser;
    String newPassword;

    EditText userNameEdit;
    EditText userPhoneEdit;
    EditText datePicker;
    Switch maleSwitch;
    Switch femaleSwitch;
    ImageView homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);
        mImageView = findViewById(R.id.imageView);
        this.user = (User) getIntent().getSerializableExtra("user");
        usrNameText = findViewById(R.id.userNameText);
        usrNameText.setText(user.getName());
        Button save_button;
        save_button = findViewById(R.id.save_button);
        this.userNameEdit = findViewById(R.id.userNameEdit);
        this.userPhoneEdit = findViewById(R.id.userPhoneEdit);
        this.datePicker = findViewById(R.id.datePicker);
        this.maleSwitch = findViewById(R.id.maleSwitch);
        this.femaleSwitch = findViewById(R.id.femaleSwitch);
        tv_Email = findViewById(R.id.TV_Email);
        tv_Email.setText(this.user.getUserID());
        this.homeIcon = findViewById(R.id.homeIcon);
        this.homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Homepage.class);
                intent.putExtra("user", Profile.this.user);
                startActivity(intent);
                finish();
            }
        });

        Button updatePasswordButton = findViewById(R.id.update_password_button);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText currentPasswordEditText = findViewById(R.id.current_password_edit_text);
                EditText newPasswordEditText = findViewById(R.id.new_password_edit_text);
                EditText confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
                String currentPassword;

                String confirmPassword;
                currentPassword = currentPasswordEditText.getText().toString();
                newPassword = newPasswordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                currUser = FirebaseAuth.getInstance().getCurrentUser();

Log.d("User", Profile.this.user.getUserID());
                Log.d("pass", currentPassword);

                                            currUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Profile.this, "Password updated.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Profile.this, "Password not updated.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }


                                });

                }


        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = Profile.this.userNameEdit.getText().toString();
                String phone = Profile.this.userNameEdit.getText().toString();
                String dob = Profile.this.datePicker.getText().toString();
                String sex = "";
                if(Profile.this.maleSwitch.isChecked())
                    sex = "Male";
                else if(Profile.this.femaleSwitch.isChecked())
                    sex = "Female";
                Profile.this.user.setName(userName);
                Profile.this.user.setPhone(Integer.parseInt(phone));
                Profile.this.user.setDob(dob);
                Profile.this.user.setSex(sex);
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                DocumentReference dbUsers = db.collection("User").document(Profile.this.user.getUserID());
                Toast.makeText(Profile.this, "Reached",
                        Toast.LENGTH_SHORT).show();
                dbUsers.set(Profile.this.user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profile.this, "Account Updated.",
                                Toast.LENGTH_SHORT).show();
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Account not Updated.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the selected date
            // For example, update the text of the EditText widget with the selected date
            EditText editText = getActivity().findViewById(R.id.datePicker);
            editText.setText(day + "/" + (month + 1) + "/" + year);
        }
    }




    public void captureClicked(View view) {


        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //To save to file: photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.withAppendedPath(locationForPhotos, targetFilename));
        //Uri location = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "photo.jpg"));
        //photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, location);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
        }
        // Create the file where the photo should go
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photoFile = null;
//        try {
//            photoFile = setUpPhotoFile();
//            mImageFilePath = photoFile.getAbsolutePath();
//        } catch (IOException ex) {
//            // Error occurred while creating the File
//            ex.printStackTrace();
//        }
//
//        // Continue only if the file was successfully created
//        if (photoFile != null) {
//            // Add the file URI to the intent so the camera app knows where to save the photo
//            Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                    BuildConfig.APPLICATION_ID + ".provider", photoFile);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//
//            // Start the camera activity
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    // Handle the result of the camera activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Get the photo into a Bitmap object and display it in the imageView
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.imageView);
            imageview.setImageBitmap(image);
            Uri photo = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "photo.jpg"));
}
}
    // Set up the photo file where the captured image will be stored
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        return f;
    }

    // Create the file where the captured image will be stored
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    // Get the directory where the captured image will be stored
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumStorageDir("MyApp");
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("Camera", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    // Get the directory path where the captured image will be stored
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName
        );
    }

}

