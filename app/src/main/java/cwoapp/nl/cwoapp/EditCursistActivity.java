package cwoapp.nl.cwoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

public class EditCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener {
    Cursist cursist;
    CursistFormFragment cursistFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cursist);
        cursist = getIntent().getExtras().getParcelable("cursist");
        cursistFormFragment = (CursistFormFragment) getSupportFragmentManager().findFragmentById(R.id.cursist_form_fragment);
        cursistFormFragment.setCursist(cursist);
    }

    @Override
    public void saveCursist(Cursist cursist) {
        this.cursist = cursist;
        String cursistJson = cursist.simpleCursistToJson();
        new saveCursistAsyncTask().execute(cursist);
    }

    void cursistSaved() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_opgeslagen), Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    class saveCursistAsyncTask extends AsyncTask<Cursist, Void, Integer> {

        @Override
        protected Integer doInBackground(Cursist... params) {

            URL url = NetworkUtils.buildUrl("cursist");
            try {
                return NetworkUtils.uploadToServer(url, cursist.simpleCursistToJson(), "PUT");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            cursistSaved();

        }
    }
}
