package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CwoEis;
import cwoapp.nl.cwoapp.utility.MockEntityGenerator;

public class CursistBehaaldEisActivity extends AppCompatActivity implements CursistBehaaldEisAdapter.CursistBehaaldEisAdapterOnClickHandler {
    private List<CwoEis> cwoEisList;
    private ProgressBar loadingIndicator;
    private List<Cursist> cursistList;
    private TextView textViewNaam;
    private RecyclerView recyclerView;
    private CursistBehaaldEisAdapter cursistBehaaldEisAdapter;
    private Cursist currentCursist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_behaald_eis);
        cwoEisList = getIntent().getParcelableArrayListExtra("selectedCwoEisList");

        // get variables for elements in layout.
        textViewNaam = (TextView) findViewById(R.id.textViewNaam);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        recyclerView.setHasFixedSize(true);
        cursistBehaaldEisAdapter = new CursistBehaaldEisAdapter();
        recyclerView.setAdapter(cursistBehaaldEisAdapter);

        loadCursistListData();

    }

    private void loadCursistListData() {
        new FetchCursistListTask().execute();
    }

    private void showFirstCursist() {
        cursistBehaaldEisAdapter.setCwoListData(cwoEisList);
        showNextCursist();
    }

    private void showNextCursist() {
        if (cursistList.size() == 0) {
            backToMainActivity();
        } else {
            currentCursist = cursistList.remove(0);
            cursistBehaaldEisAdapter.setCursist(currentCursist);
            textViewNaam.setText(currentCursist.nameToString());
        }
    }

    private void backToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveEisenBehaald() {
        // TODO , save data
    }

    public void onClickShowVolgendeCursist(View view) {
        saveEisenBehaald();
        showNextCursist();
    }

    @Override
    public void onClick(CwoEis cwoEis, boolean behaald) {
        new SaveEisBehaaldTask().execute(currentCursist.id, cwoEis.getId(), behaald);
    }


    /**
     * Async task for loading external data of cursisten.
     */
    class FetchCursistListTask extends AsyncTask<String, Void, List<Cursist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Cursist> doInBackground(String... params) {

            List<Cursist> cursistList = MockEntityGenerator.createCursistList(5);
            return cursistList;
/*
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } */
        }


        @Override
        protected void onPostExecute(List<Cursist> cursistenList) {
            loadingIndicator.setVisibility(View.GONE);
            if (cursistenList != null) {
                cursistList = cursistenList;
                showFirstCursist();
            } else {
                showErrorMessage();
            }
        }
    }


    class SaveEisBehaaldTask extends AsyncTask<Object, Void, Boolean> {

        /**
         * params: Long cursist Id, Long cwoEis id, Boolean behaald.
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Object... params) {
            // TODO consider creating a holder object rather than the obfuscation that is params.
            Long cursistId = (Long) params[0];
            Long eisId = (Long) params[1];
            Boolean behaald = (Boolean) params[2];

            // TODO make it actually save (or delete) the info.
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // TODO determine what needs to happen when this is finished. Especially in case of errors.
        }
    }

    public void showErrorMessage() {
        // TODO make error possible.
        // Maybe use seperate nested view for error?

/*        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showCursistListData() {
        recyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        */
    }

}
