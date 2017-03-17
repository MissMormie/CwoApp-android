package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // TODO this function is only here to check if i can get the data from the shared preferences
    // and keep it as a reminder. Remove once it's in it's correct place.
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showAlreadyCompleted = sharedPreferences.getBoolean(getString(R.string.pref_show_already_completed_key),
                getResources().getBoolean(R.bool.pref_show_already_completed_default));
        System.out.println("Preference show already completed: " + showAlreadyCompleted);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCursistenLijst(View view) {
        Context context = this;
        Class destinationClass = CursistListActivity.class;
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);
    }

    public void onClickNieuweTraining(View view) {
        Context context = this;
        Class destinationClass = TrainingActivity.class;
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);

    }

    public void onClickNieuweCursist(View view) {
        Context context = this;
        Class destinationClass = CursistFormActivity.class;
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);
    }
}
