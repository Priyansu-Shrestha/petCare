package com.example.petcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.petcare.R;
import com.example.petcare.db.AppDatabase;
import com.example.petcare.db.Pet;
import com.example.petcare.db.PetDao;
import java.util.List;

public class PetListFragment extends Fragment {

    private PetAdapter adapter;
    private PetDao petDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);

        RecyclerView rvPets = view.findViewById(R.id.rvPets);
        rvPets.setLayoutManager(new LinearLayoutManager(requireContext()));

        petDao = AppDatabase.get(requireContext()).petDao();
        adapter = new PetAdapter(requireContext(), pet -> {
            // Edit pet
            Intent i = new Intent(requireContext(), PetFormActivity.class);
            i.putExtra("petId", pet.id);
            i.putExtra("petName", pet.name);
            i.putExtra("petSpecies", pet.species);
            startActivity(i);
        }, pet -> {
            // Delete pet
            petDao.delete(pet);
            adapter.submitList(getPets());
        });

        rvPets.setAdapter(adapter);

        long userId = getArguments().getLong("userId", -1);
        if (userId != -1) {
            petDao.getByOwner(userId).observe(getViewLifecycleOwner(), adapter::submitList);
        }

        view.findViewById(R.id.fabAddPet).setOnClick(v -> {
            Intent i = new Intent(requireContext(), PetFormActivity.class);
            i.putExtra("isNew", true);
            startActivity(i);
        });

        return view;
    }

    private List<Pet> getPets() {
        long userId = getArguments().getLong("userId", -1);
        return userId != -1 ? petDao.getByOwner(userId).getValue() : null;
    }
}