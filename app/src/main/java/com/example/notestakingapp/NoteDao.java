package com.example.notestakingapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    public void Insert(Note note);

    @Update
    public void Update(Note note);

    @Delete
    public void Delete(Note note);

    @Query("SELECT * FROM my_notes")
    public LiveData<List<Note>> getAllData();


}
