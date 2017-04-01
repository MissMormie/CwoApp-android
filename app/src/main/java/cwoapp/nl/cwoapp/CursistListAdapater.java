package cwoapp.nl.cwoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;


/**
 * Created by Sonja on 3/9/2017.
 */

public class CursistListAdapater extends RecyclerView.Adapter<CursistListAdapater.CursistListAdapterViewHolder> {
    // For logging:
    private static final String TAG = CursistListAdapater.class.getSimpleName();
    protected List<Cursist> cursistList;
    private final CursistListAdapterOnClickHandler clickHandler;
    private final Context context;


    public CursistListAdapater(CursistListAdapterOnClickHandler clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public void setCursistListData(List<Cursist> cursistList) {
        this.cursistList = cursistList;
        notifyDataSetChanged();
    }


    @Override
    public CursistListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.cursist_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        CursistListAdapterViewHolder viewHolder = new CursistListAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CursistListAdapterViewHolder holder, int position) {
        holder.bind(position);
//        holder.view.setBackgroundColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        if(cursistList == null)
            return 0;
        return cursistList.size();
    }

    class CursistListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView cursistItemTextView;
        final ImageView fotoImageView;
        final View view;

        public CursistListAdapterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            cursistItemTextView = (TextView) itemView.findViewById(R.id.tv_cursist_data);
            fotoImageView = (ImageView) itemView.findViewById(R.id.imageViewFoto);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Cursist cursist = cursistList.get(position);
            cursistItemTextView.setText(cursist.nameToString());

            // Set foto
            if (cursist.getCursistFoto() != null) {
                String imgData = cursist.getCursistFoto().getThumbnail();
                byte[] imgByteArray = Base64.decode(imgData, Base64.NO_WRAP);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
                fotoImageView.setImageBitmap(bitmap);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_user_image);
                fotoImageView.setImageDrawable(drawable);
            }

            // Set background color in case cursist is hidden.
            // TODO get this from colors class. needs getResources().
            if (cursist.isVerborgen())
                this.view.setBackgroundColor(Color.GRAY);
            else
                this.view.setBackgroundColor(Color.WHITE);

        }

        @Override
        public void onClick(View v) {
            System.out.println("onClick " + TAG);
            int adapterPosition = getAdapterPosition();
            Cursist cursist = cursistList.get(adapterPosition);
            clickHandler.onClick(cursist);

        }
    }


    public interface CursistListAdapterOnClickHandler {
        void onClick(Cursist cursist);
    }

}
