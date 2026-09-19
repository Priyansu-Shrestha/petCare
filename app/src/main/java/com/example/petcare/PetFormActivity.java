package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.petcare.R;
import com.example.petcare.db.AppDatabase;
import com.example.petcare.db.Pet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.Manifest;
import androidx.core.app.ActivityCompat;

public class PetFormActivity extends AppCompatActivity {

    private EditText etName, etSpecies;
    private Button btnSave, btnLocation;
    private long petId;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        etName = findViewById(R.id.etPetName);
        etSpecies = findViewById(R.id.etPetSpecies);
        btnSave = findViewById(R.id.btnSavePet);
        btnLocation = findViewById(R.id.btnSetLocation);
        client = LocationServices.getFusedLocationProviderClient(this);

        petId = getIntent().getLongExtra("petId", -1);
        boolean isNew = getIntent().getBooleanExtra("isNew", true);

        if (!isNew && petId != -1) {
            // Load existing pet (skip Room query for brevity; you can add Dao query here)
            etName.setText("Loaded Pet");
            etSpecies.setText("Dog");
        }

        btnSave.setOnClick(v -> savePet());
        btnLocation.setOnClick(v -> getCurrentLocation());
    }

    private void savePet() {
        String name = etName.getText().toString().trim();
        String species = etSpecies.getText().toString().trim();

        if (name.isEmpty() || species.isEmpty()) {
            Toast.makeText(this, "Name and species required", Toast.LENGTH_SHORT).show();
            return;
        }

        Pet pet = new Pet(name, species, 0); // ownerId set later
        if (petId != -1) {
            pet.id = petId; // update existing
        }
        AppDatabase.get(this).petDao().insert(pet);
        Toast.makeText(this, "Pet saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != androidx.core.content.PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        client.getLastLocation().addOnSuccessListener(this, loc -> {
            if (loc != null) {
                // Save lat/lng to pet (you'd extend Pet entity or use shared prefs)
                Toast.makeText(this, "Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}