package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import cwoapp.nl.cwoapp.asyncLoadingTasks.SaveCursistAsyncTask;
import cwoapp.nl.cwoapp.entity.Cursist;

public class EditCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener, SaveCursistAsyncTask.SaveCursist {
    private Cursist cursist;
    private CursistFormFragment cursistFormFragment;

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
        new SaveCursistAsyncTask(this).execute(cursist);
    }

    @Override
    public void cursistSaved(Cursist cursist) {
        if (cursist != null) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_opgeslagen), Toast.LENGTH_SHORT);
            toast.show();
            if(cursist.getCursistFoto()!= null)
                cursist.getCursistFoto().setImage(this.cursist.getFotoFileBase64());
            // TODO, fix this work around.
            // For some reason the api returns a different date, it's saved correctly
            cursist.paspoort = this.cursist.paspoort;
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent();
        intent.putExtra("cursist", cursist);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cursist", cursist);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
