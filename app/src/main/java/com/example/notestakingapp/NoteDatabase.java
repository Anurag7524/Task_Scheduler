package com.example.notestakingapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.sql.Date;

@Database(entities = {Note.class},version = 6)
@TypeConverters({Converters.class})
public abstract class NoteDatabase  extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    @Entity
    public class User {
        private Date birthday;
    }

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext()
            ,NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
