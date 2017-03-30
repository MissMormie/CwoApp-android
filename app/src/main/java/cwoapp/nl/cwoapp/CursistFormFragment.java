package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.utility.DateUtil;

import static android.app.Activity.RESULT_OK;
import static cwoapp.nl.cwoapp.R.id.imageViewFoto;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CursistFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CursistFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CursistFormFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Cursist cursist;

    // UI elements.
    private EditText voornaamEditText;
    private EditText tussenvoegselEditText;
    private EditText achternaamEditText;
    private EditText opmerkingenEditText;
    private CheckBox paspoortCheckbox;
    private ImageView fotoImageView;
    private ImageButton takeImageButton;
    private Button saveButton;


    public CursistFormFragment() {

    }

    private void setupFields() {
        voornaamEditText = (EditText) getActivity().findViewById(R.id.editTextVoornaam);
        tussenvoegselEditText = (EditText) getActivity().findViewById(R.id.editTextTussenvoegsel);
        achternaamEditText = (EditText) getActivity().findViewById(R.id.editTextAchternaam);
        opmerkingenEditText = (EditText) getActivity().findViewById(R.id.editTextOpmerkingen);
        paspoortCheckbox = (CheckBox) getActivity().findViewById(R.id.checkBoxPaspoort);
        fotoImageView = (ImageView) getActivity().findViewById(imageViewFoto);
        saveButton = (Button) getActivity().findViewById(R.id.buttonSave);
        takeImageButton = (ImageButton) getActivity().findViewById(R.id.imageButtonPhoto);
        takeImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(v);
            }
        });

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CursistFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CursistFormFragment newInstance() {
        CursistFormFragment fragment = new CursistFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cursist_form, container, false);

        // workaround because onClick in xml does not work for fragments, it sends the onClick to the activity instead.
        Button saveButton = (Button) v.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickSaveCursist(v);
            }
        });
        // Inflate the layout for this fragment
        return v;

    }

    public void setCursist(Cursist cursist) {
        this.cursist = cursist;
        populateFields();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateFields() {

        if (voornaamEditText == null) {
            setupFields();
        }
        // Check if we're working with an existing cursist or a new empty one.
        if (cursist.id != null && cursist.id != 0) {
            voornaamEditText.setText(cursist.voornaam);
            tussenvoegselEditText.setText(cursist.tussenvoegsel);
            achternaamEditText.setText(cursist.achternaam);
            opmerkingenEditText.setText(cursist.opmerking);
            if (cursist.paspoort == null) {
                paspoortCheckbox.setChecked(false);
            } else {
                paspoortCheckbox.setChecked(true);
            }
            // TODO Set fotoImageview to foto thing somehow.
            //fotoImageView;
        }
    }

    /**
     * Read the entered data and use it to fill the member Cursist instance cursist.
     */
    private void readCursist() {
        cursist.voornaam = voornaamEditText.getText().toString();
        cursist.tussenvoegsel = tussenvoegselEditText.getText().toString();
        cursist.achternaam = achternaamEditText.getText().toString();
        cursist.opmerking = opmerkingenEditText.getText().toString();
        if (paspoortCheckbox.isChecked()) {
            // check if it was already checked, if not, set date of today.
            if (cursist.paspoort == null) {
                cursist.paspoort = new Date();
            }
        } else {
            cursist.paspoort = null;
        }
        // Check if picture was taken, if so, path to the photo is not null.
        if (mCurrentPhotoPath != null && mCurrentPhotoPath != "") {
            //Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
            BitmapDrawable drawable = (BitmapDrawable) fotoImageView.getDrawable();
            Bitmap bm = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String image = Base64.encodeToString(b, Base64.NO_WRAP);
            cursist.setFotoFileBase64(image);

        }
    }

    public void onClickSaveCursist(View view) {
        saveButton.setEnabled(false);
        readCursist();
        mListener.saveCursist(cursist);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void saveCursist(Cursist cursist);

    }

    // ------------------------------------ PHOTO SUPPORT -----------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "cwoapp.nl.cwoapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = DateUtil.dateTo_yyyyMMdd_HHmmsString(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = fotoImageView.getWidth();
        int targetH = fotoImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        fotoImageView.setImageBitmap(bitmap);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            setPic();
/*            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoImageView.setImageBitmap(imageBitmap);*/
        }
    }
}
