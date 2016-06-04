package com.ataulm.basic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ataulm.basic.navigation.Navigator;
import com.ataulm.basic.navigation.Screen;
import com.ataulm.basic.navigation.UriCreator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopLevelActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.drawer)
    View drawerView;

    @BindView(R.id.button_screen_a)
    Button buttonScreenA;

    @BindView(R.id.button_screen_b)
    Button buttonScreenB;

    @BindView(R.id.button_screen_c)
    Button buttonScreenC;

    @BindView(R.id.drawer_layout_content)
    FrameLayout contentFrame;

    private Navigator navigator;
    private DrawerController drawerController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        ButterKnife.bind(this);

        ContentViewSetter contentViewSetter = new ContentViewSetter(getLayoutInflater(), contentFrame);
        navigator = Navigator.create(this);
        drawerController = new DrawerController(drawerLayout, Gravity.START);

        setupButtons();

        int layout = getScreenLayout(getIntent().getData());
        contentViewSetter.setContentView(layout);
    }

    @LayoutRes
    private int getScreenLayout(Uri data) {
        if (userIsOnInitialScreen()) {
            return R.layout.screen_a;
        }

        if (data.getPath().endsWith(Screen.B.getPath())) {
            return R.layout.screen_b;
        } else {
            return R.layout.screen_c;
        }
    }

    private void setupButtons() {
        buttonScreenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateTo(Screen.A);
            }
        });

        buttonScreenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateTo(Screen.B);
            }
        });

        buttonScreenC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateTo(Screen.C);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerController.isDrawerOpen()) {
            drawerController.closeDrawer();
        } else if (userIsOnInitialScreen()) {
            finish();
        } else {
            goToInitialScreen();
        }
    }

    private boolean userIsOnInitialScreen() {
        Uri data = getIntent().getData();
        return data == null || data.getPath().endsWith(Screen.A.getPath());
    }

    private void goToInitialScreen() {
        navigator.navigateTo(Screen.A);
    }

}
