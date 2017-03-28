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
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
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

    // TODO at the moment when you come back here after editing the cursist, all info is downloaded again, should be able to pass this back.
    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_edit) {
            showEditCursist();
            return true;
        } else if (itemThatWasClickedId == R.id.action_verbergen) {
            hideCursist();
            return true;
        } else if (itemThatWasClickedId == R.id.action_delete) {
            deleteCursist();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideCursist() {
        // TODO
    }

    private void deleteCursist() {
        new DeleteCursistTask().execute();
    }

    private void cursistDeleted() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_verwijderd), Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }

    private void showEditCursist() {
        Context context = this;
        Class destinationClass = EditCursistActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("cursist", cursist);
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

    class DeleteCursistTask extends AsyncTask<Long, Void, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {
            URL url = NetworkUtils.buildUrl("cursist", cursist.id.toString());
            int resultCode = 0;
            try {
                resultCode = NetworkUtils.sendToServer(url, "DELETE");
                return resultCode;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        /**
         * @param resultCode
         */
        @Override
        protected void onPostExecute(Integer resultCode) {
            if (resultCode == HttpURLConnection.HTTP_OK) {
                cursistDeleted();
            } else {
                // Handle error
            }
        }
    }
}
