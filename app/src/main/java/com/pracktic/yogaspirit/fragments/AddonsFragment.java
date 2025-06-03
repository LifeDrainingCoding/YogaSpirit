package com.pracktic.yogaspirit.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.adapters.AddonsAdapter;
import com.pracktic.yogaspirit.adapters.MeditationAdapter;
import com.pracktic.yogaspirit.adapters.RecAdapter;
import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.MeditationURL;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.consts.AddonType;
import com.pracktic.yogaspirit.data.interfaces.OnDataLoader;
import com.pracktic.yogaspirit.data.interfaces.OnDataUploader;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.singleton.Timestamp;
import com.pracktic.yogaspirit.data.user.Personalisation;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.script.ArticleGather;
import com.pracktic.yogaspirit.script.TittleGather;
import com.pracktic.yogaspirit.script.URLExtractor;
import com.pracktic.yogaspirit.utils.DBUtils;
import com.pracktic.yogaspirit.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class AddonsFragment extends Fragment implements Consumer<AddonType> {

    private FrameLayout frameLayout;
    private RecyclerView list;

    public AddonsFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addons_list, container, false);

        frameLayout = new FrameLayout(requireActivity());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            list = (RecyclerView) view;
            list.setLayoutManager(new LinearLayoutManager(context));

            list.setAdapter(new AddonsAdapter(this));
            frameLayout.addView(list);
        }

        return frameLayout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void accept(AddonType addonType) {
        frameLayout.removeAllViews();

        switch (addonType){
            case AUDIO -> {
                list = (RecyclerView) LayoutInflater.from(requireContext()).inflate(R.layout.inflater_list,frameLayout,false);

                List<MeditationURL> allUrls = URLExtractor.getUrls(getResources());
                new TittleGather(requireContext()).getTitlesAsync(
                        allUrls.stream().map(MeditationURL::audioURL).collect(Collectors.toList()),
                        strings -> {
                            list.setLayoutManager(new LinearLayoutManager(requireContext()));
                            list.setAdapter(new RecAdapter(allUrls, strings, meditationURL -> {
                                requireActivity().runOnUiThread(()->{
                                    frameLayout.removeAllViews();
                                    View meditation = getLayoutInflater().inflate(R.layout.fragment_audio, frameLayout, false);
                                    WebView browser = meditation.findViewById(R.id.browser);

                                    browser.clearHistory();
                                    browser.clearCache(true);
                                    browser.loadUrl("about:blank");
                                    browser.getSettings().setJavaScriptEnabled(true);
                                    WebViewClient viewClient = new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            super.onPageFinished(view, url);
                                            Timer.getInstance().startTimer();

                                        }
                                    };
                                    MaterialButton endBtn = meditation.findViewById(R.id.endMeditation);
                                    endBtn.setOnClickListener(v -> {
                                        DBUtils.getUserData(SessionManager.restoreSession(requireContext()), userData -> {
                                            Timestamp timestamp = Timer.getInstance().stopTimer();
                                            if (userData.getMeditationStats() == null){
                                                userData.setMeditationStats(new HashMap<>());
                                            }
                                            userData.getMeditationStats().merge(DateUtils.getStringFromDate(timestamp.getDate()), timestamp.getTotalTime(), Integer::sum);

                                            DBUtils.uploadUserData(SessionManager.restoreSession(requireContext()), userData, this::statsUploaded);

                                        });
                                        reload();
                                    });

                                    browser.setWebViewClient(viewClient);
                                    browser.loadUrl(meditationURL.audioURL());
                                    frameLayout.addView(meditation);
                                });


                            }));
                            frameLayout.addView(list);
                        });



            }
            case ARTICLES -> {

                 ArticleGather.getAllArticles(articles -> requireActivity().runOnUiThread(()->{
                     frameLayout.removeAllViews();
                     list = (RecyclerView) LayoutInflater.from(requireContext()).inflate(R.layout.inflater_list,frameLayout,false);
                     list.setVerticalScrollBarEnabled(true);
                     list.setScrollbarFadingEnabled(false);
                     list.setScrollBarSize(16);

                     list.setAdapter(new MeditationAdapter(articles, userData -> {
                         Timestamp timestamp = Timer.getInstance().stopTimer();
                         if (userData.getMeditationStats() == null) {
                             userData.setMeditationStats(new HashMap<>());
                         }
                         userData.getMeditationStats().merge(DateUtils.getStringFromDate(timestamp.getDate()),timestamp.getTotalTime(),Integer::sum);
                         DBUtils.uploadUserData(SessionManager.restoreSession(requireContext()),userData, this::statsUploaded);
                     }));

                     list.setLayoutManager(new LinearLayoutManager(frameLayout.getContext()));


                     frameLayout.addView(list);
                 }));
            }

        }

    }
    private void statsUploaded(){
        Toast.makeText(requireContext(),"Успешно загружена статистика",Toast.LENGTH_SHORT).show();
    }
    private void reload(){
        frameLayout.removeAllViews();
        View rootView = getLayoutInflater().inflate(R.layout.fragment_addons_list, frameLayout, false);


        if (rootView instanceof RecyclerView) {
            Context context = rootView.getContext();
            list = (RecyclerView) rootView;
            list.setLayoutManager(new LinearLayoutManager(context));

            list.setAdapter(new AddonsAdapter(this));
            frameLayout.addView(list);
        }

    }
}