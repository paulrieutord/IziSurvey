package com.udp.paul.izisurvey.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.udp.paul.izisurvey.R;
import com.udp.paul.izisurvey.adapter.OrganizationViewHolder;
import com.udp.paul.izisurvey.model.Organization;

public class fm_organizations extends Fragment {

    private RecyclerView recView_organizations;
    public FirebaseRecyclerAdapter adapter_organizations;

    private FirebaseDatabase FBDatabase;
    private DatabaseReference FBReference;
    private Query queryRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fm_organizations, container, false);

        recView_organizations = (RecyclerView) rootView.findViewById(R.id.recView_organizations);

        FBDatabase = FirebaseDatabase.getInstance();
        FBReference = FBDatabase.getReference("organizations");
        queryRef = FBReference.orderByChild("name");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter_organizations = new FirebaseRecyclerAdapter<Organization, OrganizationViewHolder>(
                Organization.class,
                R.layout.organization_item,
                OrganizationViewHolder.class,
                queryRef
        ) {
            @Override
            protected void populateViewHolder(OrganizationViewHolder viewHolder, Organization model, int position) {
                viewHolder.bindOrganizations(model, adapter_organizations.getRef(position).getKey());
            }
        };

        LinearLayoutManager layoutM = new LinearLayoutManager(getActivity());
        //layoutM.setReverseLayout(true)
        //layoutM.setStackFromEnd(true);
        recView_organizations.setHasFixedSize(true);
        recView_organizations.setLayoutManager(layoutM);
        recView_organizations.setAdapter(adapter_organizations);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter_organizations.cleanup();
    }
}