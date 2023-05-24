package com.example.notestakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.notestakingapp.databinding.ActivityDataInsertBinding;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataInsertActivity extends AppCompatActivity {

    ActivityDataInsertBinding binding;
    Date date;
    int day;
    int month;
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDataInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type=getIntent().getStringExtra("type");

        //Updating a previously stored note
        if(type.equals("updateMode")){
            binding.txtsetUpdate.setText("Update a note");
            binding.txtTitle.setText(getIntent().getStringExtra("title"));
            binding.txtDesc.setText(getIntent().getStringExtra("desc"));
            binding.txtDate.setText(getIntent().getStringExtra("date"));
            int id=getIntent().getIntExtra("id",0);
            binding.btnAdd.setText("Update Note");



            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();

                    intent.putExtra("title", binding.txtTitle.getText().toString());
                    intent.putExtra("desc", binding.txtDesc.getText().toString());
                    intent.putExtra("date",binding.txtDate.getText().toString());

                    intent.putExtra("id",getIntent().getIntExtra("id",0));

                    intent.putExtra("day",day);
                    intent.putExtra("month",month);
                    intent.putExtra("year",year);

//                    Toast.makeText(DataInsertActivity.this,"Updating note :"+binding.txtDesc.getText().toString(),Toast.LENGTH_LONG)
//                            .show();

                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        //Adding a new note
        else {
            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("title", binding.txtTitle.getText().toString());
                    intent.putExtra("desc", binding.txtDesc.getText().toString());

                    Toast.makeText(DataInsertActivity.this,"date is"
                    +binding.txtDate.getText().toString(),Toast.LENGTH_SHORT).show();

                    intent.putExtra("date",binding.txtDate.getText().toString());

                    intent.putExtra("day",day);
                    intent.putExtra("month",month);
                    intent.putExtra("year",year);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        binding.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar dt= Calendar.getInstance();
                 year=dt.get(Calendar.YEAR);
                 month=dt.get(Calendar.MONTH);
                 day=dt.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(DataInsertActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        binding.txtDate.setText(i2+" - "+(i1+1)+ " - "+i);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DataInsertActivity.this,MainActivity.class));
    }
}