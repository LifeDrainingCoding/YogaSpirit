package com.pracktic.yogaspirit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.pracktic.yogaspirit.data.Stat;
import com.pracktic.yogaspirit.databinding.StatItemBinding;
import com.pracktic.yogaspirit.utils.DateUtils;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
    private List<Stat> items;

    public StatsAdapter(List<Stat> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatsAdapter.ViewHolder holder, int pos) {
        holder.item  = items.get(pos);
        if (pos == 0){
            holder.date.setText("Дата");
            holder.time.setText("Минуты");
        }else {
            holder.date.setText(DateUtils.getStringFromDate(holder.item.date()));
            holder.time.setText(String.valueOf(holder.item.seconds()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public Stat item;
        public MaterialTextView date, time;
        public ViewHolder(@NonNull StatItemBinding binding) {
            super(binding.getRoot());
            date = binding.dateItem;
            time = binding.timeItem;
        }
    }
}
