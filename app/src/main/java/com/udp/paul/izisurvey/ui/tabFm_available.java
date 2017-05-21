package com.udp.paul.izisurvey.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.adapter.SurveysAvailableViewHolder;

public class tabFm_available extends Fragment {

    private RecyclerView recView_surveys;
    private FirebaseRecyclerAdapter adapter_surveys;

    private FirebaseDatabase FBDatabase;
    private DatabaseReference FBReference;
    private Query queryRef;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static tabFm_available newInstance(int sectionNumber) {
        tabFm_available fragment = new tabFm_available();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public tabFm_available() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_fm_available, container, false);

        recView_surveys = (RecyclerView) rootView.findViewById(R.id.recView_survey_available);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FBDatabase = FirebaseDatabase.getInstance();
        FBReference = FBDatabase.getReference("users").child(user.getUid()).child("surveys");
        queryRef = FBReference.orderByValue().equalTo(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter_surveys = new FirebaseRecyclerAdapter<Boolean, SurveysAvailableViewHolder>(
                Boolean.class,
                R.layout.card_view_survey,
                SurveysAvailableViewHolder.class,
                queryRef
        ) {
            @Override
            protected void populateViewHolder(SurveysAvailableViewHolder viewHolder, Boolean model, int position) {
                viewHolder.bindEvent(adapter_surveys.getRef(position).getKey());
            }
        };

        recView_surveys.setHasFixedSize(true);
        recView_surveys.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView_surveys.setAdapter(adapter_surveys);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter_surveys.cleanup();
    }
}