package cwoapp.nl.cwoapp.asyncLoadingTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import cwoapp.nl.cwoapp.utility.FotoUtils;

/**
 * Created by sonja on 3/31/2017.
 * Downloads image from url string and sets it to passed ImageView
 */

public class DownloadAndSetImageTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView bmImage;
    private final Context context;

    public DownloadAndSetImageTask(ImageView bmImage, Context context) {
        this.context = context;
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        FotoUtils.setFoto(bmImage, result, context);
    }


}