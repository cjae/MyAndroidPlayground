package org.skilli.myandroidplayground.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.skilli.myandroidplayground.R;
import org.skilli.myandroidplayground.databinding.MainActivityBinding;
import org.skilli.myandroidplayground.handler.Handlers;
import org.skilli.myandroidplayground.model.User;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpUI();
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
    }
}
