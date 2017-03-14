package cwoapp.nl.cwoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CursistDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_detail);

        Long cursistId = getIntent().getLongExtra("cursistId", 0);
    }
}
