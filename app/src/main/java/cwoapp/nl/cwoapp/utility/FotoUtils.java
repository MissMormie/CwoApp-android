package cwoapp.nl.cwoapp.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sonja on 3/17/2017.
 */

public class FotoUtils {

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

    }

    // Heavily borrowed from: http://stackoverflow.com/questions/8232608/fit-image-into-imageview-keep-aspect-ratio-and-then-resize-imageview-to-image-d
    public static void setFoto(ImageView imageView, Bitmap bitmap, Context context) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        // get size in px of the imageView.
        int boundingHeight = imageView.getHeight();
        int boundingWidth = imageView.getWidth();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundingWidth) / width;
        float yScale = ((float) boundingHeight) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        imageView.setImageBitmap(bitmap);

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        //width = scaledBitmap.getWidth(); // re-use
        //height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(context.getResources(), scaledBitmap);

        // Apply the scaled bitmap
        imageView.setImageDrawable(result);

    }

}
