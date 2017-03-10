package cwoapp.nl.cwoapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

public class CursistListActivity extends AppCompatActivity implements CursistListAdapater.CursistListAdapterOnClickHandler {
    private static final String TAG = CursistListActivity.class.getSimpleName();

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private CursistListAdapater cursistListAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_list);

        /*
         * Get references to the elements in the layout we need.
         * */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_cursist_lijst);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        // All items in list have the same size
        mRecyclerView.setHasFixedSize(true);
        cursistListAdapater = new CursistListAdapater(this);
        mRecyclerView.setAdapter(cursistListAdapater);

        loadCursistListData();
    }

    private void loadCursistListData() {
        new FetchCursistListTask().execute();
    }

    @Override
    public void onClick(Cursist cursist) {
        System.out.println("onclick " + TAG);
        Context context = this;
        Toast.makeText(context, cursist.nameToString(), Toast.LENGTH_SHORT).show();
    }


    public class FetchCursistListTask extends AsyncTask<String, Void, List<Cursist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Cursist> doInBackground(String... params) {

            List<Cursist> cursistList = new ArrayList<>();
            for(int i = 0; i < 30; i++) {
                Cursist cursist = new Cursist("sonja" + i, "", "duijvesteijn", "foto");
                cursistList.add(cursist);
            }
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
        protected void onPostExecute(List<Cursist> cursistList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (cursistList!= null) {
                cursistListAdapater.setCursistListData(cursistList);
            } else {
                showErrorMessage();
            }
        }
    }

    public void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showCursistListData() {
        /* First, show the cursisten data */
        mRecyclerView.setVisibility(View.VISIBLE);
        /* Then, hide the error */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }
}
