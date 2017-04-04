package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.FetchCursistListAsyncTask;
import cwoapp.nl.cwoapp.asyncLoadingTasks.SaveCursistAsyncTask;
import cwoapp.nl.cwoapp.asyncLoadingTasks.SaveDiplomaBehaaldAsyncTask;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistHeeftDiploma;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;

public class CursistenBehalenDiplomaActivity extends AppCompatActivity
        implements
            FetchCursistListAsyncTask.FetchCursistList,
            SaveDiplomaBehaaldAsyncTask.SaveDiplomaBehaaldResult,
            SaveCursistAsyncTask.SaveCursist {



    // UI elements:
    private ProgressBar loadingIndicator;
    private CheckBox diplomaCheckbox;
    private CheckBox paspoortCheckBox;

    // Data
    private Diploma diploma;
    private Cursist currentCursist;
    private List<Cursist> cursistList;

    private CursistBehaaldEisAdapter cursistBehaaldEisAdapter;
    private Boolean showAlreadyCompleted;
    private boolean saveData = true; // OnChangeChecklistener saves data when checkbox is clicked, but also when data is refeshed. Using to as workaround.


    // Fragment
    private CursistHeaderFragment cursistHeaderFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursisten_behalen_diploma);

        // Get parceled info
        Intent intent = getIntent();
        diploma = intent.getExtras().getParcelable("diploma");
        List<DiplomaEis> diplomaEisList = intent.getExtras().getParcelableArrayList("selectedDiplomaEisList");

        // Get UI elements
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        diplomaCheckbox = (CheckBox) findViewById(R.id.diplomaCheckbox);
        paspoortCheckBox = (CheckBox) findViewById(R.id.paspoortCheckbox);

        // Set Recyclerview adapter for diplomaEisen
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        cursistBehaaldEisAdapter = new CursistBehaaldEisAdapter();
        cursistBehaaldEisAdapter.setCwoListData(diplomaEisList);
        recyclerView.setAdapter(cursistBehaaldEisAdapter);

        // fragments
        cursistHeaderFragment = (CursistHeaderFragment) getSupportFragmentManager().findFragmentById(R.id.cursist_header_fragment);

        // Set diploma tekst eenmalig.
        diplomaCheckbox.setText(diploma.toString());

        // Get preference for showing cursisten who already met all eisen.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showAlreadyCompleted = sharedPreferences.getBoolean(getString(R.string.pref_show_already_completed_key),
                getResources().getBoolean(R.bool.pref_show_already_completed_default));


        loadCursistListData();
        setListeners();

    }

    private void setListeners() {
        // TODO must be smarter way to access this in inner class.
        final CursistenBehalenDiplomaActivity thingie = this;
        diplomaCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!saveData)
                    return;

                CursistHeeftDiploma cursistHeeftDiploma = new CursistHeeftDiploma(currentCursist.id, diploma, isChecked);
                new SaveDiplomaBehaaldAsyncTask(thingie).execute(cursistHeeftDiploma);
            }
        });

        paspoortCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!saveData)
                    return;

                currentCursist.heeftPaspoort(isChecked);
                new SaveCursistAsyncTask(thingie).execute(currentCursist);

            }
        });
    }

    private void loadCursistListData() {
        new FetchCursistListAsyncTask(this).execute(false);
    }

    @Override
    public void setCursistList(List<Cursist> cursistList) {
        if (cursistList == null) {
            showErrorMessage();
            return;
        }

        loadingIndicator.setVisibility(View.GONE);
        this.cursistList = cursistList;
        showNextCursist();
    }

    private void showNextCursist() {
        if (cursistList.size() == 0) {
            backToMainActivity();
        } else {
            currentCursist = cursistList.remove(0);
            // Als deze cursist alle eisen behaald heeft en deze preference is aangegeven, sla deze cursist dan over.
            if (currentCursist.hasDiploma(diploma.getId()) && !showAlreadyCompleted) {
                showNextCursist();
            } else {
                setCursistData(currentCursist);

            }
        }
    }

    private void setCursistData(Cursist cursist) {
        saveData = false;
        // Set checkbox
        if(cursist.paspoort != null) {
            paspoortCheckBox.setChecked(true);
        } else {
            paspoortCheckBox.setChecked(false);
        }

        if(cursist.hasDiploma(diploma.getId())) {
            diplomaCheckbox.setChecked(true);
        } else {
            diplomaCheckbox.setChecked(false);
        }
        saveData = true;

        cursistBehaaldEisAdapter.setCursist(cursist);
        cursistHeaderFragment.setCursist(cursist);
    }


    private void backToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showErrorMessage() {
        // TODO
    }

    public void onClickShowVolgendeCursist(View view) {
        showNextCursist();
    }

    @Override
    public void diplomaSaved(boolean success) {
        if(!success)
            showErrorMessage();
    }

    @Override
    public void cursistSaved(Cursist cursist) {
        if(cursist == null)
            showErrorMessage();
    }
}
