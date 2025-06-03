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
import com.pracktic.yogaspirit.adapters.StatsAdapter;
import com.pracktic.yogaspirit.data.interfaces.OnDataIO;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.Stat;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.utils.DBUtils;
import com.pracktic.yogaspirit.utils.DateUtils;
import com.pracktic.yogaspirit.utils.PixelUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ProgressFragment extends Fragment implements OnDataIO<UserData> {

    private static final String TAG = ProgressFragment.class.getName();
    private FrameLayout frameLayout;


    public ProgressFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_progress, container, false);
        frameLayout = rootView.findViewById(R.id.frame_progress);

        DBUtils.getUserData(SessionManager.restoreSession(requireContext()), this);

        return rootView;
    }


    @Override
    public void onLoad(UserData userData) {
        requireActivity().runOnUiThread(()->{
            Toast.makeText(requireActivity(), "Загружена статистика", Toast.LENGTH_SHORT).show();

            RecyclerView recyclerView = new RecyclerView(requireContext()){{
                setLayoutManager(new LinearLayoutManager(requireContext()));
                setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT){{
                    setMargins(PixelUtils.dpToPx(getResources(),16), PixelUtils.dpToPx(getResources(), 16),
                            PixelUtils.dpToPx(getResources(), 16), PixelUtils.dpToPx(getResources(), 16));
                }});
            }};


            List<Stat> stats = new ArrayList<>();

            if (userData.getMeditationStats()==null){
                Toast.makeText(getContext(), "Нет статистики", Toast.LENGTH_SHORT).show();
                return;
            }

            userData.getMeditationStats().forEach((s, integer) -> {
                stats.add(new Stat(DateUtils.getLDFromString(s),integer/60));
            });

            stats.sort(Comparator.comparing(Stat::date));
            stats.add(0,new Stat(LocalDate.now(), 0));

            stats.forEach(stat -> {
                Log.d(TAG, "onLoad: stat: "+DateUtils.getStringFromDate(stat.date())+" "+stat.seconds());
            });

            recyclerView.setAdapter(new StatsAdapter(stats));

            frameLayout.addView(recyclerView);
        });
    }

    @Override
    public void onUpload() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}