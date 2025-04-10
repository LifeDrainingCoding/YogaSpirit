package com.pracktic.yogaspirit.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.adapters.TestsAdapter;
import com.pracktic.yogaspirit.data.Quiz;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.user.Session;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.placeholder.TestsPlaceholder;
import com.pracktic.yogaspirit.utils.DBUtils;
import com.pracktic.yogaspirit.widgets.RadioButtonExt;


import org.apache.commons.collections4.IterableUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class TestsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private TestsPlaceholder.TestItem item;
    private FrameLayout frameLayout;

    private MaterialButton next, prev;
    private MaterialTextView textView;

    private List<RadioButtonExt> buttons;

    private RadioGroup radioGroup;
    private static final String TAG = "TestsFragment";

    private boolean isBackPressed;

    private Quiz quiz;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestsFragment() {
    }


    public static TestsFragment newInstance(int columnCount) {
        TestsFragment fragment = new TestsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isBackPressed){
                    frameLayout.removeAllViews();

                    View testList = getLayoutInflater().inflate(R.layout.fragment_tests_list,frameLayout, false);

                    RecyclerView recyclerView = testList.findViewById(R.id.list);
                    Context context = recyclerView.getContext();

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    recyclerView.setAdapter(new TestsAdapter(TestsPlaceholder.ITEMS, TestsFragment.this));

                    frameLayout.addView(testList);
                    isBackPressed  = true;
                    return ;
                }


                requireActivity().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isBackPressed = true;
        View rootView = inflater.inflate(R.layout.fragment_tests_list, container, false);

        try {
            this.frameLayout = new FrameLayout(requireContext());

            RecyclerView recyclerView = rootView.findViewById(R.id.list);
            Context context = recyclerView.getContext();

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new TestsAdapter(TestsPlaceholder.ITEMS, this));
            frameLayout.addView(rootView);
        return frameLayout;
        }catch (IllegalStateException ex) {
            Log.e(TAG, "onCreateView: fragment not attached to a context! ", ex);
        }
        return frameLayout;
    }


    public void initTest(TestsPlaceholder.TestItem item){


        this.item = item;
        isBackPressed = false;

        frameLayout.removeAllViews();

        View test = getLayoutInflater().inflate(R.layout.fragment_poll, frameLayout, false);
        quiz = new Quiz(item.type,item.type.questions, item.type.btnTexts,getContext(), getResources());
        buttons = quiz.getRadioButtons();
        radioGroup =  test.findViewById(R.id.radio_group);

        Log.d(TAG, "initTest: buttons size "+buttons.size());
        for (int i = 0; i<buttons.size(); i++){
            radioGroup.addView(buttons.get(i));
        }


        for (int i = 0; i<buttons.size(); i++){

            buttons.get(i).setOnClickListener((btn) -> {
                RadioButtonExt rbtn = ((RadioButtonExt) btn);
                buttons.forEach(radioButtonExt -> {
                    if (radioButtonExt.isChecked() && !radioButtonExt.equals(rbtn)){
                        quiz.resultLevel = quiz.resultLevel - rbtn.level;
                    }
                });
                quiz.resultLevel = quiz.resultLevel + rbtn.level;
                quiz.questionMap.put(quiz.currentPos, rbtn.level);

                boolean isTestFinished = quiz.questionMap.values().stream().allMatch(Objects::nonNull);
                if (isTestFinished){
                    buttons.forEach(radioButtonExt -> {
                        radioButtonExt.setEnabled(false);
                    });
                    Log.i(TAG, "initTest: questionMap size "+quiz.questionMap.size());
                    updateQuestion("Тест завершен, ваш уровень "+item.type.txt +": "+quiz.getLevel()+
                            "\n "+item.type.getDescForLevel(quiz.getLevel()));

                    saveLevel();

                }
                Log.d(TAG, "initTest: resultLevel "+quiz.resultLevel);
            });

        }

        frameLayout.addView(test);

        initTxtView(test);
        initBtns(test);
        buttons.forEach(radioButtonExt -> {
            Log.d(TAG, "initTest: isButtonVisible"+radioButtonExt.getVisibility());
        });
    }


    private void initBtns(View rootView){
        prev = rootView.findViewById(R.id.prevBtn);
        next = rootView.findViewById(R.id.nextBtn);

        if (quiz.currentPos <= 0){
            disableBtn(prev);
        }

        prev.setOnClickListener(v -> {
            updateQuestion(quiz.prev());
            MaterialButton btn = ((MaterialButton) v);
            enableBtn(next);
            if (quiz.currentPos<=0){

                disableBtn(btn);
            }else {
                enableBtn(btn);
            }
        });

        next.setOnClickListener(v -> {
            updateQuestion(quiz.next());
            MaterialButton btn = ((MaterialButton) v);
            enableBtn(prev);
            if (quiz.currentPos>=quiz.getQuestions().size()-1){
                disableBtn(btn);
            }else {
                enableBtn(btn);
            }

        });

    }

    private void initTxtView(View rootView){
        textView = rootView.findViewById(R.id.pollTxt);
        textView.setText(quiz.getQuestions().get(quiz.currentPos));
    }

    private void enableBtn(MaterialButton btn){
        btn.setEnabled(true);
    }
    private void disableBtn(MaterialButton btn){
        btn.setEnabled(false);
    }
    private void updateQuestion(String s){
        textView.setText(s);

        radioGroup.clearCheck();
        if (quiz.questionMap.get(quiz.currentPos) == null){
            Log.i(TAG, "updateQuestion: updated question are null");
            buttons.forEach(radioButtonExt -> {
                radioButtonExt.setChecked(false);
            });
        }else {
            Log.i(TAG, "updateQuestion: updated question aren't null");
           RadioButtonExt  chosenBtn =  IterableUtils.find(buttons, btn -> {
               return btn.level == quiz.questionMap.get(quiz.currentPos);
           });
           chosenBtn.setChecked(true);
        }
    }

    private void saveLevel(){
        Session session = SessionManager.restoreSession(requireContext());
        DatabaseReference userRef = DBUtils.getUsersRef();
        userRef.child(session.getLogin()).get().addOnSuccessListener(dataSnapshot -> {

            UserData userData = dataSnapshot.getValue(UserData.class);
            Log.d(TAG, "loaded userData: "+userData);

            assert userData != null;
            HashMap<String,Integer> levels = new HashMap<>();

            if (userData.getLevels()!= null){
              levels =  userData.getLevels();
            }

            levels.put(item.type.name(),quiz.getLevel());
            userData.setLevels(levels);
            userRef.child(session.getLogin()).setValue(userData, (error, ref) -> {
                if (error != null){
                    Log.e(TAG, "saveLevel: FAILED TO SAVE USERDATA", error.toException() );
                    return;
                }
                Log.i(TAG, "saveLevel: successfully saved userData");
                Toast.makeText(getContext(),"Успешно сохранены результаты теста", Toast.LENGTH_SHORT ).show();
            });
        });
    }

}