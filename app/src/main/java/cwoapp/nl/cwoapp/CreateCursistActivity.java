package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.asyncLoadingTasks.FetchDiplomaAsyncTask;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CreateCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener, FetchDiplomaAsyncTask.FetchDiploma {
    private CursistFormFragment cursistFormFragment;
    private Cursist cursist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_cursist);
        cursistFormFragment = (CursistFormFragment) getSupportFragmentManager().findFragmentById(R.id.cursist_form_fragment);
        Cursist cursist = new Cursist();
        cursistFormFragment.setCursist(cursist);
    }


    @Override
    public void saveCursist(Cursist cursist) {

        new SaveCursistAsyncTask().execute(cursist);
    }


    private void cursistSaved(Cursist cursist) {
        this.cursist = cursist;
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_opgeslagen), Toast.LENGTH_SHORT);
        toast.show();

        // Get preference for showing diploma's after creation of Cursist.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showDiploma = sharedPreferences.getBoolean(getString(R.string.pref_show_diploma_after_create_key),
                getResources().getBoolean(R.bool.pref_show_diploma_after_create_cursist));
        if (showDiploma) {
            new FetchDiplomaAsyncTask(this).execute();
        } else {
            finish();
        }
    }

    private void saveFailed() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT);
        toast.show();
    }

    // Used when cursist is saved to call setDiplomaBehaald.
    @Override
    public void setDiploma(List<Diploma> diplomaList) {
        ArrayList<Diploma> diplomaArrayList = (ArrayList<Diploma>) diplomaList;
        Context context = this;
        Class destinationClass = CursistBehaaldDiplomaActivity.class;
        Intent intent = new Intent(context, destinationClass);


        intent.putParcelableArrayListExtra("selectedDiplomaList", diplomaArrayList);

        intent.putExtra("cursist", cursist);

        startActivity(intent);

    }

    private class SaveCursistAsyncTask extends AsyncTask<Cursist, Void, Cursist> {


        @Override
        protected Cursist doInBackground(Cursist... cursist) {

            URL url = NetworkUtils.buildUrl("cursist");
            try {
                String cursistString = NetworkUtils.sendAndReceiveString(url, cursist[0].simpleCursistToJson(), "POST");
                if (cursistString == null || cursistString.equals(""))
                    return null;
                return OpenJsonUtils.getCursist(cursistString);
//                return NetworkUtils.uploadToServer(url, cursist[0].simpleCursistToJson(), "POST");
            } catch (Exception e) {
                // check errors
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursist cursist) {
            if (cursist != null) {
                cursistSaved(cursist);
            } else {
                saveFailed();
            }


        }
    }

}
