package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.CwoEis;
import cwoapp.nl.cwoapp.utility.MockEntityGenerator;

public class TrainingActivity extends AppCompatActivity implements TrainingsListAdapter.TrainingListAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TrainingsListAdapter trainingsListAdapter;
    private Button volgendeButton;
    private ArrayList<CwoEis> selectedCwoEisList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Link the variables to the view items.
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        volgendeButton = (Button) findViewById(R.id.button_volgende);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        mRecyclerView.setHasFixedSize(true);
        trainingsListAdapter = new TrainingsListAdapter(this);
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
        Log.d("blaat", "onClickShowVOlgende called");
        if (selectedCwoEisList.size() > 0) {
            showCursistBehaaldEisenActivity();
        }
    }

    private void showCursistBehaaldEisenActivity() {
        Context context = this;
        Class destinationClass = CursistBehaaldEisActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putParcelableArrayListExtra("selectedCwoEisList", selectedCwoEisList);
        // TODo put parcelable object.
        startActivity(intent);

    }

    private void loadCwoEisData() {
        new FetchCwoEisData().execute();
    }

    @Override
    public void onClick(CwoEis cwoEis, boolean selected) {
        if (selected == true) {
            selectedCwoEisList.add(cwoEis);
        } else if (selectedCwoEisList.contains(cwoEis)) {
            selectedCwoEisList.remove(cwoEis);
        }
        toggleVolgendeButton();
    }

    private void toggleVolgendeButton() {
        // TODO make this work. So far it doesnt' re-active the button.
        // Also add activated false back in xml.
        // take out check for the onclickshow volgende.
        /*
        if(selectedCwoEisList.size() > 0 && volgendeButton.isActivated() == false) {
            volgendeButton.setActivated(true);
        } else if(selectedCwoEisList.size() == 0){
            volgendeButton.setActivated(false);
        }*/
    }

    class FetchCwoEisData extends AsyncTask<String, Void, List<CwoEis>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CwoEis> doInBackground(String... params) {

            List<CwoEis> cwoEisenList = MockEntityGenerator.createCwoEisenList(15);

            return cwoEisenList;
        }

        @Override
        protected void onPostExecute(List<CwoEis> cwoEisenList) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (cwoEisenList != null) {
                trainingsListAdapter.setCwoData(cwoEisenList);
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
        mErrorMessageDisplay.setVisibility(View.GONE);
    }
}
