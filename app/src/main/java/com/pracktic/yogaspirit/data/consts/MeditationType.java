package com.pracktic.yogaspirit.data.consts;

import java.util.List;
import static com.pracktic.yogaspirit.data.consts.TextConst.BtnConst.*;
public enum MeditationType {
    DEPRESSION("Тест на уровень депрессии", DEPRESSION_TEST,
            TextConst.Questions.DEPRESSION, TextConst.Levels.DEPRESSION_LEVELS,"депрессии" ),
    STRESS("Тест на уровень стресса", STRESS_TEST, TextConst.Questions.STRESS,
            TextConst.Levels.STRESS_LEVELS,"стресса"),
    WARN("Тест на уровень тревожности", WARN_TEST, TextConst.Questions.WARN ,
            TextConst.Levels.WARN_LEVELS,"тревожности");
    public final String testName, txt;
    public final List<String> btnTexts, desc;
    public final List<String> questions;
    MeditationType(String testName, List<String> btnTexts, List<String> questions,
                   List<String> desc,String txt){
        this.txt = txt;
        this.desc = desc;
        this.testName = testName;
        this.btnTexts = btnTexts;
        this.questions =  questions;
    }

    public String getDescForLevel(int level ){
        if(level <=0){
            switch (this){
                case WARN -> {
                    return "Вы совсем не тревожны. К сожалению, рекомендации подобрать не можем.";
                }
                case STRESS -> {
                    return "У вас отсутствует стресс. К сожалению, рекомендации подобрать не можем.";
                }
                case DEPRESSION -> {
                    return "У вас отсутствует депрессия. К сожалению, рекомендации подобрать не можем";
                }
            }
        }
        return desc.get(level-1);
    }
}
