package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.FetchDiplomaAsyncTask;
import cwoapp.nl.cwoapp.entity.Diploma;

public class DiplomaUitgevenActivity extends AppCompatActivity implements FetchDiplomaAsyncTask.FetchDiploma, DiplomaListAdapter.DiplomaListAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private DiplomaListAdapter diplomaListAdapter;
    private ArrayList<Diploma> selectedDiplomaList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diploma_uitgeven);

        // Link the variables to the view items.
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_diploma_lijst);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        mRecyclerView.setHasFixedSize(true);
        diplomaListAdapter = new DiplomaListAdapter(this);
        mRecyclerView.setAdapter(diplomaListAdapter);

        loadCwoEisData();
    }

// ---------------------------- Load data ----------------------------------------------------------

    private void loadCwoEisData() {
        new FetchDiplomaAsyncTask(this).execute();
    }


    @Override
    public void setDiploma(List<Diploma> diplomaList) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (diplomaList == null) {
            showError();
        } else {
            diplomaListAdapter.setDiplomaList(diplomaList);
        }
    }

    private void showError() {
        // TODO
    }

// ---------------------------- Click ----------------------------------------------------------

    public void onClickShowVolgende(View view) {
        if (selectedDiplomaList.size() > 0) {
            showCreateDiplomaActivity();
        }
    }

    private void showCreateDiplomaActivity() {
        Context context = this;
        Class destinationClass = CursistBehaaldDiplomaActivity.class;
        Intent intent = new Intent(context, destinationClass);

        // TODO make diploma parcelable.
        intent.putParcelableArrayListExtra("selectedDiplomaList", selectedDiplomaList);

        startActivity(intent);
    }

    // ----------------- DiplomaListAdapterOnClickHandler implementation ---------------------------

    @Override
    public void onClick(Diploma diploma, boolean selected) {
        if (selected && !selectedDiplomaList.contains(diploma)) {
            selectedDiplomaList.add(diploma);
        } else if (!selected && selectedDiplomaList.contains(diploma)) {
            selectedDiplomaList.remove(diploma);
        }
    }

    @Override
    public boolean isSelectedDiploma(Diploma diploma) {
        return selectedDiplomaList != null && selectedDiplomaList.contains(diploma);
    }
}

