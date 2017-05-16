package com.senierr.demo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.senierr.shootbutton.ShootButton;

public class MainActivity extends AppCompatActivity {

    private ShootButton customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = (ShootButton) findViewById(R.id.cv_view);
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customView.getCenterMode() == ShootButton.MODE_CENTER_CIRCLE) {
                    customView.setCenterMode(ShootButton.MODE_CENTER_RECT);
                    customView.setCenterColorRes(R.color.colorPrimaryDark);
                } else {
                    customView.setCenterMode(ShootButton.MODE_CENTER_CIRCLE);
                    customView.setCenterColorRes(R.color.colorAccent);
                }
            }
        });
    }
}

