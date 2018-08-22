package com.lono.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Models.Journals_Model;
import com.lono.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class Adapter_Journals_Fragment extends RecyclerView.Adapter<Adapter_Journals_Fragment.JournalsHolder> {

    Activity activity;
    AlertDialog.Builder builder;
    List<Journals_Model> list_journals;
    TextView text_info_journals;

    public Adapter_Journals_Fragment(Activity activity, List<Journals_Model> list_journals){
        this.activity = activity;
        this.list_journals = list_journals;
        this.text_info_journals = activity.findViewById(R.id.text_info_journals);
        this.builder = new AlertDialog.Builder(activity);
    }

    @NonNull
    @Override
    public Adapter_Journals_Fragment.JournalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journals_fragment, parent, false);
        return new Adapter_Journals_Fragment.JournalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Journals_Fragment.JournalsHolder holder, final int position) {
        final Journals_Model journalsModel = list_journals.get(position);

        holder.sigle_journal.setText(journalsModel.getSigle());
        holder.name_journal.setText(journalsModel.getName());
        holder.image_remove_journals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja remover este jornal?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeJournals(journalsModel, position);
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list_journals.size() > 0){
            text_info_journals.setVisibility(View.GONE);
        }else{
            text_info_journals.setVisibility(View.VISIBLE);
            text_info_journals.setText("Não há jornais cadastrados");
        }
        return list_journals != null ? list_journals.size() : 0;
    }

    public class JournalsHolder extends RecyclerView.ViewHolder {

        TextView sigle_journal;
        TextView name_journal;
        ImageView image_remove_journals;

        public JournalsHolder(View itemView) {
            super(itemView);

            sigle_journal = itemView.findViewById(R.id.sigle_journal);
            name_journal = itemView.findViewById(R.id.name_journal);
            image_remove_journals = itemView.findViewById(R.id.image_remove_journals);

        }
    }

    public void removeJournals(final Journals_Model journalsModel, final int position){
        Snackbar.make(activity.getWindow().getDecorView(),
                "Removendo jornal...", Snackbar.LENGTH_SHORT).show();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_jornal", journalsModel.getId());
            AndroidNetworking.post(Server.URL()+"services/remover-jornal-cliente")
                    .addHeaders("token", Server.token(activity))
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                String status = response.getString("status");
                                switch (status){
                                    case "success":
                                        list_journals.remove(journalsModel);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        Snackbar.make(activity.getWindow().getDecorView(),
                                                "Jornal removido com sucesso", Snackbar.LENGTH_SHORT).show();
                                        break;

                                        default:
                                            Snackbar.make(activity.getWindow().getDecorView(),
                                                    "Não foi possível remover este jornal no momento", Snackbar.LENGTH_SHORT).show();
                                            break;
                                }
                            }catch (JSONException e){}
                        }

                        @Override
                        public void onError(ANError anError) {
                            Server.ErrorServer(activity, anError.getErrorCode());
                        }
                    });
        }catch (JSONException s){}

    }
}
