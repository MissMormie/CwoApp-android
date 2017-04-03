package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;
import cwoapp.nl.cwoapp.utility.OpenJsonUtils;

public class CursistListActivity extends AppCompatActivity implements CursistListAdapater.CursistListAdapterOnClickHandler {
    private static final String TAG = CursistListActivity.class.getSimpleName();

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private CursistListAdapater cursistListAdapater;
    private MenuItem searchItem;
    static final int CURSIST_DETAIL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursist_list);

        /*
         * Get references to the elements in the layout we need.
         * */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_cursist_lijst);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        // All items in list have the same size
        mRecyclerView.setHasFixedSize(true);
        cursistListAdapater = new CursistListAdapater(this, this);
        mRecyclerView.setAdapter(cursistListAdapater);

        loadCursistListData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cursist_lijst_menu, menu);
        //searchItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        /*if (itemThatWasClickedId == R.id.action_search) {
            // TODO do something searchy ;)
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }*/
    }

    private void loadCursistListData() {
        new FetchCursistListTask().execute();
    }

    @Override
    public void onClick(Cursist cursist) {
        Context context = this;
        Class destinationClass = CursistDetailActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("cursistId", cursist.id);
        intent.putExtra("cursist", cursist);
        startActivityForResult(intent, CURSIST_DETAIL);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CURSIST_DETAIL)

            if (resultCode == RESULT_OK)
                cursistListAdapater.updateCursistInList((Cursist) data.getExtras().getParcelable("cursist"));
                // update cursist
            else if (resultCode == RESULT_CANCELED) {
                if (data.hasExtra("cursist")) {
                    Cursist cursist = data.getExtras().getParcelable("cursist");
                    cursistListAdapater.deleteCursistFromList(cursist);
                }
            }

//       super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO at the moment when you come back here after editing the cursist, all info is downloaded again, should be able to pass this back. -- is it?
    @Override
    public void onRestart() {
        super.onRestart();
    }


    private class FetchCursistListTask extends AsyncTask<String, Void, List<Cursist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Cursist> doInBackground(String... params) {
            URL diplomaListUrl = NetworkUtils.buildUrl("cursist", "lijst");

            try {
                String jsonCursistLijstResponse = NetworkUtils.getResponseFromHttpUrl(diplomaListUrl);
                return OpenJsonUtils.getCursistLijst(jsonCursistLijstResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Cursist> cursistList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (cursistList != null) {
                cursistListAdapater.setCursistListData(cursistList);
            } else {
                showErrorMessage();
            }
        }
    }

    public void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showCursistListData() {
        /* First, show the cursisten data */
        mRecyclerView.setVisibility(View.VISIBLE);
        /* Then, hide the error */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

}
