package com.pracktic.yogaspirit.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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
import com.pracktic.yogaspirit.adapters.RecAdapter;
import com.pracktic.yogaspirit.data.MeditationURL;
import com.pracktic.yogaspirit.data.interfaces.OnDataIO;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.singleton.Timestamp;
import com.pracktic.yogaspirit.data.user.Personalisation;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.script.TittleGather;
import com.pracktic.yogaspirit.utils.DBUtils;
import com.pracktic.yogaspirit.utils.DateUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.pracktic.yogaspirit.utils.PixelUtils.*;
/**
 * A fragment representing a list of Items.
 */
public class RecFragment extends Fragment implements Consumer<List<MeditationURL>>,
        OnDataIO<UserData> {

    private FrameLayout frameLayout;
    private static final String TAG = RecFragment.class.getName();


    private RecyclerView list;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rec_item_list, container, false);


        try {
            this.frameLayout = new FrameLayout(requireContext());

            RecyclerView list = new RecyclerView(requireContext());

            Context context = list.getContext();

            list.setLayoutManager(new LinearLayoutManager(context));


            Personalisation.loadMyURLs(context, this);

            frameLayout.addView(list);
            return frameLayout;
        }catch (IllegalStateException ex) {
            Log.e(TAG, "onCreateView: fragment not attached to a context! ", ex);
        }
        return rootView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void accept(List<MeditationURL> meditationURLS) {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                new TittleGather(requireActivity() ).getTitlesAsync(meditationURLS.stream().map(MeditationURL::audioURL)
                        .collect(Collectors.toList()), strings -> {
                    if (getActivity() == null){
                        onPause();
                        onDestroy();
                        return;
                    }
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            frameLayout.removeAllViews();
                            RecyclerView.LayoutParams layoutParams =
                                    new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            WeakReference<Resources> resources = new WeakReference<>(requireContext().getResources());
                            layoutParams.setMargins(dpToPx(resources.get(),16 ), 0,dpToPx(resources.get(),16),0);
                            RecyclerView list = new RecyclerView(requireContext());
                            list.setLayoutParams(layoutParams);
                            list.setLayoutManager(new LinearLayoutManager(requireContext()));

                            list.setAdapter(new RecAdapter(meditationURLS,strings , meditationURL -> {

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
                                        DBUtils.getUserData(SessionManager.restoreSession(requireContext()), RecFragment.this);

                                        try {
                                            frameLayout.removeAllViews();

                                            RecyclerView recyclerView = new RecyclerView(requireContext());

                                            Context context = recyclerView.getContext();

                                            recyclerView.setLayoutManager(new LinearLayoutManager(context));


                                            Personalisation.loadMyURLs(context, RecFragment.this);

                                            frameLayout.addView(recyclerView);

                                        } catch (IllegalStateException ex) {
                                            Log.e(TAG, "onCreateView: fragment not attached to a context! ", ex);
                                        }

                                    });

                                    browser.setWebViewClient(viewClient);
                                    browser.loadUrl(meditationURL.audioURL());
                                    frameLayout.addView(meditation);
                                });

                            }));
                            frameLayout.addView(list);
                        }
                    });

                });
            });


        }
    }

    @Override
    public void onLoad(UserData userData) {
        Timestamp timestamp = Timer.getInstance().stopTimer();
        if (userData.getMeditationStats() == null){
            userData.setMeditationStats(new HashMap<>());
        }
        if (timestamp!= null){
            userData.getMeditationStats().merge(DateUtils.getStringFromDate(timestamp.getDate()), timestamp.getTotalTime(), Integer::sum);



            DBUtils.uploadUserData(SessionManager.restoreSession(requireContext()), userData, this);
        }else {
            Toast.makeText(requireContext(), "Подожди пока загрузится страничка!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpload() {
        Toast.makeText(requireContext(), "Успешно загружена статистика", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (frameLayout != null) {
            WebView webView = frameLayout.findViewById(R.id.browser);
            if (webView != null) {
                webView.clearCache(true);
                webView.clearHistory();
                webView.onPause();
                webView.removeAllViews();
                webView.destroy();
            }
            frameLayout.removeAllViews();
        }
    }
}