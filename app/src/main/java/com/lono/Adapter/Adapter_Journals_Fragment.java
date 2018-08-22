package com.lono.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lono.Models.Journals_Model;
import com.lono.R;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter_Journals_Fragment extends RecyclerView.Adapter<Adapter_Journals_Fragment.JournalsHolder> {

    Activity activity;
    List<Journals_Model> list_journals;
    TextView text_info_journals;

    public Adapter_Journals_Fragment(Activity activity, List<Journals_Model> list_journals){
        this.activity = activity;
        this.list_journals = list_journals;
        this.text_info_journals = activity.findViewById(R.id.text_info_journals);
    }

    @NonNull
    @Override
    public Adapter_Journals_Fragment.JournalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journals_fragment, parent, false);
        return new Adapter_Journals_Fragment.JournalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Journals_Fragment.JournalsHolder holder, int position) {
        Journals_Model journalsModel = list_journals.get(position);

        holder.name_journal.setText(journalsModel.getName());

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

        TextView name_journal;

        public JournalsHolder(View itemView) {
            super(itemView);

            name_journal = itemView.findViewById(R.id.name_journal);

        }
    }
}
