package com.udp.paul.izisurvey.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.udp.paul.izisurvey.R;
//import com.udp.paul.izisurvey.adapter.EventsViewHolder;
//import com.udp.paul.izisurvey.model.Event;

public class tabFm_answered extends Fragment {

    private RecyclerView recView_events;
    //private FirebaseRecyclerAdapter adapter_events;

    private FirebaseDatabase FBDatabase;
    private DatabaseReference FBReference;
    private Query queryRef;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static tabFm_answered newInstance(int sectionNumber) {
        tabFm_answered fragment = new tabFm_answered();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public tabFm_answered() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_fm_answered, container, false);

        recView_events = (RecyclerView) rootView.findViewById(R.id.recView_survey_answered);

        FBDatabase = FirebaseDatabase.getInstance();
        //FBReference = FBDatabase.getReference("organization");
        //queryRef = FBReference.orderByChild("countUsers");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*adapter_events = new FirebaseRecyclerAdapter<Event, EventsViewHolder>(
                Event.class,
                R.layout.event_item,
                EventsViewHolder.class,
                queryRef
        ) {
            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, Event model, int position) {
                viewHolder.bindEvent(model, adapter_events.getRef(position).getKey());
            }
        };

        LinearLayoutManager layoutM = new LinearLayoutManager(getActivity());
        layoutM.setReverseLayout(true);
        layoutM.setStackFromEnd(true);
        recView_events.setHasFixedSize(true);
        recView_events.setLayoutManager(layoutM);
        recView_events.setAdapter(adapter_events);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //adapter_events.cleanup();
    }
}