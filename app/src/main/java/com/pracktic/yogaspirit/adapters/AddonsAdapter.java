package com.pracktic.yogaspirit.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pracktic.yogaspirit.data.consts.AddonType;
import com.pracktic.yogaspirit.databinding.FragmentAddonBinding;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddonsAdapter extends RecyclerView.Adapter<AddonsAdapter.ViewHolder> {

    private final List<AddonType> items;
    private final Consumer<AddonType> callback;

    public AddonsAdapter(Consumer<AddonType> callback) {
        this.callback = callback;
        items = Arrays.stream(AddonType.values()).collect(Collectors.toList());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAddonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.title.setText(holder.item.text);
        holder.title.setOnClickListener(v -> {
            callback.accept(holder.item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public AddonType item;

        public ViewHolder(FragmentAddonBinding binding) {
            super(binding.getRoot());
            title = binding.addonTitle;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}