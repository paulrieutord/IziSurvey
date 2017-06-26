package com.udp.paul.izisurvey.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.model.Organization;

public class organization_detail extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    FloatingActionButton fab;
    TextView category;
    TextView description;
    ImageView photo;
    Boolean childExists;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    Bundle extras;

    private FirebaseDatabase FBDatabase;
    private DatabaseReference FBReferenceOrganization;
    private DatabaseReference FBReferenceUser;
    private DatabaseReference FBReferenceSurveyAnswer;
    private StorageReference imageReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_detail);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_detail_organization);
        category = (TextView) findViewById(R.id.category_value);
        description = (TextView) findViewById(R.id.description_value);
        photo = (ImageView) findViewById(R.id.photo_detail);

        imageReference = FirebaseStorage.getInstance().getReference();

        extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        imageReference.child("Photos/Organizations/"+extras.getString(EXTRA_KEY)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso
                        .with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .fit()
                        .into(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        FBDatabase = FirebaseDatabase.getInstance();
        FBReferenceOrganization = FBDatabase.getReference("organizations");
        FBReferenceUser = FBDatabase.getReference("users");
        FBReferenceSurveyAnswer = FBDatabase.getReference("surveyAnswers");

        fab = (FloatingActionButton) findViewById(R.id.fab_detail_organization);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childExists) {
                    FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("users").equalTo(user.getUid()).getRef().removeValue();
                    FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("surveys").addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String keySurvey = String.valueOf(snapshot.getKey());

                                FBReferenceSurveyAnswer.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (!snapshot.hasChild(keySurvey)) {
                                            FBReferenceUser.child(user.getUid()).child("surveys").child(keySurvey).getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    FBReferenceUser.child(user.getUid()).child("organizations").equalTo(extras.getString(EXTRA_KEY)).getRef().removeValue();
                    fab.setImageResource(R.drawable.ic_menu_add);
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.dark_gray));
                    childExists = false;
                } else {
                    FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long size = dataSnapshot.getChildrenCount();
                            FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("users").child(String.valueOf(size)).getRef().setValue(user.getUid());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("surveys").addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String keySurvey = String.valueOf(snapshot.getKey());

                                FBReferenceSurveyAnswer.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (!snapshot.hasChild(keySurvey)) {
                                            FBReferenceUser.child(user.getUid()).child("surveys").child(keySurvey).getRef().setValue(true);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    FBReferenceUser.child(user.getUid()).child("organizations").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long size = dataSnapshot.getChildrenCount();
                            FBReferenceUser.child(user.getUid()).child("organizations").child(String.valueOf(size)).getRef().setValue(extras.getString(EXTRA_TITLE));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    fab.setImageResource(R.drawable.ic_check_black_24px);
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                    childExists = true;
                }

                /*FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("users").addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                countUsers = dataSnapshot.getChildrenCount();
                                assistants.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).child("countUsers").setValue(countUsers);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });*/
            }
        });

        FBReferenceOrganization.child(extras.getString(EXTRA_KEY)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Organization organizationObject = dataSnapshot.getValue(Organization.class);
                        collapsingToolbarLayout.setTitle(organizationObject.getName());

                        category.setText(organizationObject.getCategory());
                        description.setText(organizationObject.getDescription());

                        childExists = false;

                        if (dataSnapshot.child("users").exists()) {
                            for (DataSnapshot users : dataSnapshot.child("users").getChildren()) {
                                Log.d("UID ORG DETAL", String.valueOf(users.getValue()) + " == " + user.getUid());

                                String valueKey = String.valueOf(users.getValue());
                                if (valueKey.equals(user.getUid())) {
                                    Log.d("SON IGUALES", "SI");
                                    childExists = true;
                                }
                            }
                        }

                        if (childExists) {
                            Log.d("EXISTE", "SI");
                            fab.setImageResource(R.drawable.ic_check_black_24px);
                            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                        } else {
                            Log.d("EXISTE", "NO");
                            fab.setImageResource(R.drawable.ic_menu_add);
                            fab.setBackgroundTintList(getResources().getColorStateList(R.color.dark_gray));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}