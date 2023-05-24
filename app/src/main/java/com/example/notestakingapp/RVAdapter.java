package com.example.notestakingapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notestakingapp.databinding.EachNoteBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends ListAdapter<Note,RVAdapter.ViewHolder> {
    Context context;

    private List<Note> notes=new ArrayList<>();

    public RVAdapter(){
        super(CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Note> CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return ((oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDesc().equals(newItem.getDesc())) && oldItem.getDate().equals(newItem.getDate()));
        }
    };


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_note,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note=getItem(position);
        holder.binding.txtHeading.setText(note.getTitle());
        holder.binding.txtDesc.setText(note.getDesc());

        if(note.getDate().equals(""))
        {
            holder.binding.showDate.setVisibility(View.INVISIBLE);
            holder.binding.txtSetDate.setText("");
        }
        else{
            holder.binding.showDate.setVisibility(View.VISIBLE);
            holder.binding.txtSetDate.setText(note.getDate());
        }

    }

    public Note getNote(int position)
    {
        return getItem(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        EachNoteBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding=EachNoteBinding.bind(itemView);
        }
    }

}
