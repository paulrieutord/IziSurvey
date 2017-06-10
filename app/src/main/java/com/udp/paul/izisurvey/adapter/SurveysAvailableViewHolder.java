package com.udp.paul.izisurvey.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.model.Organization;
import com.udp.paul.izisurvey.model.Survey;
import com.udp.paul.izisurvey.ui.survey_questions;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SurveysAvailableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    View mView;
    Context mContext;
    String mKey;

    //private ImageView icon;
    private TextView titleSurvey;
    private TextView organizationSurvey;
    private TextView expireDateSurvey;
    private String mTitleEvent;

    private DatabaseReference mDatabase;
    private DatabaseReference mOrganization;

    public SurveysAvailableViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mContext = itemView.getContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mOrganization = mDatabase.child("organizations");
    }

    public void bindEvent (String key) {
        mKey = key;

        //icon = (ImageView) itemView.findViewById(R.id.avatar_survey_item);
        titleSurvey = (TextView) itemView.findViewById(R.id.name_survey_item);
        organizationSurvey = (TextView) itemView.findViewById(R.id.organization_survey_item);
        expireDateSurvey = (TextView) itemView.findViewById(R.id.expire_date_survey_item);
        mView.setOnClickListener(this);

        mDatabase.child("surveys").child(mKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Survey eventObject = dataSnapshot.getValue(Survey.class);

                titleSurvey.setText(eventObject.getName());
                mTitleEvent = eventObject.getName();

                mOrganization.child(eventObject.getOrganization()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        organizationSurvey.setText(dataSnapshot.getValue(Organization.class).getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String formatDate = new SimpleDateFormat("HH:mm", Locale.US).format(eventObject.getCreatedAt())+" hrs.";
                expireDateSurvey.setText(formatDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(mContext, survey_questions.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_KEY, mKey);
        extras.putString(EXTRA_TITLE, mTitleEvent);
        i.putExtra(BUNDLE_EXTRAS, extras);

        mContext.startActivity(i);
    }
}