package com.pracktic.yogaspirit.placeholder;

import com.pracktic.yogaspirit.data.MeditationURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecPlaceholder {

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final MeditationURL url;
        public final String title;

        public PlaceholderItem(MeditationURL url, String title) {
            this.url = url;
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}