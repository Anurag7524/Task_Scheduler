package com.example.notestakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notestakingapp.databinding.ActivityMainBinding;

import java.sql.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NoteViewModel viewModel;

    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel=new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

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


            Note note=new Note(title,desc,date);
            viewModel.insert(note);
            Toast.makeText(this,"Note added",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode==2){
            String title=data.getStringExtra("title");
            String desc=data.getStringExtra("desc");
            desc=desc+" ";
//            Toast.makeText(MainActivity.this, "desc is :"+descUpdate, Toast.LENGTH_LONG).show();
            String date=data.getStringExtra("date");

            Note note=new Note(title,desc,date);
            note.setId(data.getIntExtra("id",0));
            viewModel.update(note);
//            Toast.makeText(this,"Note updated",Toast.LENGTH_SHORT).show();
        }
    }
}