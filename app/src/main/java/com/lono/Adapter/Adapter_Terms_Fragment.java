package com.lono.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lono.Models.Terms_Model;
import com.lono.R;

import java.util.List;

public class Adapter_Terms_Fragment extends RecyclerView.Adapter<Adapter_Terms_Fragment.TermsHoder>{

    Activity activity;
    List<Terms_Model> list_terms;

    public Adapter_Terms_Fragment(Activity activity, List<Terms_Model> list_terms){
        this.activity = activity;
        this.list_terms = list_terms;
    }

    @NonNull
    @Override
    public TermsHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_terms_fragment, parent, false);
        return new Adapter_Terms_Fragment.TermsHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsHoder holder, int position) {
        Terms_Model termsModel = list_terms.get(position);

        holder.name_terms_frag.setText(termsModel.getName());

    }

    @Override
    public int getItemCount() {
        return list_terms != null ? list_terms.size() : 0;
    }

    public class TermsHoder extends RecyclerView.ViewHolder {

        TextView name_terms_frag;

        public TermsHoder(View itemView) {
            super(itemView);

            name_terms_frag = itemView.findViewById(R.id.name_terms_frag);
        }
    }
}
