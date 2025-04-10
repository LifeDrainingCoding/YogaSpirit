package com.pracktic.yogaspirit.script;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pracktic.yogaspirit.data.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TittleGather {

    private static final String TAG = TittleGather.class.getName();

    private Handler handler;
    private Context context;

    private AtomicInteger i;

    private List<String> urls, titles;
    private List<WebView> views;
    private Consumer<List<String>> callback;

    private HandlerThread thread;


    public TittleGather (  Context context){
        this.context = context;
    }

    public void getTitlesAsync(List<String> urls, Consumer<List<String>> callback){
        this.urls = urls;
        i = new AtomicInteger(0);
        titles = new ArrayList<>();
        this.views = new ArrayList<>();
        this.callback = callback;
        handler = new Handler(Looper.getMainLooper());
        Handler titleHandler = new Handler( handler.getLooper());

        titleHandler.post(()-> urls.forEach(this::getTitle));


    }



    @SuppressLint("SetJavaScriptEnabled")
    public void getTitle(String url){
        WebView view = new WebView(context);
        view.getSettings().setJavaScriptEnabled(true);
        views.add(view);
        if (!view.getSettings().getJavaScriptEnabled()){
            throw new IllegalStateException("Java Script isn't enabled");
        }
        view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loop(view);
            }
        });
        view.loadUrl(url);
    }

    private void checkTitles(){
        if ( i.get() >= urls.size()-1 ){
            Log.d(TAG, "checkTitles: starting to clear up browsers");
            views.forEach(view -> {
                view.onPause();
                view.pauseTimers();
                view.clearCache(true);
                view.clearHistory();
                view.setWebViewClient(null);
                view.destroy();
                Log.d(TAG, "checkTitles: view  has been destroyed");
                view = null;
            });
            callback.accept(titles);
        }else {
            i.getAndAdd(1);
        }
    }
    private void loop(WebView view){
        handler.postDelayed(()->{
            view.evaluateJavascript("$trackTitle.getHTML()", value -> {
                AtomicReference<String> atomRef = new AtomicReference<>(value.replace("\"", ""));
                if (atomRef.get().isBlank() || atomRef.get().isEmpty()){

                    loop(view);
                }else {

                        Log.d(TAG, "loop: iteration "+ i.get());
                        titles.add(atomRef.get());
                        checkTitles();

                }
                Log.d(TAG, "loop: value "+atomRef.get());
            });
        }, 100);
    }
}
