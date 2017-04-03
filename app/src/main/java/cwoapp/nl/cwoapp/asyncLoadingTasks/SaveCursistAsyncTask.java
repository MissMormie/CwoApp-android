package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.os.AsyncTask;

import java.net.URL;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

/**
 * Created by sonja on 3/31/2017.
 * Saves single cursist info to server.
 */

public class SaveCursistAsyncTask extends AsyncTask<Cursist, Void, Cursist> {
    private SaveCursist saveCursist;

    public SaveCursistAsyncTask(SaveCursist saveCursist) {
        this.saveCursist = saveCursist;
    }

    @Override
    protected Cursist doInBackground(Cursist... cursists) {

        URL url = NetworkUtils.buildUrl("cursist");

        try {
            String cursistString = NetworkUtils.sendAndReceiveString(url, cursists[0].simpleCursistToJson(), "PUT");
            if (cursistString != null && !cursistString.equals(""))
                return OpenJsonUtils.getCursist(cursistString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Cursist s) {
        super.onPostExecute(s);
        saveCursist.cursistSaved(s);
    }

    public interface SaveCursist {
        /**
         * returns null if there was an error or no result.
         */
        void cursistSaved(Cursist cursist);
    }
}
