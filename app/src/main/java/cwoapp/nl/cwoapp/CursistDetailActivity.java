package cwoapp.nl.cwoapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cwoapp.nl.cwoapp.databinding.ActivityCursistDetailBinding;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.MockEntityGenerator;

public class CursistDetailActivity extends AppCompatActivity {

    ActivityCursistDetailBinding activityCursistDetailBinding;
    Cursist cursist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCursistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_cursist_detail);

        Long cursistId = getIntent().getLongExtra("cursistId", 0);
        cursist = MockEntityGenerator.createFullCursist(cursistId.intValue());
        displayCursistInfo();
    }

    private void displayCursistInfo() {
        if (cursist == null)
            return;

        activityCursistDetailBinding.textviewNaam.setText(cursist.nameToString());
        activityCursistDetailBinding.textViewOpmerking.setText(cursist.opmerking);
        if (cursist.paspoort == null)
            activityCursistDetailBinding.textViewPaspoort.setText("nee");
        else
            activityCursistDetailBinding.textViewPaspoort.setText("ja");
    }
}
