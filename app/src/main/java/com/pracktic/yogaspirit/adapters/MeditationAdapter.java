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
import com.pracktic.yogaspirit.data.OnDataIO;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.singleton.Timestamp;
import com.pracktic.yogaspirit.data.user.Session;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.databinding.FragmentMeditationBinding;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.List;
import java.util.concurrent.Callable;


public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {
    private static final String TAG = MeditationAdapter.class.getName();
    private final List<Article> items;
    private OnDataIO<UserData> onDataIO;

    public MeditationAdapter(List<Article> items, OnDataIO<UserData> onDataIO) {
        this.items = items;
        this.onDataIO = onDataIO;
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
            Timer.getInstance().startTimer();
            textView.setVisibility(VISIBLE);
        }else {

            textView.setVisibility(GONE);
            if (textView.getContext() != null){
                //todo проверить как работает здесь, адаптер вроде создается в UI потоке.
                Context context = textView.getContext();
                DBUtils.getUserData(SessionManager.restoreSession(context),onDataIO);
            }else {
                Log.e(TAG, "toggleVisibility: ", new RuntimeException("TEXTVIEW CONTEXT ARE NULL!") );
            }



        }
    }
}