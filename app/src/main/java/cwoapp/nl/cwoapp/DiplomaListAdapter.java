package cwoapp.nl.cwoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import cwoapp.nl.cwoapp.entity.Diploma;

/**
 * Created by sonja on 4/1/2017.
 * Uses diploma_list_item to show entered list of Diploma's, including checkbox behaviour.
 */

class DiplomaListAdapter extends RecyclerView.Adapter<DiplomaListAdapter.DiplomaListAdapterViewHolder> {
    private List<Diploma> diplomaList;
    final private DiplomaListAdapterOnClickHandler clickHandler;
    Context context;

    DiplomaListAdapter(DiplomaListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public DiplomaListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.diploma_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new DiplomaListAdapter.DiplomaListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiplomaListAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (diplomaList == null)
            return 0;
        return diplomaList.size();
    }

    public void setDiplomaList(List<Diploma> diplomaList) {
        this.diplomaList = diplomaList;
        notifyDataSetChanged();
    }

    class DiplomaListAdapterViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener */ {
        private CheckBox checkBoxDiploma;

        DiplomaListAdapterViewHolder(View itemView) {
            super(itemView);
            checkBoxDiploma = (CheckBox) itemView.findViewById(R.id.checkBoxDiploma);

            setListeners();
        }

        private void setListeners() {
            checkBoxDiploma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapterPosition = getAdapterPosition();
                    Diploma diploma = diplomaList.get(adapterPosition);

                    clickHandler.onClick(diploma, isChecked);
                }
            });

        }

        void bind(int position) {
            Diploma diploma = diplomaList.get(position);
            checkBoxDiploma.setChecked(clickHandler.isSelectedDiploma(diploma));
            checkBoxDiploma.setText(diploma.getTitel() + " " + diploma.getNivo());
        }
    }

    interface DiplomaListAdapterOnClickHandler {
        void onClick(Diploma diploma, boolean selected);

        boolean isSelectedDiploma(Diploma diploma);
    }
}
