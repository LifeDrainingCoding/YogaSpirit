package com.pracktic.yogaspirit.placeholder;

import com.pracktic.yogaspirit.data.consts.MeditationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestsPlaceholder {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<TestItem> ITEMS = new ArrayList<TestItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, TestItem> ITEM_MAP = new HashMap<String, TestItem>();

    private static final int COUNT = 3;

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(TestItem item) {
        ITEMS.add(item);
    }

    private static TestItem createPlaceholderItem(int position) {
        return new TestItem(MeditationType.values()[position]);
    }



    /**
     * A placeholder item representing a piece of content.
     */
    public static class TestItem {

        public MeditationType type;
        public TestItem(MeditationType type) {
            this.type = type;
        }

        @Override
        public String toString() {
           return type.testName;
        }
    }
}