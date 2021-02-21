package com.example.carwash;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.carwash.pageMenu.Ajouter;
import com.example.carwash.pageMenu.Lavage;
import com.example.carwash.pageMenu.Terminer;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class Menu extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        getSupportActionBar().hide();

        bubbleNavigationLinearView = findViewById(R.id.menu_bottom);

//        bubbleNavigationLinearView.setBadgeValue(0,"30");
//        bubbleNavigationLinearView.setBadgeValue(1,"20");
//        bubbleNavigationLinearView.setBadgeValue(2,"5");

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.page_container, new Ajouter());
        fragmentTransaction.commit();
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                switch (position)
                {
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.page_container, new Ajouter());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.page_container, new Lavage());
                        fragmentTransaction.commit();
                        break;

                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.page_container, new Terminer());
                        fragmentTransaction.commit();
                        break;


                }
            }
        });
    }
}
