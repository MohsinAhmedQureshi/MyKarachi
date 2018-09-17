package com.fyp.mykarachi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class AddUpdateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

//     private BottomAppBar bottomAppBar;
//     private FloatingActionButton floatingActionButton;
//     private int PLACE_PICKER_REQUEST = 1;
//     private EditText location, cause, details;

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_add_update);

//         bottomAppBar = findViewById(R.id.AddUpdateAppBar);
//         floatingActionButton = findViewById(R.id.AddUpdateFab);

//         bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 AddUpdateActivity.super.onBackPressed();
//             }
//         });

//         location = findViewById(R.id.addUpdateLocation);
//         cause = findViewById(R.id.addUpdateCause);
//         details = findViewById(R.id.addUpdateDetails);

//     }

//     public void onClickLocation(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
//         PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//         startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//     }

//     public void onClickAddPhoto(View view) {
//         Toast.makeText(this, "Add Photo Clicked", Toast.LENGTH_SHORT).show();
//     }

//     public void onClickSendUpdate(View view) {
//         DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Pending Updates");
//         final String updateID = mRef.push().getKey();
//         mRef = mRef.child(updateID);

//         final String strLocation = location.getText().toString();
//         final String strCause = cause.getText().toString();
//         final String strDetails = details.getText().toString();

//         if (!strLocation.isEmpty() && !strCause.isEmpty() && !strDetails.isEmpty()) {

//             final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//             mRef.child("Author").setValue(uid);

//             FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
//                 @Override
//                 public void onDataChange(DataSnapshot dataSnapshot) {
//                     final String userName = dataSnapshot.child("Name").getValue().toString();
//                     FirebaseDatabase.getInstance().getReference().child("Pending Updates").child(updateID).child("AuthorName").setValue(userName);
//                 }

//                 @Override
//                 public void onCancelled(DatabaseError databaseError) {

//                 }
//             });

//             mRef.child("Location").setValue(strLocation);
//             mRef.child("Cause").setValue(strCause);
//             mRef.child("Details").setValue(strDetails);
//             mRef.child("Reputation").setValue("0");

//             Toast.makeText(this, "Successfully Posted Update!", Toast.LENGTH_SHORT).show();
//             super.onBackPressed();
//         } else {
//             Toast.makeText(this, "Please Fill All Fields Correctly", Toast.LENGTH_SHORT).show();
//         }

//     }


//     @Override
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//         if (requestCode == PLACE_PICKER_REQUEST) {
//             if (resultCode == RESULT_OK) {
//                 Place place = PlacePicker.getPlace(data, this);
//                 String toastMsg = String.format("Place: %s", place.getName());
//                 Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//             }
//         }
//     }
    
    private static final String TAG = "AddUpdateActivity";
    private TextInputEditText locationText, messageText;
    private Spinner spinner;
    BottomAppBar bottomAppBar;
    FloatingActionButton floatingActionButton;
    private int PLACE_PICKER_REQUEST = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    News update;
    //VARIABLES
    private String cause, updateImageUrl = "";
    private String[] trafficJams;
    private Button chooseBtn, submitBtn;
    private ImageView imgView;
    private Uri galleryFilePath;
    private FirebaseDatabase database;
    private DatabaseReference myRef, tempRef;
    private DatabaseReference pendingNewsRef, verifiedNewsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

//        Instantiation
        submitBtn = findViewById(R.id.updateNewsSubmitUpdateBtn);
//        imgView = findViewById(R.id.updateNewsImgView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        pendingNewsRef = myRef.child("PendingNews");
        verifiedNewsRef = myRef.child("VerifiedNews");
        trafficJams = getResources().getStringArray(R.array.Traffic_Jam_Reasons);
        cause = trafficJams[0];
        locationText = findViewById(R.id.locationText);
        messageText = findViewById(R.id.messageText);
        spinner = findViewById(R.id.updateNewsCauseSpinner);
        bottomAppBar = findViewById(R.id.AddUpdateAppBar);
        floatingActionButton = findViewById(R.id.AddUpdateFab);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Traffic_Jam_Reasons, R.layout.custom_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateActivity.super.onBackPressed();
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationText.setCursorVisible(true);
            }
        });

        messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageText.setCursorVisible(true);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = new News();
                tempRef = null;
                String loc = locationText.getText().toString();
                String det = messageText.getText().toString();
                if (!loc.equals("")) {
                    update.setLocation(loc);
                    update.setCause(cause);
                    if (!det.equals(""))
                        update.setDetails(det);
                    tempRef = pendingNewsRef.push();
                    tempRef.child("Location").setValue(update.getLocation());
                    tempRef.child("Cause").setValue(update.getCause());
                    tempRef.child("Details").setValue(update.getDetails());
                    tempRef.child("Reputation").setValue(update.getReputation());
                    if (galleryFilePath != null)
                        uploadPicture();
                    else {
                        tempRef.child("ImageURL").setValue(updateImageUrl);
                        Toast.makeText(AddUpdateActivity.this, "Update Added.", Toast.LENGTH_SHORT).show();
                    }
                    String userId = mAuth.getCurrentUser().getUid();
                    tempRef.child("Author").setValue(userId);
                } else
                    Toast.makeText(AddUpdateActivity.this, "Location Field is Compulsory!", Toast.LENGTH_SHORT).show();
                locationText.setCursorVisible(false);
                messageText.setCursorVisible(false);
                updateImageUrl = "";
            }
        });
    }

    public void onClickLocation(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddUpdateActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.d(TAG, "placePicker onClick: GooglePlayServicesRepairableException: " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d(TAG, "placePicker onClick: GooglePlayServicesNotAvailableException: " + e.getMessage());
        }
    }

    public void onClickAddPhoto(View view) {
        Toast.makeText(this, "Add Photo Clicked", Toast.LENGTH_SHORT).show();
        choosePictureFromGallery();
    }

    private void choosePictureFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                locationText.setText(String.format("%s", place.getName()));
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("AddUpdateActivity" , "data: "+ data.getData());
            galleryFilePath = data.getData();
//            try {
//                Bitmap bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryFilePath);
//                imgView.setImageBitmap(bitMap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void uploadPicture() {
        if (galleryFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding..");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(galleryFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AddUpdateActivity.this, "Update Added with Picture From Gallery", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddUpdateActivity.this, "Failed to add Update, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Adding Update " + (int) progress + "%");
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        updateImageUrl = downloadUri.toString();
                        update.setImageURL(updateImageUrl);
                        tempRef.child("ImageURL").setValue(update.getImageURL());
                        Log.d("ProvideNewsActivity" , "Picture Uploaded: ImageURL: "+ updateImageUrl);
                    } else {
                        Toast.makeText(AddUpdateActivity.this, "URI Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cause = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    locationText.setCursorVisible(false);
                    messageText.setCursorVisible(false);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
