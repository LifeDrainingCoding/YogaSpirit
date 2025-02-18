package com.pracktic.yogaspirit.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.textview.MaterialTextView;
import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.MeditationURL;
import com.pracktic.yogaspirit.placeholder.RecPlaceholder.PlaceholderItem;
import com.pracktic.yogaspirit.databinding.RecItemBinding;
import com.pracktic.yogaspirit.script.TittleGather;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private final List<MeditationURL> items;
    private final List<String> titles;
    private final Consumer<MeditationURL> callback;

    public RecAdapter(List<MeditationURL> items, Consumer<MeditationURL> callback) {
        this.items = items;
        titles = TittleGather.getTitles(items.stream().map(MeditationURL::audioURL)
                .collect(Collectors.toList()));
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(RecItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {

        holder.item = new PlaceholderItem(items.get(pos) ,titles.get(pos));
        holder.title.setText(titles.get(pos));
        holder.recItem.setOnClickListener(v -> {
            callback.accept(holder.item.url);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final MaterialTextView title;
        public final LinearLayout recItem;
        public PlaceholderItem item;

        public ViewHolder(RecItemBinding binding) {
            super(binding.getRoot());
            title = binding.title;
            recItem = binding.recItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + item.url.audioURL()+ "'";
        }
    }
}