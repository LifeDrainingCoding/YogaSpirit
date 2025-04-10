package com.pracktic.yogaspirit.fragments;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.adapters.DairyAdapter;
import com.pracktic.yogaspirit.data.Note;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.interfaces.OnDataIO;
import com.pracktic.yogaspirit.data.interfaces.OnDataLoader;
import com.pracktic.yogaspirit.data.interfaces.OnDataUploader;
import com.pracktic.yogaspirit.data.user.Dairy;
import com.pracktic.yogaspirit.data.user.Session;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class DairyFragment extends Fragment implements BiConsumer<Note, Integer> ,
        OnDataIO<UserData> {
    private int edit, delete;

    private RecyclerView list;

    private ExtendedFloatingActionButton fab;

    private FrameLayout frameLayout;

    private TextInputEditText title, content;

    private Note noteToDelete;

    private Button saveBtn, backBtn;

    private Session session;



    public DairyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        delete = R.id.delete_menu_item;
        edit = R.id.edit_menu_item;
        session = SessionManager.restoreSession(requireContext());

        View rootView = inflater.inflate(R.layout.fragment_dairy, container, false);
        frameLayout = new FrameLayout(requireContext());



        fab = rootView.findViewById(R.id.fab);

        Dairy dairy = new Dairy(notes -> {
            if (isAdded() && getContext() != null) {
                requireActivity().runOnUiThread(()->{

                        list = new RecyclerView(requireContext());
                        frameLayout.removeView(list);
                        list = new RecyclerView(requireContext());
                        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        list.setLayoutParams(params);
                        list.setLayoutManager(new LinearLayoutManager(requireContext()));
                        list.setAdapter(new DairyAdapter(notes, this));

                        frameLayout.addView(list, 0);

                });
            }
        });

        fab.setOnClickListener(v -> {
            accept(new Note("", ""), edit);
        });



        frameLayout.addView(rootView);

        dairy.loadMyNotes(requireContext());

        return frameLayout;
    }

    @Override
    public void accept(Note note, Integer id) {
        if (id == edit){
            frameLayout.removeAllViews();
            View rootView = getLayoutInflater().inflate(R.layout.note, frameLayout,false);
            frameLayout.addView(rootView);

            title = rootView.findViewById(R.id.title_note);
            content = rootView.findViewById(R.id.content_note);

            title.setText(note.getTitle());
            content.setText(note.getContent());

            backBtn = rootView.findViewById(R.id.back_note_btn);
            saveBtn = rootView.findViewById(R.id.save_note_btn);

            saveBtn.setOnClickListener(v -> {
                DBUtils.getUserData(session, userData -> {

                    String noteTitle, noteContent = "";

                    if (title.getText()!= null && !title.getText().toString().trim().isEmpty() &&
                            !title.getText().toString().trim().isBlank() ){
                        noteTitle = title.getText().toString();
                        if (content.getText() != null){
                            noteContent = content.getText().toString();
                        }
                        note.setContent(noteContent);
                        note.setTitle(noteTitle);
                        if (userData.getNotes()!=null){

                            userData.getNotes().put(note.getId(),note);

                        }else {
                            userData.setNotes(new HashMap<>(){{
                                put(note.getId(),note);
                            }});
                        }

                        DBUtils.uploadUserData(session, userData, this);


                    }else {
                        Toast.makeText(requireContext(), "Нельзя сохранить пустое название",Toast.LENGTH_LONG).show();
                    }


                });
            });

            backBtn.setOnClickListener(v -> {
               reload();
            });



        }else if (id == delete){
            DairyAdapter adapter = ((DairyAdapter) list.getAdapter());
            if (adapter != null) {
                adapter.deleteItem(note);

                noteToDelete = note;
                DBUtils.getUserData(session,this);

            }
        }
    }

    @Override
    public void onLoad(UserData userData) {
        userData.getNotes().remove(noteToDelete.getId());
        DBUtils.uploadUserData(session, userData, this);
    }

    @Override
    public void onUpload() {
        Toast.makeText(requireContext(), "Данные успешно загружены", Toast.LENGTH_SHORT).show();
    }
    private void reload(){
        frameLayout.removeAllViews();
        delete = R.id.delete_menu_item;
        edit = R.id.edit_menu_item;
        session = SessionManager.restoreSession(requireContext());

        View rootView = getLayoutInflater().inflate(R.layout.fragment_dairy, frameLayout, false);

        CoordinatorLayout coordinatorLayout = ((CoordinatorLayout) rootView);


        Dairy dairy = new Dairy(notes -> {
            if (isAdded() && getContext() != null) {
                requireActivity().runOnUiThread(()->{

                    list = new RecyclerView(requireContext());
                    frameLayout.removeView(list);
                    list = new RecyclerView(requireContext());
                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    list.setLayoutParams(params);
                    list.setLayoutManager(new LinearLayoutManager(requireContext()));
                    list.setAdapter(new DairyAdapter(notes, this));

                    coordinatorLayout.addView(list);
                });
            }
        });
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            accept(new Note("", ""), edit);
        });

        frameLayout.addView(rootView);

        dairy.loadMyNotes(requireContext());
    }
}