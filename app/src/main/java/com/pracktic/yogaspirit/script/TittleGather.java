package com.pracktic.yogaspirit.script;

import android.util.Log;

import com.pracktic.yogaspirit.data.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TittleGather {

    private static final String TAG = TittleGather.class.getName();





    public static void getTitlesAsync(List<String> urls, Consumer<List<String>> callback){
        new Thread(()->{
            List<String> titles = new ArrayList<>();
            urls.forEach(s -> {
                titles.add(getTitle(s));
            });
            callback.accept(titles);
        }).start();

    }

    public static List<String> getTitles(List<String> urls ){
        List<String> titles = new ArrayList<>();
        urls.forEach(s -> {
            titles.add(getTitle(s));
        });
        return titles;
    }

    public static String getTitle(String url){
        try{
            Element element = Jsoup.connect(url).get().getElementById("clip-title");
            if (element!=null) return element.text();
            Log.i(TAG, "getTitle: loaded tittle"+element.text());
        }catch (IOException ex){
            Log.e(TAG, "getTitle: error during getting title", ex );
        }
            return null;
    }

    public static String getArticleTitle(Article article){

    }

}
