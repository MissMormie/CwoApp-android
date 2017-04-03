package cwoapp.nl.cwoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cwoapp.nl.cwoapp.asyncLoadingTasks.SaveCursistAsyncTask;
import cwoapp.nl.cwoapp.entity.Cursist;

public class EditCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener, SaveCursistAsyncTask.SaveCursist {
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
        new SaveCursistAsyncTask(this).execute(cursist);
    }

    @Override
    public void cursistSaved(Cursist cursist) {
        if (cursist != null) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.cursist_opgeslagen), Toast.LENGTH_SHORT);
            toast.show();
            // TODO set a check that getCursistFoto excist, create it otherwise.
            cursist.getCursistFoto().setImage(this.cursist.getFotoFileBase64());
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
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cursist", cursist);
        setResult(RESULT_CANCELED, intent);
    }
}
