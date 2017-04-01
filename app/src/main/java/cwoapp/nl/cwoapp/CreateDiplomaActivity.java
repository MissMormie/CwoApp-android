package cwoapp.nl.cwoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.FetchDiplomaAsyncTask;
import cwoapp.nl.cwoapp.entity.Diploma;

public class CreateDiplomaActivity extends AppCompatActivity implements FetchDiplomaAsyncTask.FetchDiploma {
    List<Diploma> diplomaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diploma);
        loadCwoEisData();
    }

    private void loadCwoEisData() {
        new FetchDiplomaAsyncTask(this).execute();
    }

    @Override
    public void setDiploma(List<Diploma> diplomaList) {
        this.diplomaList = diplomaList;
    }

}
