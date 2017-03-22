package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.URL;

import cwoapp.nl.cwoapp.databinding.ActivityCursistDetailBinding;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CursistDetailActivity extends AppCompatActivity {

    ActivityCursistDetailBinding activityCursistDetailBinding;
    Cursist cursist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCursistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_cursist_detail);

        Long cursistId = getIntent().getLongExtra("cursistId", 0);
        loadCursistData(cursistId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cursist_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_edit) {
            showEditCursist();
            // TODO go to edit cursist.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditCursist() {
        Context context = this;
        Class destinationClass = EditCursistActivity.class;
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);

    }


    private void loadCursistData(Long cursistId) {
        new FetchCursistTask().execute(cursistId);
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

    class FetchCursistTask extends AsyncTask<Long, Void, Cursist> {

        @Override
        protected void onPreExecute() {
            activityCursistDetailBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Cursist doInBackground(Long... id) {
            URL curistUrl = NetworkUtils.buildUrl("cursist", id[0].toString());

            try {
                String jsonCursistResponse = NetworkUtils.getResponseFromHttpUrl(curistUrl);
                Cursist cursist = OpenJsonUtils.getCursist(jsonCursistResponse);
                return cursist;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursist cursistObject) {
            activityCursistDetailBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            cursist = cursistObject;
            displayCursistInfo();
        }
    }
}
