package com.pracktic.yogaspirit.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.consts.TabTypes;

//todo appBar допилить
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        runOnUiThread();
        tabLayout = findViewById(R.id.tabLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag()!=null && tab.getTag() instanceof String tag ){
                    try {

                        TabTypes tabType = TabTypes.valueOf(tag);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,tabType.fragment)
                                .commit();


                    }catch (IllegalArgumentException ex){
                        Toast.makeText(MainActivity.this, "Ошибка в названии тега!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"ТЕГ НЕ СТРОКА, СООБЩИТЕ РАЗРАБОТЧИКУ!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}