package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.os.AsyncTask;

import java.net.URL;

import cwoapp.nl.cwoapp.entity.CursistHeeftDiploma;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

/**
 * Created by sonja on 4/4/2017.
 * SaveDiplomaBehaaldAsyncTask
 */

public class SaveDiplomaBehaaldAsyncTask extends AsyncTask<CursistHeeftDiploma, Void, Boolean> {
    private final SaveDiplomaBehaaldResult saveDiplomaBehaaldResult;

    public SaveDiplomaBehaaldAsyncTask(SaveDiplomaBehaaldResult saveDiplomaBehaaldResult) {
        this.saveDiplomaBehaaldResult = saveDiplomaBehaaldResult;
    }

    @Override
    protected Boolean doInBackground(CursistHeeftDiploma... params) {
        CursistHeeftDiploma cursistHeeftDiploma = params[0];
        if (cursistHeeftDiploma.isBehaald())
            return saveCursistHeeftDiploma(cursistHeeftDiploma, "POST");
        else
            return saveCursistHeeftDiploma(cursistHeeftDiploma, "DELETE");

    }

    private boolean saveCursistHeeftDiploma(CursistHeeftDiploma cursistHeeftDiploma, String action) {

        URL url = NetworkUtils.buildUrl("cursistHeeftDiploma");
        //String json = "{\"diplomaId\": 1, \"cursistId\": 1}";
        String json = cursistHeeftDiploma.toJson();
        try {
            int resultCode = NetworkUtils.uploadToServer(url, json, action);
            if (resultCode == 200)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    @Override
    protected void onPostExecute(Boolean success) {
        saveDiplomaBehaaldResult.diplomaSaved(success);
    }

    public interface SaveDiplomaBehaaldResult {
        void diplomaSaved(boolean success);

    }
}
