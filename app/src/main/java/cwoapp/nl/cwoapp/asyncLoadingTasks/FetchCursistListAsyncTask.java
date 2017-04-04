package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

/**
 * Created by sonja on 4/3/2017.
 * fetches list of cursisten.
 */

public class FetchCursistListAsyncTask extends AsyncTask<Boolean, Void, List<Cursist>> {
    final private FetchCursistList responseClass;

    public FetchCursistListAsyncTask(FetchCursistList responseClass) {
        this.responseClass = responseClass;
    }

    /**
     * verborgen[0] = true also gives results that are hidden.
     */
    @Override
    protected List<Cursist> doInBackground(Boolean... verborgen) {
        URL diplomaListUrl;
        if (verborgen[0]) {
            diplomaListUrl = NetworkUtils.buildUrl("cursist", "lijst");
        } else {
            diplomaListUrl = NetworkUtils.buildUrl("cursist", "lijst", "verborgen", "false");
        }

        try {
            String jsonCursistLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
            return OpenJsonUtils.getCursistLijst(jsonCursistLijstResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onPostExecute(List<Cursist> cursistenList) {
        responseClass.setCursistList(cursistenList);
    }

    public interface FetchCursistList {
        void setCursistList(List<Cursist> cursistList);
    }
}
