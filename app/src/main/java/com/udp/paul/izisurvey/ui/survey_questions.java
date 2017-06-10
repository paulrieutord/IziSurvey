package com.udp.paul.izisurvey.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference FBSurveyAnswers;
    private FirebaseUser currentUser;
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
        FBReference = FBDatabase.getReference("surveys").child(extras.getString(EXTRA_KEY)).child("questionSections");
        queryRef = FBReference.orderByChild("order");

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshotSection : dataSnapshot.getChildren()) {
                    TextView titleSection = new TextView(getApplicationContext());

                    String titleSectionString = String.valueOf(String.valueOf(snapshotSection.child("description").getValue()));
                    titleSection.setText(titleSectionString);
                    titleSection.setTextColor(Color.BLACK);
                    titleSection.setTypeface(null, Typeface.BOLD);

                    container_layout.addView(titleSection);

                    LinearLayout.LayoutParams textViewSectionParams = (LinearLayout.LayoutParams) titleSection.getLayoutParams();
                    textViewSectionParams.setMargins(0, 0, 0, 40);
                    titleSection.setLayoutParams(textViewSectionParams);

                    for (DataSnapshot snapshot : snapshotSection.child("questions").getChildren()) {
                        String type = String.valueOf(snapshot.child("type").getValue());

                        TextView title = new TextView(getApplicationContext());

                        String titleString = String.valueOf(snapshot.child("order").getValue()) + ") " + String.valueOf(snapshot.child("description").getValue());
                        title.setText(titleString);
                        title.setTextColor(Color.BLACK);

                        switch (type) {
                            //ONLY ONE OPTION
                            case "1":
                                RadioGroup group = new RadioGroup(getApplicationContext());
                                group.setOrientation(RadioGroup.VERTICAL);

                                group.addView(title);

                                for (DataSnapshot options : snapshot.child("options").getChildren()) {
                                    String option = String.valueOf(options.getValue());

                                    RadioButton opt = new RadioButton(getApplicationContext());
                                    opt.setText(option);
                                    opt.setId(Integer.valueOf(options.getKey()));
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
                                LinearLayout editTextLayout = new LinearLayout(getApplicationContext());
                                editTextLayout.setOrientation(RadioGroup.VERTICAL);

                                EditText textAnswer = new EditText(getApplicationContext());
                                textAnswer.setMaxLines(5);
                                textAnswer.setTextColor(Color.BLACK);

                                editTextLayout.addView(title);
                                editTextLayout.addView(textAnswer);
                                container_layout.addView(editTextLayout);

                                LinearLayout.LayoutParams editParams = (LinearLayout.LayoutParams) textAnswer.getLayoutParams();
                                editParams.setMargins(0, 0, 0, 30);
                                editParams.height = 150;
                                textAnswer.setLayoutParams(editParams);

                                break;
                            //MULTIPLE
                            case "3":
                                LinearLayout checkGroup = new LinearLayout(getApplicationContext());
                                checkGroup.setOrientation(RadioGroup.VERTICAL);

                                checkGroup.addView(title);

                                for (DataSnapshot options : snapshot.child("options").getChildren()) {
                                    String option = String.valueOf(options.getValue());

                                    CheckBox opt = new CheckBox(getApplicationContext());
                                    opt.setText(option);
                                    opt.setId(Integer.valueOf(options.getKey()));
                                    opt.setTextColor(Color.BLACK);
                                    checkGroup.addView(opt);

                                    LinearLayout.LayoutParams checkParams = (LinearLayout.LayoutParams) opt.getLayoutParams();
                                    checkParams.setMargins(0, 0, 0, 30);
                                    opt.setLayoutParams(checkParams);
                                }

                                container_layout.addView(checkGroup);

                                break;
                        }

                        LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) title.getLayoutParams();
                        textViewParams.setMargins(0, 0, 0, 40);
                        title.setLayoutParams(textViewParams);
                    }
                }

                Button sendQuestions = new Button(getApplicationContext());
                sendQuestions.setText("ENVIAR");

                container_layout.addView(sendQuestions);

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                sendQuestions.setLayoutParams(buttonParams);
                sendQuestions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (formIsValid(container_layout)) {
                            sendData(container_layout);
                        } else {
                            Toast.makeText(getApplicationContext(), "Debe responder todas las preguntas.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean formIsValid(LinearLayout layout) {
        boolean isValid = true;

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);

            if (v instanceof RadioGroup) {
                if (((RadioGroup) v).getCheckedRadioButtonId() == -1) {
                    isValid = false;
                }
            } else if (v instanceof LinearLayout) {
                View vvv = ((LinearLayout) v).getChildAt(1);

                if (vvv instanceof EditText) {
                    if (((EditText) vvv).getText().toString().trim().length() == 0) {
                        isValid = false;
                    }
                } else if (vvv instanceof CheckBox) {
                    boolean isChecked = false;
                    for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                        View vv = ((LinearLayout) v).getChildAt(j);

                        if (vv instanceof  CheckBox) {
                            if (((CheckBox) vv).isChecked()) {
                                isChecked = true;
                            }
                        }
                    }

                    if (!isChecked) {
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }

    public void sendData(LinearLayout layout) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FBSurveyAnswers = FBDatabase.getReference("surveyAnswers").child(currentUser.getUid()).child(extras.getString(EXTRA_KEY));

        int sectionIndex = 0;

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof TextView) {
                sectionIndex++;
            } else if (v instanceof RadioGroup) {
                int idRadioButton = ((RadioGroup) v).getCheckedRadioButtonId();
                View radioButton = v.findViewById(idRadioButton);
                int radioId = ((RadioGroup) v).indexOfChild(radioButton);
                RadioButton btn = (RadioButton) ((RadioGroup) v).getChildAt(radioId);
                String selection = (String) btn.getText();

                Log.d("SECTION_INDEX", String.valueOf(sectionIndex));
                Log.d("SELECCION", idRadioButton + ") " + selection);
            } else if (v instanceof LinearLayout) {
                View vvv = ((LinearLayout) v).getChildAt(1);

                if (vvv instanceof EditText) {
                    String text = ((EditText) vvv).getText().toString().trim();

                    Log.d("SECTION_INDEX", String.valueOf(sectionIndex));
                    Log.d("TEXT", text);
                } else if (vvv instanceof CheckBox) {
                    for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                        View vv = ((LinearLayout) v).getChildAt(j);

                        if (vv instanceof CheckBox) {
                            if (((CheckBox) vv).isChecked()) {
                                String text = ((CheckBox) vv).getText().toString();

                                Log.d("SECTION_INDEX", String.valueOf(sectionIndex));
                                Log.d("CHECKBOX", String.valueOf(vv.getId()) + ") " + text );
                            }
                        }
                    }
                }
            }
        }
    }
}