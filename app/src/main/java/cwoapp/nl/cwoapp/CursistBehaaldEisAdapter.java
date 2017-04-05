package cwoapp.nl.cwoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
 * Cursist Behaald Eis Adapter
 */

class CursistBehaaldEisAdapter extends RecyclerView.Adapter<CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder> {
    private List<DiplomaEis> diplomaEisList;
    private Cursist cursist;
    private boolean saveData = true; // OnChangeChecklistener saves data when checkbox is clicked, but also when data is refeshed. Using to as workaround.
    private Context context;


    @Override
    public CursistBehaaldEisViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.training_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new CursistBehaaldEisAdapter.CursistBehaaldEisViewHolder(view);
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

    void setCwoListData(List<DiplomaEis> diplomaEisList) {
        this.diplomaEisList = diplomaEisList;
        //notifyDataSetChanged();
    }

    public void setCursist(Cursist cursist) {
        this.cursist = cursist;
        notifyDataSetChanged();
    }

    class CursistBehaaldEisViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbCwoEis;
        final ImageButton imgButtonInfo;

        CursistBehaaldEisViewHolder(final View itemView) {
            super(itemView);
            cbCwoEis = (CheckBox) itemView.findViewById(R.id.checkBoxTrainingsEis);
            imgButtonInfo = (ImageButton) itemView.findViewById(R.id.imageButtonInfo);

            setListeners();

        }

        void setListeners() {
            // set on checked listener for checkbox
            cbCwoEis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!saveData)
                        return;

                    int adapterPosition = getAdapterPosition();
                    DiplomaEis de = diplomaEisList.get(adapterPosition);

                    CursistBehaaldEis cursistBehaaldEis = new CursistBehaaldEis(cursist, de, isChecked);
                    cursist.addOrRemoveDiplomaEis(cursistBehaaldEis);

                    new SaveEisBehaaldTask().execute(cursistBehaaldEis);
                }
            });

            imgButtonInfo.setOnClickListener(new ImageButton.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    DiplomaEis diplomaEis = diplomaEisList.get(adapterPosition);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage(diplomaEis.getOmschrijving())
                            .setTitle(diplomaEis.getTitel())
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // We doen niets, maar je moet deze hebben om eruit te komen.
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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
            if(cursist == null)
                return;
            // Reset cbCwoEis so it's standard enabled.
            cbCwoEis.setEnabled(true);
            boolean checked;
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

    private class SaveEisBehaaldTask extends AsyncTask<CursistBehaaldEis, Void, Boolean> {

        /**
         * params: Long cursist Id, Long cwoEis id, Boolean behaald.
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
        }
    }


}
