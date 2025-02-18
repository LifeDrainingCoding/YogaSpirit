package com.pracktic.yogaspirit.script;

import android.util.Log;

import com.pracktic.yogaspirit.data.Article;
import com.pracktic.yogaspirit.data.consts.DepressionArticles;
import com.pracktic.yogaspirit.data.consts.MeditationType;
import com.pracktic.yogaspirit.data.consts.WarnArticles;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArticleGather {
    private static final String  TAG = ArticleGather.class.getName();
    private static final String STRESS_URL = "https://www.psychologies.ru/wellbeing/snyat-stress-za-minutu-6-korotkih-meditatsiy/";

    public static List<Article> getStressArticles(){
         try {
             Elements container = Jsoup.connect(STRESS_URL)
                     .get().select(".block-text");
             container.removeIf(element -> element.hasClass("place-for-creative"));
             container.removeIf(element -> container.indexOf(element)<2);

             List<String> h2s = new ArrayList<>();
             List<String> ps =  new ArrayList<>();

             container.forEach(element -> {
                 Log.d(TAG, "getStressArticles: tag: "+element.tagName());
             });

             int i = 0;

             while (i < container.size()) {
                 Element element = container.get(i);

                 if (element.tagName().equals("h2")) {
                     StringBuilder combinedText = new StringBuilder(element.text());


                     while (i + 1 < container.size() && container.get(i + 1).tagName().equals("h2")) {
                         combinedText.append(" ").append(container.get(i + 1).text());
                         i++;
                     }

                     h2s.add(combinedText.toString());
                 } else if (element.tagName().equals("p")) {
                     ps.add(element.text());
                 }

                 i++;
             }
             List<Article> articles = new ArrayList<>();
             if (ps.size() == h2s.size()){
                 for (int j = 0; j < ps.size(); j++) {
                     articles.add(new Article(h2s.get(j), ps.get(j), j, MeditationType.STRESS));
                 }

             }else {
                 Log.e(TAG, "getStressArticles: titles and descriptions aren't same size! " );
             }
             return articles;
         }catch (IOException ex){
             Log.e(TAG, "getStressArticles: error during connection", ex );
             return null;
         }
    }
    public static List<Article> getDepressionArticles () {
        DepressionArticles[] consts = DepressionArticles.values();
        List<DepressionArticles> constsList = List.of(consts);
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i <constsList.size() ; i++) {
            DepressionArticles depressionArticles = constsList.get(i);
            ArrayList<String> arrayList = Arrays.stream(depressionArticles.text.split("\\.")).collect(Collectors.toCollection(ArrayList::new));
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j < arrayList.size(); j++) {
                sb.append(arrayList.get(j));
            }
            Article article = new Article(arrayList.get(0),sb.toString(),i,MeditationType.DEPRESSION);
            articles.add(article);
        }


            return articles;

    }
    public static List<Article> getWarnArticles(){

        WarnArticles[] consts = WarnArticles.values();
        List<WarnArticles> constsList = List.of(consts);
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i <constsList.size() ; i++) {
            WarnArticles warnArticles = constsList.get(i);
            ArrayList<String> arrayList = Arrays.stream(warnArticles.text.split("\\.")).collect(Collectors.toCollection(ArrayList::new));
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j < arrayList.size(); j++) {
                sb.append(arrayList.get(j));
            }
            Article article = new Article(arrayList.get(0),sb.toString(),i,MeditationType.WARN);
            articles.add(article);
        }

        return articles;
    }
    public static List<Article> getAllArticles(){
        List<Article> allArticles = getStressArticles();
        CollectionUtils.addAll(allArticles, getDepressionArticles());
        CollectionUtils.addAll(allArticles, getWarnArticles());
        return allArticles;
    }

}
