package cwoapp.nl.cwoapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.DownloadAndSetImageTask;
import cwoapp.nl.cwoapp.asyncLoadingTasks.SaveCursistAsyncTask;
import cwoapp.nl.cwoapp.databinding.ActivityCursistDetailBinding;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CursistDetailActivity extends AppCompatActivity implements SaveCursistAsyncTask.SaveCursist {

    ActivityCursistDetailBinding activityCursistDetailBinding;
    Cursist cursist;
    private RecyclerView recyclerView;
    private CursistBehaaldEisAdapter cursistBehaaldEisAdapter;
    static final int EDIT_CURSIST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCursistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_cursist_detail);

        // Set up of the recycler view and adapter.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_training_lijst);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // Not all items in list have the same size
        recyclerView.setHasFixedSize(true);
        cursistBehaaldEisAdapter = new CursistBehaaldEisAdapter();
        recyclerView.setAdapter(cursistBehaaldEisAdapter);

        cursist = getIntent().getExtras().getParcelable("cursist");
        displayCursistInfo();
        loadDiplomaData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cursist_detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_edit) {
            showEditCursist();
            return true;
        } else if (itemThatWasClickedId == R.id.action_verbergen) {
            hideCursist();
            return true;
        } else if (itemThatWasClickedId == R.id.action_delete) {
            deleteCursist();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideCursist() {
        toggleLoading(true);
        cursist.toggleVerborgen();
        new SaveCursistAsyncTask(this).execute(cursist);

        // TODO
    }

    private void deleteCursist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.echt_verwijderen)
                .setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteCursistTask().execute();
                    }
                })
                .setNegativeButton(R.string.nee, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                    // Create the AlertDialog object and return it
                });

        Dialog dialog = builder.create();
        dialog.show();


    }

    private void cursistDeleted() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_verwijderd), Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent();
        intent.putExtra("cursist", cursist);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cursist", cursist);
        setResult(RESULT_OK, intent);
    }

    public void toggleLoading(boolean currentlyLoading) {
        if (activityCursistDetailBinding.loadingProgressBar == null)
            return;
        if (currentlyLoading == true)
            activityCursistDetailBinding.loadingProgressBar.setVisibility(View.VISIBLE);
        else
            activityCursistDetailBinding.loadingProgressBar.setVisibility(View.INVISIBLE);

    }

    private void showEditCursist() {
        Context context = this;
        Class destinationClass = EditCursistActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("cursist", cursist);
        startActivityForResult(intent, EDIT_CURSIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CURSIST)
            if (resultCode == RESULT_OK && data.hasExtra("cursist")) {
                this.cursist = data.getExtras().getParcelable("cursist");
                displayCursistInfo();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadDiplomaData() {
        //new FetchCursistTask().execute(cursistId);
        new FetchCwoEisData().execute();
    }

    private void displayCursistInfo() {
        if (cursist == null)
            return;

        activityCursistDetailBinding.textviewNaam.setText(cursist.nameToString());
        activityCursistDetailBinding.textViewOpmerking.setText(cursist.opmerking);
        if (cursist.paspoort == null)
            activityCursistDetailBinding.textViewPaspoort.setText("nee");
        else
            activityCursistDetailBinding.textViewPaspoort.setText("ja");

        if (cursist.getCursistFoto() != null) {
            // Check if photo is included in cursist object
            if (cursist.getCursistFoto().getImage() != null && !cursist.getCursistFoto().getImage().equals("")) {
                String imgData = cursist.getCursistFoto().getThumbnail();
                byte[] imgByteArray = Base64.decode(imgData, Base64.NO_WRAP);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                activityCursistDetailBinding.imageViewFoto.setImageBitmap(bitmap);
            } else {

                URL fotoUrl = NetworkUtils.buildUrl("foto", cursist.getCursistFoto().getId().toString());
                new DownloadAndSetImageTask(activityCursistDetailBinding.imageViewFoto, getApplicationContext())
                        .execute(fotoUrl.toString());
            }
        }

//            activityCursistDetailBinding.imageViewFoto.setImageBitmap();
        // Pass information to adapter for eisen met.
        cursistBehaaldEisAdapter.setCursist(cursist);
    }

    private void displayDiplomaEisInfo(List<DiplomaEis> diplomaEisList) {
        cursistBehaaldEisAdapter.setCwoListData(diplomaEisList);

    }

    @Override
    public void cursistSaved(Cursist cursist) {
        toggleLoading(false);
        if (cursist != null) {
            String tekst;
            if (cursist.isVerborgen())
                tekst = getString(R.string.cursist_verborgen);
            else
                tekst = getString(R.string.cursist_niet_verborgen);

            Toast toast = Toast.makeText(getApplicationContext(), tekst, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            showErrorMessage();
        }
    }

    void showErrorMessage() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_LONG);
        toast.show();
    }


    private class DeleteCursistTask extends AsyncTask<Long, Void, Integer> {

        @Override
        protected void onPreExecute() {
            toggleLoading(true);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Long... params) {
            URL url = NetworkUtils.buildUrl("cursist", cursist.id.toString());
            int resultCode = 0;
            try {
                resultCode = NetworkUtils.sendToServer(url, "DELETE");
                return resultCode;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode) {
            toggleLoading(false);
            if (resultCode == HttpURLConnection.HTTP_OK) {
                cursistDeleted();
            } else {
                showErrorMessage();
            }
        }
    }

    private class FetchCwoEisData extends AsyncTask<String, Void, List<Diploma>> {

        @Override
        protected void onPreExecute() {
            toggleLoading(true);
            super.onPreExecute();
        }

        @Override
        protected List<Diploma> doInBackground(String... params) {
            URL diplomaListUrl = NetworkUtils.buildUrl("diplomas");

            try {
                String jsonDiplomaLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
                return OpenJsonUtils.getDiplomaLijst(jsonDiplomaLijstResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Diploma> diplomaList) {
            toggleLoading(false);
            if (diplomaList != null) {
                List<DiplomaEis> diplomaEisenLijst = new ArrayList<>();
                for (int i = 0; i < diplomaList.size(); i++) {
                    diplomaEisenLijst.addAll(diplomaList.get(i).getDiplomaEis());
                }

                displayDiplomaEisInfo(diplomaEisenLijst);
            } else {
                showErrorMessage();
            }
        }
    }

}
