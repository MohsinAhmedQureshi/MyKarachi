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

public class AddUpdateActivity extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingActionButton;
    private int PLACE_PICKER_REQUEST = 1;
    private EditText location, cause, details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        bottomAppBar = findViewById(R.id.AddUpdateAppBar);
        floatingActionButton = findViewById(R.id.AddUpdateFab);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateActivity.super.onBackPressed();
            }
        });

        location = findViewById(R.id.addUpdateLocation);
        cause = findViewById(R.id.addUpdateCause);
        details = findViewById(R.id.addUpdateDetails);

    }

    public void onClickLocation(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    public void onClickAddPhoto(View view) {
        Toast.makeText(this, "Add Photo Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onClickSendUpdate(View view) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Pending Updates");
        final String updateID = mRef.push().getKey();
        mRef = mRef.child(updateID);

        final String strLocation = location.getText().toString();
        final String strCause = cause.getText().toString();
        final String strDetails = details.getText().toString();

        if (!strLocation.isEmpty() && !strCause.isEmpty() && !strDetails.isEmpty()) {

            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mRef.child("Author").setValue(uid);

            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String userName = dataSnapshot.child("Name").getValue().toString();
                    FirebaseDatabase.getInstance().getReference().child("Pending Updates").child(updateID).child("AuthorName").setValue(userName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mRef.child("Location").setValue(strLocation);
            mRef.child("Cause").setValue(strCause);
            mRef.child("Details").setValue(strDetails);
            mRef.child("Reputation").setValue("0");

            Toast.makeText(this, "Successfully Posted Update!", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Please Fill All Fields Correctly", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
