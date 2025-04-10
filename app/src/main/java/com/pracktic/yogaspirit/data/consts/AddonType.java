package com.pracktic.yogaspirit.data.consts;

public enum AddonType {
    ARTICLES("Статьи"), AUDIO("Аудио медитации");

    public final String text;
    AddonType(String text){
        this.text = text;
    }
}
