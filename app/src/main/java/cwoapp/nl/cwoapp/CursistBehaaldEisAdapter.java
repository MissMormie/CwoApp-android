package cwoapp.nl.cwoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.DiplomaEis;

/**
 * Created by sonja on 3/15/2017.
 */

public class CursistBehaaldEisAdapter extends RecyclerView.Adapter<CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder> {
    private List<DiplomaEis> diplomaEisList;
    private Cursist cursist;
    private final CursistBehaaldEisAdapter.CursistBehaaldEisAdapterOnClickHandler clickHandler;
    private boolean saveData = true; // OnChangeChecklistener saves date when checkbox is clicked, but also when data is refeshed. Using to as workaround.

    public CursistBehaaldEisAdapter(CursistBehaaldEisAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;

    }


    @Override
    public CursistBehaaldEisViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        // TODO make an actual layout thing for this.
        int layoutIdForListItem = R.layout.training_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder viewHolder = new CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CursistBehaaldEisViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (diplomaEisList == null)
            return 0;
        return diplomaEisList.size();
    }

    public void setCwoListData(List<DiplomaEis> diplomaEisList) {
        this.diplomaEisList = diplomaEisList;
    }

    public void setCursist(Cursist cursist) {
        this.cursist = cursist;
        notifyDataSetChanged();
    }

    class CursistBehaaldEisViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbCwoEis;

        public CursistBehaaldEisViewHolder(final View itemView) {
            super(itemView);
            cbCwoEis = (CheckBox) itemView.findViewById(R.id.checkBoxTrainingsEis);
            cbCwoEis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!saveData)
                        return;

                    int adapterPosition = getAdapterPosition();
                    DiplomaEis de = diplomaEisList.get(adapterPosition);

                    CursistBehaaldEis cbe = new CursistBehaaldEis(cursist, de, isChecked);
                    clickHandler.onClick(cbe);
                }
            });

        }


        void bind(int position) {
            DiplomaEis diplomaEis = diplomaEisList.get(position);
            cbCwoEis.setText(diplomaEis.getTitel());
            boolean eisBehaald = cursist.isEisBehaald(diplomaEis);
            saveData = false;
            cbCwoEis.setChecked(eisBehaald);
            saveData = true;
        }
    }


    public interface CursistBehaaldEisAdapterOnClickHandler {
        void onClick(CursistBehaaldEis cursistBehaaldEis);
    }

}
