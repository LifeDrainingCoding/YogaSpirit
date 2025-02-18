package com.pracktic.yogaspirit.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pracktic.yogaspirit.fragments.TestsFragment;
import com.pracktic.yogaspirit.placeholder.TestsPlaceholder.TestItem;
import com.pracktic.yogaspirit.databinding.FragmentTestsBinding;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TestItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {

    private final List<TestItem> mValues;
    private final WeakReference<TestsFragment> fragment;

    public TestsAdapter(List<TestItem> items, TestsFragment fragment) {
        mValues = items;
        this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTestsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);
        holder.holder.setOnClickListener(view -> {
            fragment.get().initTest(holder.item);
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView testName;
        public final LinearLayout holder;
        public TestItem item;

        public ViewHolder(FragmentTestsBinding binding) {
            super(binding.getRoot());
            testName = binding.testItem;
            holder  = binding.holder;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + testName.getText() + "'";
        }
    }
}