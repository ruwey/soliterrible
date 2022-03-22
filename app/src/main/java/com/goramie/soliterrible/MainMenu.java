package com.goramie.soliterrible;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPress(View v) {
        setContentView(R.layout.game);
    }
}