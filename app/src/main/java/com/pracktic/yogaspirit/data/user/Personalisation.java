package com.pracktic.yogaspirit.data.user;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;


import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.MeditationURL;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.script.ArticleGather;
import com.pracktic.yogaspirit.script.URLExtractor;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Personalisation {
    private static final String TAG = Personalisation.class.getName();
    public static void loadMyURLs(Context context,  Consumer<List<MeditationURL>> consumer){
        Session session = SessionManager.restoreSession(context);

        DBUtils.getUsersRef().child(session.getLogin()).get().addOnSuccessListener(dataSnapshot -> {
            UserData userData = dataSnapshot.getValue(UserData.class);

            List<MeditationURL> allUrls = URLExtractor.getUrls(context.getResources());
            assert userData != null;
            if (userData.getLevels() == null || userData.getLevels().isEmpty()){
                consumer.accept(new ArrayList<>());
                Toast.makeText(context, "Нет рекомендаций, пройдите хотя бы 1 тест!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "loadMyURLs: levels are null");
            }

           List<MeditationURL> myUrls =  new ArrayList<>();

           allUrls.forEach(meditationURL -> {
               userData.getLevels().forEach((s, integer) -> {
                   if (meditationURL.meditationType().equals(s)&& integer>= meditationURL.unlockLevel()){
                       myUrls.add(meditationURL);
                   }
               });
           });

            Log.d(TAG, "loadMyURLs: loaded "+myUrls.size()+" urls");
           consumer.accept(myUrls);

        }).addOnFailureListener(e -> {
            Toast.makeText(context,"Ошибка загрузки рекомендаций!", Toast.LENGTH_SHORT).show();
        });

    }
    public static void loadMyArticles(Context context, Consumer<List<Article>> callback){
        Session session = SessionManager.restoreSession(context);
        DBUtils.getUsersRef().child(session.getLogin()).get().addOnSuccessListener(dataSnapshot -> {

           UserData userData = dataSnapshot.getValue(UserData.class);
           List<Article> allArticle = ArticleGather.getAllArticles();

            assert userData != null;
            if (userData.getLevels() == null || userData.getLevels().isEmpty()){
                callback.accept(new ArrayList<>());
                Toast.makeText(context, "Нет рекомендаций, пройдите хотя бы 1 тест!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "loadMyArticles: levels are null");
            }
            List<Article> myArticles = new ArrayList<>();

            allArticle.forEach(article -> {
                userData.getLevels().forEach((s, integer) -> {
                    if (article.type().name().equals(s) && integer>= article.level()){
                        myArticles.add(article);
                    }
                });
            });

            callback.accept(myArticles);

        });

    }
}
