package com.pracktic.yogaspirit.data;

import com.pracktic.yogaspirit.data.consts.MeditationType;

public record Article(String title, String desc, int level, MeditationType type) {

}
