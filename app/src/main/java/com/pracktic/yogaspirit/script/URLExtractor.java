package com.pracktic.yogaspirit.script;

import android.content.res.Resources;
import android.util.Log;

import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.MeditationURL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URLExtractor {
    public static List<MeditationURL> getUrls(Resources resources)  {
        try(InputStream is =  resources.openRawResource(R.raw.audio_links);) {
            byte[] buffer =  new byte[is.available()];
            is.read(buffer);
            String src = new String(buffer, StandardCharsets.UTF_8);
            List<String> items = Arrays.stream(src.split("\n")).collect(Collectors.toList());
            List<MeditationURL> urls =  new ArrayList<>();

            items.forEach(s -> {
                String[] parts = s.split(";");
                String url = parts[0].split("\"")[0];
                urls.add(new MeditationURL(url,parts[1],Integer.parseInt(parts[2])));
            });


            urls.forEach(s -> {
                Log.i("Resources", "getUrls: "+s.audioURL());
            });

            return urls;
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
