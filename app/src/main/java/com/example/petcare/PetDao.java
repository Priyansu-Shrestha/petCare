package com.example.petcare;

import androidx.dao.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflict;
import androidx.room.Query;

@Dao
public interface PetDao {
    @Insert(onConflict = OnConflict.REPLACE)
    long insert(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId")
    LiveData<List<Pet>> getByOwner(long ownerId);

    @Query("SELECT * FROM pets WHERE id = :id")
    LiveData<Pet> getById(long id);
}