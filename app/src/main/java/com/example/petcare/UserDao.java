package com.example.petcare.db;

import androidx.dao.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflict;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflict.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    LiveData<User> findByEmail(String email);
}