package com.udp.paul.izisurvey.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.model.Organization;
//import com.udp.paul.izisurvey.ui.survey_detail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrganizationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_URI = "EXTRA_URI";

    View mView;
    Context mContext;
    ImageView avatar;
    TextView name;
    TextView category;
    String mKey;
    String mName;

    private DatabaseReference mDatabase;
    private StorageReference imageReference;

    public OrganizationViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mContext = itemView.getContext();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageReference = FirebaseStorage.getInstance().getReference();
    }

    public void bindOrganizations (Organization survey, String key) {
        mKey = key;
        mName = survey.getName();
        avatar = (ImageView) itemView.findViewById(R.id.icon_organization_item);
        name = (TextView) itemView.findViewById(R.id.name_organization_item);
        category = (TextView) itemView.findViewById(R.id.category_organization_item);

        mDatabase.child("organizations").child(mKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Organization organizationObject = dataSnapshot.getValue(Organization.class);
                        name.setText(organizationObject.getName());
                        category.setText(organizationObject.getCategory());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        //FOR AVATAR

        /*imageReference.child("Photos/"+mKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso
                        .with(mContext)
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
        });*/
    }

    @Override
    public void onClick(View v) {
        /*Intent i = new Intent(mContext, event_detail.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_KEY, mKey);
        extras.putString(EXTRA_TITLE, mName);
        i.putExtra(BUNDLE_EXTRAS, extras);

        mContext.startActivity(i);*/
    }
}