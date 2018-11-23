package com.hva.joris.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    private GameObject gameObject;
    private EditText title, platform, notes;
    private Spinner status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        gameObject = intent.getParcelableExtra(MainActivity.EXTRA_GAME);

        title = findViewById(R.id.titleEditField);
        platform = findViewById(R.id.platformEditField);
        notes = findViewById(R.id.notesEditField);
        status = findViewById(R.id.statusSpinner);

        //Fill the fields with the correct data if gameObject is not null
        if(gameObject != null) {
            title.setText(gameObject.getTitle());
            platform.setText(gameObject.getPlatform());
            notes.setText(gameObject.getNotes());
            status.setSelection(gameObject.getStatus());
        }
        else {
            gameObject = new GameObject("","","", 1, "");
        }

        final long id = gameObject.getId();

        FloatingActionButton saveEdit = findViewById(R.id.saveEdit);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsNotEmpty()) {
                    GameObject gameObject = new GameObject(title.getText().toString(), platform.getText().toString(),notes.getText().toString(), status.getSelectedItemPosition(), DateFormat.getDateInstance().format(new Date()));
                    gameObject.setId(id);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXTRA_GAME, gameObject);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(v, "Please make sure all fields are filled in correctly", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean fieldsNotEmpty() {
        return !title.getText().toString().isEmpty() && !platform.getText().toString().isEmpty() && !notes.getText().toString().isEmpty();
    }
}
