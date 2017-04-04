package cwoapp.nl.cwoapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

import cwoapp.nl.cwoapp.asyncLoadingTasks.DownloadAndSetImageTask;
import cwoapp.nl.cwoapp.databinding.FragmentCursistHeaderBinding;
import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.NetworkUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class CursistHeaderFragment extends Fragment {
    private Cursist cursist;
    private FragmentCursistHeaderBinding databinding;


    public CursistHeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CursistHeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CursistHeaderFragment newInstance() {

        return new CursistHeaderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cursist_header, container, false);
        return databinding.getRoot(); // View
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setCursist(Cursist cursist) {
        this.cursist = cursist;


        if (cursist == null)
            return;
        databinding.textViewNaam.setText(cursist.nameToString());
        databinding.textViewOpmerking.setText(cursist.opmerking);
        if (cursist.paspoort == null)
            databinding.textViewPaspoort.setText(getString(R.string.paspoort) +": " + getString(R.string.nee));
        else
            databinding.textViewPaspoort.setText(getString(R.string.paspoort) +": " + getString(R.string.ja));

        if (cursist.getCursistFoto() != null && cursist.getCursistFoto().getThumbnail() != null && !cursist.getCursistFoto().getThumbnail().equals("")) {
            // Check if photo is included in cursist object
            if (cursist.getCursistFoto().getImage() != null && !cursist.getCursistFoto().getImage().equals("")) {
                String imgData = cursist.getCursistFoto().getThumbnail();
                byte[] imgByteArray = Base64.decode(imgData, Base64.NO_WRAP);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                databinding.imageViewFoto.setImageBitmap(bitmap);
            } else {

                URL fotoUrl = NetworkUtils.buildUrl("foto", cursist.getCursistFoto().getId().toString());
                new DownloadAndSetImageTask(databinding.imageViewFoto, getContext())
                        .execute(fotoUrl.toString());
            }
        }
    }

}
