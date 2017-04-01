package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

// TODO when deselecting a trainingseis remove it from the list.
public class TrainingActivity extends AppCompatActivity implements TrainingsListAdapter.TrainingListAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TrainingsListAdapter trainingsListAdapter;
    private Button volgendeButton;
    private ArrayList<DiplomaEis> selectedDiplomaEisList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Link the variables to the view items.
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        volgendeButton = (Button) findViewById(R.id.buttonVolgende);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        mRecyclerView.setHasFixedSize(true);
        trainingsListAdapter = new TrainingsListAdapter(this, this);
        mRecyclerView.setAdapter(trainingsListAdapter);


        // Get data
        loadCwoEisData();
    }

    /**
     * On click method voor volgende button.
     *
     * @param view
     */
    public void onClickShowVolgende(View view) {
        if (selectedDiplomaEisList.size() > 0) {
            showCursistBehaaldEisenActivity();
        }
    }

    private void showCursistBehaaldEisenActivity() {
        Context context = this;
        Class destinationClass = CursistBehaaldEisActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putParcelableArrayListExtra("selectedDiplomaEisList", selectedDiplomaEisList);

        startActivity(intent);
    }

    private void loadCwoEisData() {
        new FetchCwoEisData().execute();
    }

    @Override
    public void onClick(DiplomaEis diplomaEis, boolean selected) {
        if (selected == true) {
            selectedDiplomaEisList.add(diplomaEis);
        } else if (selectedDiplomaEisList.contains(diplomaEis)) {
            selectedDiplomaEisList.remove(diplomaEis);
        }
        toggleVolgendeButton();
    }

    @Override
    public boolean isSelectedDiplomaEis(DiplomaEis eis) {
        return selectedDiplomaEisList.contains(eis);
    }

    private void toggleVolgendeButton() {
        // TODO make this work. So far it doesnt' re-active the button.
        // Also add activated false back in xml.
        // maybe take out check for the onclickshow volgende.
        /*
        if(selectedDiplomaEisList.size() > 0 && volgendeButton.isActivated() == false) {
            volgendeButton.setActivated(true);
        } else if(selectedDiplomaEisList.size() == 0){
            volgendeButton.setActivated(false);
        }*/
    }

    class FetchCwoEisData extends AsyncTask<String, Void, List<Diploma>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.GONE);
            if (diplomaList != null) {
                List<DiplomaEis> diplomaEisenLijst = new ArrayList<>();
                for (int i = 0; i < diplomaList.size(); i++) {
                    diplomaEisenLijst.addAll(diplomaList.get(i).getDiplomaEis());
                }

                trainingsListAdapter.setCwoData(diplomaEisenLijst);
            } else {
                // TODO hide volgende button.
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
        mErrorMessageDisplay.setVisibility(View.GONE);
    }
}
