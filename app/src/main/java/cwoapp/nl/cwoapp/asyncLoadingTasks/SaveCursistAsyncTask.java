package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

/**
 * Created by sonja on 3/31/2017.
 * Saves single cursist info to server.
 */

public class SaveCursistAsyncTask extends AsyncTask<Cursist, Void, Integer> {
    private SaveCursist saveCursist;

    public SaveCursistAsyncTask(SaveCursist saveCursist) {
        this.saveCursist = saveCursist;
    }

    @Override
    protected Integer doInBackground(Cursist... cursists) {

        URL url = NetworkUtils.buildUrl("cursist");

        try {
            return NetworkUtils.uploadToServer(url, cursists[0].simpleCursistToJson(), "PUT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);
        saveCursist.cursistSaved(s);
    }

    public interface SaveCursist {
        void cursistSaved(int resultCode);
    }
}
