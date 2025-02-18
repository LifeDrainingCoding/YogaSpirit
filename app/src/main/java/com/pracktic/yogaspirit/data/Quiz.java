package com.pracktic.yogaspirit.data;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.view.ContextThemeWrapper;

import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.consts.MeditationType;
import com.pracktic.yogaspirit.widgets.RadioButtonExt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Quiz implements PosCallback<String> {
    private final MeditationType type;
    private final List<String> questions;
    private final List<RadioButtonExt> radioButtons;

    private Resources resources;
    public final LinkedHashMap<Integer, Integer> questionMap;
    public int resultLevel = 0, levelMax = 1, difference = 5, currentPos = 0;
    public Quiz(MeditationType type, List<String> questions,
                List<String> btnTexts, Context context, Resources resources) {
        this.type = type;
        this.questions = questions;
        this.radioButtons = new ArrayList<>();
        questionMap =  new LinkedHashMap<>();
        this.resources = resources;

        ContextThemeWrapper themeWrapper =  new ContextThemeWrapper(context, R.style.Widget_Material3_CompoundButton_RadioButton);
        if  (btnTexts.size() % 2 == 0 && btnTexts.size() <= 8){
            for (int i = 0; i<btnTexts.size();i++ ){

                RadioButtonExt radioButtonExt = new RadioButtonExt(themeWrapper, i);
                radioButtonExt.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);

                questionMap.put(i,null);

                radioButtonExt.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT){{
                    setMargins(dpToPx(6), dpToPx(6),dpToPx(6),dpToPx(6));
                }});

                radioButtonExt.setText(btnTexts.get(i));
                if (i == btnTexts.size()-1){
                    levelMax = i;
                }
            }
        }
    }

    public int getLevel(){
        return (resultLevel*levelMax)/(levelMax*difference);
    }

    public MeditationType getType() {
        return type;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<RadioButtonExt> getRadioButtons() {
        return radioButtons;
    }


    @Override
    public String next() {
        currentPos++;
        return questions.get(currentPos);
    }

    @Override
    public String prev() {
        currentPos--;
        return questions.get(currentPos);
    }
   public int dpToPx(int dp){
        return (int) (dp*resources.getDisplayMetrics().density);
   }
}
