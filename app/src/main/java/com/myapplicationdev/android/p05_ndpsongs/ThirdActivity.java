package com.myapplicationdev.android.p05_ndpsongs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    EditText etID, etTitle, etSingers, etYear;
    Button btnCancel, btnUpdate, btnDelete;
    RatingBar rbUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        setTitle(getTitle().toString() + " ~ " + getResources().getText(R.string.title_activity_third));

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        etID = (EditText) findViewById(R.id.etID);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etSingers = (EditText) findViewById(R.id.etSingers);
        etYear = (EditText) findViewById(R.id.etYear);
        rbUpdate = (RatingBar) findViewById(R.id.ratingBarUpdate);

        Intent i = getIntent();
        final Song currentSong = (Song) i.getSerializableExtra("song");

        etID.setText(currentSong.getId()+"");
        etTitle.setText(currentSong.getTitle());
        etSingers.setText(currentSong.getSingers());
        etYear.setText(currentSong.getYearReleased()+"");
        rbUpdate.setRating(currentSong.getStars());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(ThirdActivity.this);
                currentSong.setTitle(etTitle.getText().toString().trim());
                currentSong.setSingers(etSingers.getText().toString().trim());
                int year = 0;
                try {
                    year = Integer.valueOf(etYear.getText().toString().trim());
                } catch (Exception e){
                    Toast.makeText(ThirdActivity.this, "Invalid year", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentSong.setYearReleased(year);

                currentSong.setStars(rbUpdate.getRating());
                int result = dbh.updateSong(currentSong);
                if (result>0){
                    Toast.makeText(ThirdActivity.this, "Song updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThirdActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ThirdActivity.this);

                myBuilder.setTitle("Danger");
                myBuilder.setMessage("Are you sure you want to delete the island " + currentSong.getTitle());
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("CANCEL", null);
                myBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbh = new DBHelper(ThirdActivity.this);
                        int result = dbh.deleteSong(currentSong.getId());
                        if (result>0){
                            Toast.makeText(ThirdActivity.this, "Song deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ThirdActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ThirdActivity.this);

                myBuilder.setTitle("Danger");
                myBuilder.setMessage("Are you sure you want to discard the changes");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("DO NOT DISCARD", null);
                myBuilder.setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

    }


}