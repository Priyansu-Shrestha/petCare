package com.example.petcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.petcare.R;
import com.example.petcare.db.Pet;

public class PetAdapter extends ListAdapter<Pet, PetAdapter.PetViewHolder> {

    private final OnPetActionListener listener;
    private final Context context;

    public interface OnPetActionListener {
        void onEdit(Pet pet);
        void onDelete(Pet pet);
    }

    public PetAdapter(Context context, OnPetActionListener listener) {
        super(diffCallback());
        this.context = context;
        this.listener = listener;
        private static DiffUtil.ItemCallback<Pet> diffCallback() {
            return new DiffUtil.ItemCallback<Pet>() {
                @Override
                public boolean areItemsTheSame(Pet oldItem, Pet newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(Pet oldItem, Pet newItem) {
                    return oldItem.name.equals(newItem.name) &&
                            oldItem.species.equals(newItem.species);
                }
            };
        }

        @NonNull
        @Override
        public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
            return new PetViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
            Pet pet = getItem(position);
            holder.bind(pet);
        }

        class PetViewHolder extends RecyclerView.ViewHolder {
            private final android.widget.TextView tvName, tvSpecies;

            public PetViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvPetName);
                tvSpecies = itemView.findViewById(R.id.tvPetSpecies);
            }

            public void bind(Pet pet) {
                tvName.setText(pet.name);
                tvSpecies.setText(pet.species);
                itemView.setOnClickListener(v -> listener.onEdit(pet));
                itemView.setOnLongClickListener(v -> {
                    listener.onDelete(pet);
                    return true;
                });
            }
        }
    }