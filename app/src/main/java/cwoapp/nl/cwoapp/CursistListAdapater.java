package cwoapp.nl.cwoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



    public CursistListAdapater(CursistListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
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
    }

    @Override
    public int getItemCount() {
        if(cursistList == null)
            return 0;
        return cursistList.size();
    }

    class CursistListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cursistItemTextView;

        public CursistListAdapterViewHolder(View itemView) {
            super(itemView);
            cursistItemTextView = (TextView) itemView.findViewById(R.id.tv_cursist_data);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Cursist cursist = cursistList.get(position);
            cursistItemTextView.setText(cursist.nameToString());
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
