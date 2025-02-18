package com.pracktic.yogaspirit.data;

import com.pracktic.yogaspirit.data.consts.MeditationType;

public record Article(String desc, String title, int level, MeditationType type) {

}
