package com.lono.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lono.Models.All_Jorunals_Model;
import com.lono.R;

import java.util.List;

public class Adapter_All_Journals extends RecyclerView.Adapter<Adapter_All_Journals.JornalsHolder> {

    Activity activity;
    List<All_Jorunals_Model> list_journals;

    public Adapter_All_Journals(Activity activity, List<All_Jorunals_Model> list_journals){
        this.activity = activity;
        this.list_journals = list_journals;
    }

    @NonNull
    @Override
    public JornalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_journal, parent, false);
        return new Adapter_All_Journals.JornalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JornalsHolder holder, final int position) {
        final All_Jorunals_Model allJorunalsModel = list_journals.get(position);

        holder.sigle_journal.setText(allJorunalsModel.getSigle());
        holder.name_journal.setText(allJorunalsModel.getName());
        holder.select_journal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonView.setChecked(true);
                    allJorunalsModel.setSelected(true);
                }else{
                    buttonView.setChecked(false);
                    allJorunalsModel.setSelected(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_journals != null ? list_journals.size() : 0;
    }

    public class JornalsHolder extends RecyclerView.ViewHolder {

        CheckBox select_journal;
        TextView sigle_journal;
        TextView name_journal;

        public JornalsHolder(View itemView) {
            super(itemView);

            select_journal = itemView.findViewById(R.id.select_journal);
            sigle_journal = itemView.findViewById(R.id.sigle_journal);
            name_journal = itemView.findViewById(R.id.name_journal);
        }
    }
}
