package cwoapp.nl.cwoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.MockEntityGenerator;

public class EditCursistActivity extends AppCompatActivity implements CursistFormFragment.OnFragmentInteractionListener {
    Cursist cursist;
    CursistFormFragment cursistFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cursist);
        cursist = MockEntityGenerator.createSimpleCursist(1);
        cursistFormFragment = (CursistFormFragment) getSupportFragmentManager().findFragmentById(R.id.cursist_form_fragment);
        cursistFormFragment.setCursist(cursist);
    }

    @Override
    public void saveCursist(Cursist cursist) {
        System.out.println(cursist.toString());
        // TODO
    }

}
