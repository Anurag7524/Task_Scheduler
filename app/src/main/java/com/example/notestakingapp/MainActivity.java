package com.example.notestakingapp;

import static android.app.AlarmManager.*;
import static android.app.AlarmManager.RTC_WAKEUP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.notestakingapp.databinding.ActivityMainBinding;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NoteViewModel viewModel;

    private PendingIntent notifIntent;
    private AlarmManager alarmMgr;
    Calendar calendar;

    String newNoteTitle;
    String updatedNoteTitle;

//    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS)!=
            PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},102);
            }
        }


         newNoteTitle="";
         updatedNoteTitle="";

        calendar= Calendar.getInstance();

        viewModel=new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        alarmMgr=(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(MainActivity.this,NotificationReceiver.class);
        notifIntent=PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        intent.putExtra("newTitle",newNoteTitle);
        intent.putExtra("updatedTitle",updatedNoteTitle);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
                intent.putExtra("type","addMode");
                startActivityForResult(intent,1);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVAdapter adapter=new RVAdapter();
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT){
                    Toast.makeText(MainActivity.this,"Deleting",Toast.LENGTH_LONG)
                            .show();
                    viewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                }
                else{
                    Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
                    intent.putExtra("title",adapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("desc",adapter.getNote(viewHolder.getAdapterPosition()).getDesc());
                    intent.putExtra("date",adapter.getNote(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra("id",adapter.getNote(viewHolder.getAdapterPosition()).getId());
                    intent.putExtra("type","updateMode");
                    startActivityForResult(intent,2);
                }
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            String title=data.getStringExtra("title");
            String desc=data.getStringExtra("desc");
            String date=data.getStringExtra("date");

            newNoteTitle=title;

            Note note=new Note(title,desc,date);
            viewModel.insert(note);

            sendNotification(date);

            Toast.makeText(this,"Note added",Toast.LENGTH_SHORT).show();
            Log.d("while adding a new note",date+" ");
        }
        else if(requestCode==2){
            String title=data.getStringExtra("title");
            String desc=data.getStringExtra("desc");
            desc=desc+" ";

            updatedNoteTitle=title;

            String date=data.getStringExtra("date");
//            String time=data.getStringExtra("time");

            Note note=new Note(title,desc,date);

            note.setId(data.getIntExtra("id",0));

            viewModel.update(note);

            sendNotification(date);

            Toast.makeText(MainActivity.this,"Note updated",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(String date) {

        Log.d("while adding a new note","Reached send notification method");
        if(date.length()>2){
            int[] dt=getDate(date);

            calendar.setTimeInMillis(System.currentTimeMillis());

            int day=dt[0];
            int month=dt[1];
            int year=dt[2];
            String newDate="";
            if(day<10){
                if(month<10){
                    newDate="0"+day+"/"+"0"+month+"/"+year;
                }
                else{
                    newDate="0"+day+"/"+month+"/"+year;
                }
            }
            else{
                if(month<10){
                    newDate=day+"/"+"0"+month+"/"+year;
                }
                else{
                    newDate=day+"/"+month+"/"+year;
                }
            }
            Log.d("while adding a new note","The new Date is :"+newDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            java.util.Date utilDate;
            try {
                utilDate = dateFormat.parse(newDate);

                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                calendar.setTime(utilDate);
                Log.d("while adding a new note","The calendar is set with the values: " +String.valueOf(calendar.getTime()));

                // Set the desired time
//                    calendar.set(Calendar.YEAR,Calendar.YEAR);
//                    calendar.set(Calendar.MONTH,Calendar.MONTH);
//                    calendar.set(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.HOUR_OF_DAY, 14); // Set hour to 9
                calendar.set(Calendar.MINUTE, 18); // Set minute to 30
                calendar.set(Calendar.SECOND, 0); // Set second to 0

                Log.d("while adding a new note","The values after setting the time are: "+calendar.getTime());
            }
            catch (ParseException e){
                e.printStackTrace();
            }

//               alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()
//               ,AlarmManager.INTERVAL_DAY,notifIntent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifIntent);
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifIntent);
            }

            Log.d("while adding a new note","alarm has been set");
        }
    }

    public int[] getDate(String date){
        String[] dt;
        dt = date.split(" - ");
        int[] dates=new int[3];
        dates[0]=Integer.parseInt(dt[0]);
        dates[1]=Integer.parseInt(dt[1]);
        dates[2]=Integer.parseInt(dt[2]);

//        Log.d("while adding a new note",dates[0]+" "+"month"+dates[1]+"year"+dates[2]);
        return dates;
    }


}