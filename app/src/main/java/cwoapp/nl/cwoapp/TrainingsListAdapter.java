package cwoapp.nl.cwoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import cwoapp.nl.cwoapp.entity.CwoEis;

/**
 * Created by sonja on 3/15/2017.
 */

public class TrainingsListAdapter extends RecyclerView.Adapter<TrainingsListAdapter.TrainingsListAdapterViewHolder> {
    private static final String TAG = TrainingsListAdapter.class.getSimpleName();

    List<CwoEis> cwoEisList = null;
    private final TrainingListAdapterOnClickHandler clickHandler;

    public TrainingsListAdapter(TrainingListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public TrainingsListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.training_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrainingsListAdapter.TrainingsListAdapterViewHolder viewHolder = new TrainingsListAdapter.TrainingsListAdapterViewHolder(view);
        return viewHolder;
    }

    public void setCwoData(List<CwoEis> cwoEis) {
        this.cwoEisList = cwoEis;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TrainingsListAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (cwoEisList == null)
            return 0;
        return cwoEisList.size();
    }

    class TrainingsListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox cbCwoEis;

        public TrainingsListAdapterViewHolder(View itemView) {
            super(itemView);
            cbCwoEis = (CheckBox) itemView.findViewById(R.id.checkBoxTrainingsEis);
            cbCwoEis.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick triggered");

            int adapterPosition = getAdapterPosition();
            CwoEis cwoEis = cwoEisList.get(adapterPosition);

            // Using !cboEis.isSelected because checkbox is only toggled AFTER this is called. So this makes it use the intended position rather than the current one.
            clickHandler.onClick(cwoEis, !cbCwoEis.isSelected());

        }

        void bind(int position) {
            CwoEis cwoEis = cwoEisList.get(position);
            cbCwoEis.setText(cwoEis.getTitel());
        }
    }

    public interface TrainingListAdapterOnClickHandler {
        void onClick(CwoEis cwoEis, boolean selected);
    }
}
