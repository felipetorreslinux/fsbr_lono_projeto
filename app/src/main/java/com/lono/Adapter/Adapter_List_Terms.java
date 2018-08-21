package com.lono.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.Models.Terms_Model;
import com.lono.R;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter_List_Terms extends RecyclerView.Adapter<Adapter_List_Terms.TermsHolder> {

    Activity activity;
    List<Terms_Model> list_terms;
    AlertDialog.Builder builder;

    public Adapter_List_Terms(Activity activity, List<Terms_Model> list_terms){
        this.activity = activity;
        this.list_terms = list_terms;
        this.builder = new AlertDialog.Builder(activity);
    }

    @NonNull
    @Override
    public Adapter_List_Terms.TermsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_terms, parent, false);
        return new Adapter_List_Terms.TermsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_List_Terms.TermsHolder holder, final int position) {
        final Terms_Model termsModel = list_terms.get(position);

        holder.name_term.setText(termsModel.getName());

        if(termsModel.isLiteral()){
            holder.literal_term.setText("Sim");
        }else{
            holder.literal_term.setText("Não");
        }

        holder.image_remove_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja remover o termo "+termsModel.getName()+" ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list_terms.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_terms != null ? list_terms.size() : 0;
    }

    public class TermsHolder extends RecyclerView.ViewHolder{

        TextView name_term;
        TextView literal_term;
        ImageView image_remove_terms;

        public TermsHolder(View itemView) {
            super(itemView);

            name_term = itemView.findViewById(R.id.name_term);
            literal_term = itemView.findViewById(R.id.literal_term);
            image_remove_terms = itemView.findViewById(R.id.image_remove_terms);
        }
    }
}
