package cwoapp.nl.cwoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CreateDiplomaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diploma);
        loadCwoEisData();
    }

    private void loadCwoEisData() {
        new FetchCwoEisData().execute();
    }

    class FetchCwoEisData extends AsyncTask<String, Void, List<Diploma>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Diploma> doInBackground(String... params) {
            URL diplomaListUrl = NetworkUtils.buildUrl("diplomas");

            try {
                String jsonDiplomaLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
                List<Diploma> diplomaList = OpenJsonUtils.getDiplomaLijst(jsonDiplomaLijstResponse);
                return diplomaList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Diploma> diplomaList) {
            if (diplomaList != null) {
                List<DiplomaEis> diplomaEisenLijst = new ArrayList<>();
                for (int i = 0; i < diplomaList.size(); i++) {
                    diplomaEisenLijst.addAll(diplomaList.get(i).getDiplomaEis());
                }

            } else {
                // TODO hide volgende button.
            }
        }
    }
}
