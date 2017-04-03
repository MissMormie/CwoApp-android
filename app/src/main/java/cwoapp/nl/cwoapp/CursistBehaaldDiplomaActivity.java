package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.DownloadAndSetImageTask;
import cwoapp.nl.cwoapp.asyncLoadingTasks.FetchCursistListAsyncTask;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

public class CursistBehaaldDiplomaActivity extends AppCompatActivity implements FetchCursistListAsyncTask.FetchCursistList {
    List<Diploma> diplomaList;
    private ProgressBar loadingIndicator;
    private List<Cursist> cursistList;
    private TextView textViewNaam;
    private RecyclerView recyclerView;
    private ImageView imageViewFoto;
    private CursistBehaaldDiplomaAdapter cursistBehaaldDiplomaAdapter;
    private Cursist currentCursist;
    Boolean showAlreadyCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get activity xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_checklist);

        // Get parceled info
        Intent intent = getIntent();
        diplomaList = intent.getParcelableArrayListExtra("selectedDiplomaList");
        if (intent.hasExtra("cursist")) {
            cursistList = new ArrayList<>();
            cursistList.add((Cursist) intent.getExtras().getParcelable("cursist"));
        }

        // get variables for elements in layout.
        textViewNaam = (TextView) findViewById(R.id.textViewNaam);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);
        imageViewFoto = (ImageView) findViewById(R.id.imageViewFoto);

        // Set up of the recycler view and adapter.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        recyclerView.setHasFixedSize(true);
        cursistBehaaldDiplomaAdapter = new CursistBehaaldDiplomaAdapter(diplomaList);
        recyclerView.setAdapter(cursistBehaaldDiplomaAdapter);

        // Get preference for showing cursisten who already met all eisen.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showAlreadyCompleted = sharedPreferences.getBoolean(getString(R.string.pref_show_already_completed_key),
                getResources().getBoolean(R.bool.pref_show_already_completed_default));


        loadCursistListData();
    }

    private void loadCursistListData() {
        // Check if info needs to be loaded, if not go straight to showing it.
        if (cursistList == null)
            new FetchCursistListAsyncTask(this).execute(showAlreadyCompleted);
        else {
            loadingIndicator.setVisibility(View.GONE);
            showNextCursist();
        }
    }


    private void showNextCursist() {
        if (cursistList.size() == 0) {
            backToMainActivity();
        } else {
            currentCursist = cursistList.remove(0);
            // Als deze cursist alle eisen behaald heeft en deze preference is aangegeven, sla deze cursist dan over.
            if (currentCursist.isAlleDiplomasBehaald(diplomaList)) {
                showNextCursist();
            } else {
                cursistBehaaldDiplomaAdapter.setCursist(currentCursist);
                textViewNaam.setText(currentCursist.nameToString());

                // Set photo if available, else set user mockup.
                if (currentCursist.getCursistFoto() != null) {
                    URL fotoUrl = NetworkUtils.buildUrl("foto", currentCursist.getCursistFoto().getId().toString());
                    new DownloadAndSetImageTask(imageViewFoto, getApplicationContext())
                            .execute(fotoUrl.toString());
                } else {
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_user_image);
                    imageViewFoto.setImageDrawable(drawable);
                }

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
    public void setCursistList(List<Cursist> cursistList) {
        if (cursistList == null) {
            showErrorMessage();
            return;
        }

        loadingIndicator.setVisibility(View.GONE);
        this.cursistList = cursistList;
        showNextCursist();
    }

    void showErrorMessage() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_LONG);
        toast.show();
    }
}
