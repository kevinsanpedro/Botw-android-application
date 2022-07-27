package com.example.a3_kevin_kpsanpedro.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a3_kevin_kpsanpedro.OnRowItemClickListener;
import com.example.a3_kevin_kpsanpedro.databinding.CustomRowLayoutBinding;
import com.example.a3_kevin_kpsanpedro.model.BotwWeapon;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Context context;
    private final List<BotwWeapon> itemArrayList;
    CustomRowLayoutBinding binding;
    private final OnRowItemClickListener clickListener;

    public ItemAdapter(Context context, List<BotwWeapon> items, OnRowItemClickListener clickListener){
        this.itemArrayList = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(CustomRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        BotwWeapon currentItem = itemArrayList.get(position);
        holder.bind(context, currentItem, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.itemArrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        CustomRowLayoutBinding itemBinding;

        public ItemViewHolder(CustomRowLayoutBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, BotwWeapon currentItem, OnRowItemClickListener clickListener){
            itemBinding.tvName.setText("Name: " + currentItem.getName().substring(0,1).toUpperCase()+ currentItem.getName().substring(1));
            itemBinding.tvDiscription.setText("Description: " + currentItem.getDescription());
            itemBinding.tvAttack.setText("Attack: "+currentItem.getAttack());
            itemBinding.tvDefense.setText("Defense: "+currentItem.getDefense());
            Glide.with(context).load(currentItem.getImage()).into(itemBinding.iVWeapon);

            // click detection from the root
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.OnItemClickListener(currentItem);
                }
            });
        }
    }
}
