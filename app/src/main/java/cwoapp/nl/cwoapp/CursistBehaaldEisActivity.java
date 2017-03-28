package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.DiplomaEis;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CursistBehaaldEisActivity extends AppCompatActivity implements CursistBehaaldEisAdapter.CursistBehaaldEisAdapterOnClickHandler {
    // Lijst met diploma eisen die getraind zijn.
    private List<DiplomaEis> diplomaEisList;
    private ProgressBar loadingIndicator;
    private List<Cursist> cursistList;
    private TextView textViewNaam;
    private RecyclerView recyclerView;
    private CursistBehaaldEisAdapter cursistBehaaldEisAdapter;
    private Cursist currentCursist;
    Boolean showAlreadyCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_behaald_eis);
        Intent intent = getIntent();
        diplomaEisList = intent.getParcelableArrayListExtra("selectedDiplomaEisList");

        // get variables for elements in layout.
        textViewNaam = (TextView) findViewById(R.id.textViewNaam);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        recyclerView.setHasFixedSize(true);
        cursistBehaaldEisAdapter = new CursistBehaaldEisAdapter(this);
        recyclerView.setAdapter(cursistBehaaldEisAdapter);

        // Get preference for showing cursisten who already met all eisen.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showAlreadyCompleted = sharedPreferences.getBoolean(getString(R.string.pref_show_already_completed_key),
                getResources().getBoolean(R.bool.pref_show_already_completed_default));

        loadCursistListData();

    }

    private void loadCursistListData() {
        new FetchCursistListTask().execute();
    }

    private void showFirstCursist() {
        cursistBehaaldEisAdapter.setCwoListData(diplomaEisList);
        showNextCursist();
    }

    private void showNextCursist() {
        if (cursistList.size() == 0) {
            backToMainActivity();
        } else {
            currentCursist = cursistList.remove(0);
            // Als deze cursist alle eisen behaald heeft en deze preference is aangegeven, sla deze cursist dan over.
            if (currentCursist.isAlleEisenBehaald(diplomaEisList) && showAlreadyCompleted == false) {
                showNextCursist();
            } else {
                cursistBehaaldEisAdapter.setCursist(currentCursist);
                textViewNaam.setText(currentCursist.nameToString());
            }

        }
    }

    private void backToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onClickShowVolgendeCursist(View view) {
        showNextCursist();
    }


    @Override
    public void onClick(CursistBehaaldEis cursistBehaaldEis) {
        new SaveEisBehaaldTask().execute(cursistBehaaldEis);
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
            URL diplomaListUrl = NetworkUtils.buildUrl("cursist", "lijst");

            try {
                String jsonCursistLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
                List<Cursist> cursistList = OpenJsonUtils.getCursistLijst(jsonCursistLijstResponse);
                return cursistList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

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


    class SaveEisBehaaldTask extends AsyncTask<CursistBehaaldEis, Void, Boolean> {

        /**
         * params: Long cursist Id, Long cwoEis id, Boolean behaald.
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(CursistBehaaldEis... params) {
            CursistBehaaldEis cursistBehaaldEis = params[0];
            if (cursistBehaaldEis.isBehaald())
                return saveCursistBehaaldEis(cursistBehaaldEis, "POST");
            else
                return saveCursistBehaaldEis(cursistBehaaldEis, "DELETE");

        }

        private boolean saveCursistBehaaldEis(CursistBehaaldEis cursistBehaaldEis, String action) {

            URL url = NetworkUtils.buildUrl("cursistBehaaldEisen");
            //String json = "{\"eisId\": 1, \"cursistId\": 1}";
            String json = cursistBehaaldEis.toJson();
            try {
                int resultCode = NetworkUtils.uploadToServer(url, json, action);
                if (resultCode == 200)
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;

        }


        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                String error = getString(R.string.opslaan_mislukt);
                Toast.makeText(getApplication(), "" + error, Toast.LENGTH_SHORT).show();

            }
            // TODO determine what needs to happen when this is finished. Especially in case of errors.
            // Step 1 done, do i need to do something else?
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
