package com.pracktic.yogaspirit.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.pracktic.yogaspirit.data.OnDataIO;
import com.pracktic.yogaspirit.data.OnDataLoader;
import com.pracktic.yogaspirit.data.OnDataUploader;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.singleton.Timer;
import com.pracktic.yogaspirit.data.singleton.Timestamp;
import com.pracktic.yogaspirit.data.user.Personalisation;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class RecFragment extends Fragment implements Consumer<List<MeditationURL>>,
        OnDataIO<UserData> {

    private FrameLayout frameLayout;
    private static final String TAG = RecFragment.class.getName();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private RecyclerView list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecFragment newInstance(int columnCount) {
        RecFragment fragment = new RecFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rec_item_list, container, false);


        try {
            this.frameLayout = new FrameLayout(requireContext());

            list = rootView.findViewById(R.id.list);

            Context context = list.getContext();

            list.setLayoutManager(new LinearLayoutManager(context));


            Personalisation.loadMyURLs(context, this);

            frameLayout.addView(rootView);
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
            list.setAdapter(new RecAdapter(meditationURLS, meditationURL -> {
                requireActivity().runOnUiThread(() -> {
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
                        DBUtils.getUserData(SessionManager.restoreSession(requireContext()), this);

                        try {
                            frameLayout.removeAllViews();

                            View rootView = getLayoutInflater().inflate(R.layout.rec_item_list, frameLayout, false);

                            list = rootView.findViewById(R.id.list);

                            Context context = list.getContext();

                            list.setLayoutManager(new LinearLayoutManager(context));


                            Personalisation.loadMyURLs(context, this);

                            frameLayout.addView(rootView);

                        } catch (IllegalStateException ex) {
                            Log.e(TAG, "onCreateView: fragment not attached to a context! ", ex);
                        }

                    });

                    browser.setWebViewClient(viewClient);
                    browser.loadUrl(meditationURL.audioURL());
                    frameLayout.addView(meditation);

                });
            }));
        }
    }

    @Override
    public void onLoad(UserData userData) {
        Timestamp timestamp = Timer.getInstance().stopTimer();
        if (userData.getMeditationStats() == null){
            userData.setMeditationStats(new HashMap<>());
        }
        userData.getMeditationStats().merge(timestamp.getDate(), timestamp.getTotalTime(), Integer::sum);

        DBUtils.uploadUserData(SessionManager.restoreSession(requireContext()), userData, this);

    }

    @Override
    public void onUpload() {
        Toast.makeText(requireContext(), "Успешно загружена статистика", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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