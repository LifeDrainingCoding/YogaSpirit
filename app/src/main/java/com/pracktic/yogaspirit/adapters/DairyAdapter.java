package com.pracktic.yogaspirit.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.Note;
import com.pracktic.yogaspirit.databinding.DairyItemBinding;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DairyAdapter extends RecyclerView.Adapter<DairyAdapter.ViewHolder> {

    private List<Note> items;
    private BiConsumer<Note, Integer> callback;

    private final int edit , delete ;
    public DairyAdapter(List<Note> items, BiConsumer<Note, Integer> callback){
        this.callback = callback;
        this.items = items;
        edit = R.id.edit_menu_item;
        delete = R.id.delete_menu_item;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(DairyItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false ));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        holder.item = items.get(pos);
        holder.title.setText(holder.item.getTitle());
        holder.btn.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(v.getContext(),v);
            menu.getMenuInflater().inflate(R.menu.dairy_list, menu.getMenu());
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                callback.accept(holder.item, item.getItemId());
                return true;
            });
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public Note item;
        public MaterialTextView title;
        public ImageView btn;
        public ViewHolder(@NonNull DairyItemBinding binding) {
            super(binding.getRoot());
            title = binding.dairyTitleItem;
            btn = binding.menuBtn;
        }
    }

    public void deleteItem(Note note){
        int i = items.indexOf(note);
        items.remove(note);
        notifyItemRemoved(i);
    }
}
