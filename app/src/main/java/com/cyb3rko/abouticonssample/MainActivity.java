package com.cyb3rko.abouticonssample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cyb3rko.abouticons.AboutIcons;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new AboutIcons(this, R.drawable.class).setTitle("Custom Title").get());
    }
}