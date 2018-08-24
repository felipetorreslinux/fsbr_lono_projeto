package com.lono.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lono.Models.Publicacao_Model;
import com.lono.R;
import com.lono.Views.View_Materia;

import java.util.List;

public class Adapter_Publications extends RecyclerView.Adapter<Adapter_Publications.PubsHolder> {

    Activity activity;
    List<Publicacao_Model> list_pubs;

    public Adapter_Publications(Activity activity, List<Publicacao_Model> list_pubs){
        this.activity = activity;
        this.list_pubs = list_pubs;
    }

    @NonNull
    @Override
    public PubsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pubs, parent, false);
        return new Adapter_Publications.PubsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PubsHolder holder, int position) {
        final Publicacao_Model publicacaoModel = list_pubs.get(position);

        holder.id_pub.setText("#"+ String.valueOf(publicacaoModel.getId_materia()) +"\n" +
                "Termo: " + publicacaoModel.getNome_pesquisa()+"\n" +
                "Orgão: " + publicacaoModel.getNome_orgao()+"\n" +
                "Jornal: " + publicacaoModel.getNome_jornal()+"\n"+
                "Titulo: " + publicacaoModel.getTitulo_materia()
        );

        holder.id_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, View_Materia.class);
                intent.putExtra("id_materia", String.valueOf(publicacaoModel.getId_materia()));
                intent.putExtra("nome_termo", publicacaoModel.getNome_pesquisa());
                intent.putExtra("materia", publicacaoModel.getMateria());
                activity.startActivityForResult(intent, 3000);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_pubs != null ? list_pubs.size() : 0;
    }

    public class PubsHolder extends RecyclerView.ViewHolder{

        TextView id_pub;

        public PubsHolder(View itemView) {
            super(itemView);

            id_pub = itemView.findViewById(R.id.id_pub);
        }
    }
}
