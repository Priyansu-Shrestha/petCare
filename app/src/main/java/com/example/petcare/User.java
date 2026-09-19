package com.example.petcare.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @androidx.room.ColumnInfo(name = "email")
    public String email;

    @androidx.room.ColumnInfo(name = "password_hash")
    public String passwordHash;

    public String name;
}