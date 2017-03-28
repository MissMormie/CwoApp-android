package cwoapp.nl.cwoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

public class CreateCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener {
    CursistFormFragment cursistFormFragment;

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
        String cursistJson = cursist.simpleCursistToJson();
        new SaveCursistAsyncTask().execute(cursist);
    }


    void cursistSaved() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_opgeslagen), Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    class SaveCursistAsyncTask extends AsyncTask<Cursist, Void, Integer> {

        @Override
        protected Integer doInBackground(Cursist... cursist) {

            URL url = NetworkUtils.buildUrl("cursist");
            try {
                return NetworkUtils.uploadToServer(url, cursist[0].simpleCursistToJson(), "POST");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            cursistSaved();
            super.onPostExecute(s);

        }
    }

}
