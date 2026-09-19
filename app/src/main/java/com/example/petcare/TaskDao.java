package com.example.petcare.db;

import androidx.dao.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflict;
import androidx.room.Query;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflict.REPLACE)
    long insert(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE petId = :petId ORDER BY dueMillis")
    LiveData<List<Task>> getByPet(long petId);

    @Query("SELECT * FROM tasks WHERE petId IN (SELECT id FROM pets WHERE ownerId = :ownerId) ORDER BY dueMillis")
    LiveData<List<Task>> getByOwner(long ownerId);
}