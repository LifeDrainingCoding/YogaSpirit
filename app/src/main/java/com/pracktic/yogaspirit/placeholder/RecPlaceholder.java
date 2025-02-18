package com.pracktic.yogaspirit.placeholder;

import com.pracktic.yogaspirit.data.MeditationURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
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