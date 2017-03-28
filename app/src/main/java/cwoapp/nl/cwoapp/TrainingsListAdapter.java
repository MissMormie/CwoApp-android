package cwoapp.nl.cwoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import cwoapp.nl.cwoapp.entity.DiplomaEis;

/**
 * Created by sonja on 3/15/2017.
 */

public class TrainingsListAdapter extends RecyclerView.Adapter<TrainingsListAdapter.TrainingsListAdapterViewHolder> {
    private static final String TAG = TrainingsListAdapter.class.getSimpleName();

    List<DiplomaEis> diplomaEisList = null;
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

    public void setCwoData(List<DiplomaEis> diplomaEis) {
        this.diplomaEisList = diplomaEis;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TrainingsListAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (diplomaEisList == null)
            return 0;
        return diplomaEisList.size();
    }

    class TrainingsListAdapterViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener */ {
        CheckBox cbCwoEis;

        public TrainingsListAdapterViewHolder(View itemView) {
            super(itemView);
            cbCwoEis = (CheckBox) itemView.findViewById(R.id.checkBoxTrainingsEis);
            cbCwoEis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapterPosition = getAdapterPosition();
                    DiplomaEis diplomaEis = diplomaEisList.get(adapterPosition);

                    clickHandler.onClick(diplomaEis, isChecked);
                }
            });
        }

        /*
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick triggered");

            int adapterPosition = getAdapterPosition();
            DiplomaEis diplomaEis = diplomaEisList.get(adapterPosition);



            // Using !cboEis.isSelected because checkbox is only toggled AFTER this is called. So this makes it use the intended position rather than the current one.
            clickHandler.onClick(diplomaEis, !cbCwoEis.isSelected());

        }*/

        void bind(int position) {
            DiplomaEis diplomaEis = diplomaEisList.get(position);
            cbCwoEis.setText(diplomaEis.getTitel());
        }
    }

    public interface TrainingListAdapterOnClickHandler {
        void onClick(DiplomaEis diplomaEis, boolean selected);
    }
}
