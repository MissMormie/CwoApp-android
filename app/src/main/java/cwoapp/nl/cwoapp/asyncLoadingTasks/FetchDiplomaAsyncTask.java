package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

/**
 * Created by sonja on 3/31/2017.
 * Gets list of diploma's and underlying diplomaEisen async.
 */

public class FetchDiplomaAsyncTask extends AsyncTask<String, Void, List<Diploma>> {
    final private FetchDiploma fetchDiploma;

    public FetchDiplomaAsyncTask(FetchDiploma fetchDiploma) {
        this.fetchDiploma = fetchDiploma;
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
        fetchDiploma.setDiploma(diplomaList);
    }

    public interface FetchDiploma {
        void setDiploma(List<Diploma> diplomaList);
    }

}
