package com.senierr.shootbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.senierr.lib.ShootButton;

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
                } else {
                    customView.setCenterMode(ShootButton.MODE_CENTER_CIRCLE);
                }
            }
        });
    }
}

