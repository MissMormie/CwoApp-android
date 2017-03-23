package cwoapp.nl.cwoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class DiplomaUitgevenActivity extends AppCompatActivity {
    ListView diplomaUitgevenListView;
    ArrayAdapter adapter;
    List<Diploma> diplomaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diploma_uitgeven);


        diplomaUitgevenListView = (ListView) findViewById(R.id.diplomasListView);
        loadDiplomaData();


/*
        URL diplomaListUrl = NetworkUtils.buildUrl("diplomas");

        try {
            String jsonDiplomaLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
            List<Diploma> diplomaList = OpenJsonUtils.getDiplomaLijst(jsonDiplomaLijstResponse);
            diplomaList.size();
        } catch (Exception e) {

        }
*/
    }

    private void fillDiplomaListView() {
        String[] diplomaArray = new String[3];
        for (int i = 0; i < 3; i++) {
            diplomaArray[i] = diplomaList.get(i).toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.diploma_list_item, R.id.diplomaTextView, diplomaArray);
        diplomaUitgevenListView.setAdapter(adapter);
    }

    private void loadDiplomaData() {
        new FetchDiplomaEisData().execute();
    }


    class FetchDiplomaEisData extends AsyncTask<String, Void, List<Diploma>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Diploma> doInBackground(String... params) {
            URL diplomaListUrl = NetworkUtils.buildUrl("diplomas");

            try {
                String jsonDiplomaLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
                List<Diploma> diplomaList = OpenJsonUtils.getDiplomaLijst(jsonDiplomaLijstResponse);
                return diplomaList;

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Cause" + e.getCause());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                int a = 1;
                System.out.println("Cause" + e.getCause());
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Diploma> diplomaListResult) {
            //           mLoadingIndicator.setVisibility(View.GONE);
            if (diplomaList != null) {
                diplomaList = diplomaListResult;
                fillDiplomaListView();


            } else {
                // TODO hide volgende button.
                //               showErrorMessage();
            }
        }
    }
}
