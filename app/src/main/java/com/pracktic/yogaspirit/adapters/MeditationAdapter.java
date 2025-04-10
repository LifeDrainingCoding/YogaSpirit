package com.pracktic.yogaspirit.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.interfaces.OnDataIO;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.interfaces.OnDataLoader;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.databinding.FragmentMeditationBinding;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;


public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {
    private static final String TAG = MeditationAdapter.class.getName();
    private final List<Article> items;
    private OnDataLoader<UserData> onDataIO;
    private List<TextView> descriptions;

    public MeditationAdapter(List<Article> items, OnDataLoader<UserData> onDataIO) {
        this.items = items;
        this.onDataIO = onDataIO;
        descriptions = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMeditationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.title.setText(items.get(position).title());
        holder.title.setOnClickListener(v -> toggleVisibility(holder.description));
        holder.description.setText(items.get(position).desc());
        descriptions.add(holder.description);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView description;

        public Article item;

        public ViewHolder(FragmentMeditationBinding binding) {
            super(binding.getRoot());
            title = binding.articleTittle;
            description = binding.articleDescription;
            description.setVisibility(GONE);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + description.getText() + "'";
        }
    }
    private void toggleVisibility(TextView textView){
        if (textView.getVisibility() == GONE){
            boolean allGone =  descriptions.stream().allMatch(textView1 -> textView1.getVisibility() == GONE);
            if (allGone){
                Timer.getInstance().startTimer();
                Log.d(TAG, "toggleVisibility: timer started");
            }

            textView.setVisibility(VISIBLE);
        }else {

            boolean anyVisible  = descriptions.stream().anyMatch(textView1 -> {
                return textView1.getVisibility() == VISIBLE && !textView1.equals(textView);
            });

            if (!anyVisible){
                if (textView.getContext() != null){
                    Context context = textView.getContext();
                    DBUtils.getUserData(SessionManager.restoreSession(context),onDataIO);
                }else {
                    Log.e(TAG, "toggleVisibility: ", new RuntimeException("TEXTVIEW CONTEXT ARE NULL!") );
                }
            }


            textView.setVisibility(GONE);



        }
    }
}