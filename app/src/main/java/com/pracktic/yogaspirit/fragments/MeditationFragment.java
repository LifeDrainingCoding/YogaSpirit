package com.pracktic.yogaspirit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.interfaces.OnDataIO;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.singleton.Timestamp;
import com.pracktic.yogaspirit.data.user.Personalisation;
import com.pracktic.yogaspirit.adapters.MeditationAdapter;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.utils.DBUtils;
import com.pracktic.yogaspirit.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class MeditationFragment extends Fragment implements Consumer<List<Article>>,
        OnDataIO<UserData> {
    private static final String TAG = MeditationFragment.class.getName();
    private FrameLayout frameLayout;
    private RecyclerView list;


    public MeditationFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meditation_list, container, false);



        try {
            this.frameLayout = new FrameLayout(requireContext());




                Personalisation.loadMyArticles(requireActivity(),requireContext(), this);



            frameLayout.addView(rootView);
            return frameLayout;
        }catch (IllegalStateException ex) {
            Log.e(TAG, "onCreateView: fragment not attached to a context! ", ex);
        }
        return rootView;


        }

    @Override
    public void accept(List<Article> articles) {
        if (isAdded()){
            requireActivity().runOnUiThread(()->{
                frameLayout.removeAllViews();
                list = new RecyclerView(frameLayout.getContext());

                list.setAdapter(new MeditationAdapter(articles, this));

                list.setLayoutManager(new LinearLayoutManager(frameLayout.getContext()));


                frameLayout.addView(list);
            });

        }



    }

    @Override
    public void onLoad(UserData userData) {
        Timestamp timestamp = Timer.getInstance().stopTimer();
        if (userData.getMeditationStats() == null) {
            userData.setMeditationStats(new HashMap<>());
        }
        userData.getMeditationStats().merge(DateUtils.getStringFromDate(timestamp.getDate()),timestamp.getTotalTime(),Integer::sum);

        DBUtils.uploadUserData(SessionManager.restoreSession(requireContext()),userData, this);
    }

    @Override
    public void onUpload() {
        Toast.makeText(requireContext(),"Успешно загружена статистика", Toast.LENGTH_SHORT).show();
    }
}
