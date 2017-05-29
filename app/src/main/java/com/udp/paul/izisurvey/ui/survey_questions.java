package com.udp.paul.izisurvey.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udp.paul.izisurvey.R;

public class survey_questions extends AppCompatActivity {

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    Bundle extras;

    private FirebaseDatabase FBDatabase;
    private DatabaseReference FBReference;
    private Query queryRef;

    LinearLayout container_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey_questions);
        toolbar.setTitle(extras.getString(EXTRA_TITLE));

        container_layout = (LinearLayout) findViewById(R.id.container_survey_questions);

        FBDatabase = FirebaseDatabase.getInstance();
        FBReference = FBDatabase.getReference("surveys").child(extras.getString(EXTRA_KEY)).child("questions");
        queryRef = FBReference.orderByChild("order");

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String type = String.valueOf(snapshot.child("type").getValue());

                    TextView title = new TextView(getApplicationContext());

                    String titleString = String.valueOf(snapshot.child("order").getValue()) + ") " + String.valueOf(snapshot.child("description").getValue());
                    title.setText(titleString);
                    title.setTextColor(Color.BLACK);

                    container_layout.addView(title);

                    LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) title.getLayoutParams();
                    textViewParams.setMargins(0, 0, 0, 40);
                    title.setLayoutParams(textViewParams);

                    switch (type) {
                        //ONLY ONE OPTION
                        case "1":
                            RadioGroup group = new RadioGroup(getApplicationContext());
                            group.setOrientation(RadioGroup.VERTICAL);

                            for (DataSnapshot options : snapshot.child("options").getChildren()) {
                                String option = String.valueOf(options.getValue());

                                RadioButton opt = new RadioButton(getApplicationContext());
                                opt.setText(option);
                                opt.setTextColor(Color.BLACK);
                                group.addView(opt);

                                RadioGroup.LayoutParams radioParams = (RadioGroup.LayoutParams) opt.getLayoutParams();
                                radioParams.setMargins(0, 0, 0, 30);
                                opt.setLayoutParams(radioParams);
                            }

                            container_layout.addView(group);

                            break;
                        //TEXT AS ANSWER
                        case "2":
                            EditText textAnswer = new EditText(getApplicationContext());
                            textAnswer.setMaxLines(5);
                            textAnswer.setTextColor(Color.BLACK);

                            container_layout.addView(textAnswer);

                            LinearLayout.LayoutParams editParams = (LinearLayout.LayoutParams) textAnswer.getLayoutParams();
                            editParams.setMargins(0, 0, 0, 30);
                            editParams.height = 150;
                            textAnswer.setLayoutParams(editParams);

                            break;
                        //MULTIPLE
                        case "3":
                            LinearLayout checkGroup = new LinearLayout(getApplicationContext());
                            checkGroup.setOrientation(RadioGroup.VERTICAL);

                            for (DataSnapshot options : snapshot.child("options").getChildren()) {
                                String option = String.valueOf(options.getValue());

                                CheckBox opt = new CheckBox(getApplicationContext());
                                opt.setText(option);
                                opt.setTextColor(Color.BLACK);
                                checkGroup.addView(opt);

                                LinearLayout.LayoutParams checkParams = (LinearLayout.LayoutParams) opt.getLayoutParams();
                                checkParams.setMargins(0, 0, 0, 30);
                                opt.setLayoutParams(checkParams);
                            }

                            container_layout.addView(checkGroup);

                            break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}