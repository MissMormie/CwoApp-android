package cwoapp.nl.cwoapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.DiplomaEis;
import cwoapp.nl.cwoapp.utility.NetworkUtils;

/**
 * Created by sonja on 3/15/2017.
 */

public class CursistBehaaldEisAdapter extends RecyclerView.Adapter<CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder> {
    private List<DiplomaEis> diplomaEisList;
    private Cursist cursist;
    private boolean saveData = true; // OnChangeChecklistener saves data when checkbox is clicked, but also when data is refeshed. Using to as workaround.
    Context context;

    public CursistBehaaldEisAdapter() {
    }


    @Override
    public CursistBehaaldEisViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

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
        notifyDataSetChanged();
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

            // set on checked listener for checkbox
            cbCwoEis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!saveData)
                        return;

                    int adapterPosition = getAdapterPosition();
                    DiplomaEis de = diplomaEisList.get(adapterPosition);

                    CursistBehaaldEis cursistBehaaldEis = new CursistBehaaldEis(cursist, de, isChecked);
                    new SaveEisBehaaldTask().execute(cursistBehaaldEis);
                }
            });

            // Set on clicked listener for info button.
            ImageButton infoButton = (ImageButton) itemView.findViewById(R.id.imageButtonInfo);
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //change the action here, a toast in your example
                    DiplomaEis diplomaEis = diplomaEisList.get(getAdapterPosition());
                    Toast.makeText(context, diplomaEis.getOmschrijving(), Toast.LENGTH_LONG);

                }
            });

        }


        void bind(int position) {
            DiplomaEis diplomaEis = diplomaEisList.get(position);
            cbCwoEis.setText(diplomaEis.getDiploma().getTitel() + " " + diplomaEis.getDiploma().getNivo() + " " + diplomaEis.getTitel());

            // Save data set to false so changing of checkbox doesn't trigger saving to server.
            saveData = false;
            setCheckboxIfEisBehaald(diplomaEis);

            saveData = true;
        }

        private void setCheckboxIfEisBehaald(DiplomaEis diplomaEis) {
            // Reset cbCwoEis so it's standard enabled.
            cbCwoEis.setEnabled(true);
            boolean checked = false;
            // als diploma behaald is, is automatisch elke eis voor het diploma ook behaald.
            Long diplomaId = diplomaEis.getDiploma().getId();
            if (cursist.hasDiploma(diplomaId)) {
                checked = true;
                // als diploma gehaald is kan deze niet ge unchecked worden.
                cbCwoEis.setEnabled(false);
            } else {
                checked = cursist.isEisBehaald(diplomaEis);
            }

            cbCwoEis.setChecked(checked);

        }
    }

    class SaveEisBehaaldTask extends AsyncTask<CursistBehaaldEis, Void, Boolean> {

        /**
         * params: Long cursist Id, Long cwoEis id, Boolean behaald.
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(CursistBehaaldEis... params) {
            CursistBehaaldEis cursistBehaaldEis = params[0];
            if (cursistBehaaldEis.isBehaald())
                return saveCursistBehaaldEis(cursistBehaaldEis, "POST");
            else
                return saveCursistBehaaldEis(cursistBehaaldEis, "DELETE");

        }

        private boolean saveCursistBehaaldEis(CursistBehaaldEis cursistBehaaldEis, String action) {

            URL url = NetworkUtils.buildUrl("cursistBehaaldEisen");
            //String json = "{\"eisId\": 1, \"cursistId\": 1}";
            String json = cursistBehaaldEis.toJson();
            try {
                int resultCode = NetworkUtils.uploadToServer(url, json, action);
                if (resultCode == 200)
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;

        }


        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                String error = context.getString(R.string.opslaan_mislukt);
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

            }
            // TODO determine what needs to happen when this is finished. Especially in case of errors.
            // Step 1 done, do i need to do something else?
        }
    }


}
