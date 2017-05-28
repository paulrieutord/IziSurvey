package com.udp.paul.izisurvey.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udp.paul.izisurvey.R;

public class survey_questions extends AppCompatActivity {

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey_questions);
        toolbar.setTitle(extras.getString(EXTRA_TITLE));
    }
}