package com.lono.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Models.Terms_Model;
import com.lono.R;
import com.lono.Service.Service_Terms_Journals;
import com.lono.Utils.Alerts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_Terms_Fragment extends RecyclerView.Adapter<Adapter_Terms_Fragment.TermsHoder>{

    Activity activity;
    AlertDialog.Builder builder;
    List<Terms_Model> list_terms;
    LinearLayout layout_info_terms;
    Service_Terms_Journals serviceTermsJournals;

    public Adapter_Terms_Fragment(Activity activity, List<Terms_Model> list_terms){
        this.activity = activity;
        this.list_terms = list_terms;
        this.builder = new AlertDialog.Builder(activity);
        this.layout_info_terms = activity.findViewById(R.id.layout_info_terms);
        this.serviceTermsJournals = new Service_Terms_Journals(activity);
    }

    @NonNull
    @Override
    public TermsHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_terms_fragment, parent, false);
        return new Adapter_Terms_Fragment.TermsHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsHoder holder, final int position) {
        final Terms_Model termsModel = list_terms.get(position);

        holder.name_terms_frag.setText(termsModel.getName());

        if(termsModel.isLiteral()){
            holder.term_literal.setText("Literal");
        }else{
            holder.term_literal.setText(null);
        }

        holder.image_remove_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja remover este termo?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTerms(termsModel, position);
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list_terms.size() > 0){
            layout_info_terms.setVisibility(View.GONE);
        }else{
            layout_info_terms.setVisibility(View.VISIBLE);
        }
        return list_terms != null ? list_terms.size() : 0;
    }

    public class TermsHoder extends RecyclerView.ViewHolder {

        TextView name_terms_frag;
        TextView term_literal;
        ImageView image_remove_terms;

        public TermsHoder(View itemView) {
            super(itemView);
            name_terms_frag = itemView.findViewById(R.id.name_terms_frag);
            term_literal = itemView.findViewById(R.id.term_literal);
            image_remove_terms = itemView.findViewById(R.id.image_remove_terms);
        }
    }

    public void removeTerms (final Terms_Model terms_model, final int position){
        Snackbar.make(activity.getWindow().getDecorView(),
                "Removendo termo...", Snackbar.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_nome_pesquisa", terms_model.getId());
            AndroidNetworking.post(Server.URL()+"services/remover-termo-cliente")
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
                                        Snackbar.make(activity.getWindow().getDecorView(),
                                                response.getString("message"), Snackbar.LENGTH_SHORT).show();
                                        list_terms.remove(terms_model);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        break;

                                    default:
                                        Snackbar.make(activity.getWindow().getDecorView(),
                                                response.getString("message"), Snackbar.LENGTH_SHORT).show();
                                        break;
                                }
                            }catch (JSONException e){}
                        }

                        @Override
                        public void onError(ANError anError) {
                            Server.ErrorServer(activity, anError.getErrorCode());
                        }
                    });
        }catch (JSONException e){}
    }

}
