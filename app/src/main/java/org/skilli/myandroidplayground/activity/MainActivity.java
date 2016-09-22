package org.skilli.myandroidplayground.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.skilli.myandroidplayground.R;
import org.skilli.myandroidplayground.databinding.MainActivityBinding;
import org.skilli.myandroidplayground.handler.Handlers;
import org.skilli.myandroidplayground.model.User;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private TextSwitcher mSwitcher;
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpUI();
        setTextswitcher();
    }

    private void setTextswitcher() {
        mSwitcher = (TextSwitcher) findViewById(R.id.switcher);

        mSwitcher.setFactory(mFactory);

        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter++;
                mSwitcher.setText(String.valueOf(mCounter));
            }
        });

        mSwitcher.setCurrentText(String.valueOf(mCounter));
    }

    private void setUpUI() {
        User user = new User("Test", "User");
        binding.setUser(user);
        binding.setHandlers(new Handlers());
        binding.concealActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConcealActivity.class));
            }
        });

        binding.buttonReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConcealActivity.class));
            }
        });
    }

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {

            // Create a new TextView
            TextView t = new TextView(MainActivity.this);
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_Large);
            return t;
        }
    };
}