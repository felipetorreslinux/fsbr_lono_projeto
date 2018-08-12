package com.lono.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.Models.List_Plans_Model;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.CalcTerms;
import com.lono.Utils.Price;
import com.lono.Views.View_New_Account_Free;
import com.lono.Views.View_New_Account_PF_Plus;
import com.lono.Views.View_New_Account_PJ_Plus;
import com.lono.Views.View_Send_Mail_Plan_200;

import java.util.List;


public class List_Plans_Adapter extends RecyclerView.Adapter<List_Plans_Adapter.PlansHolder> {

    List<List_Plans_Model> list_plans;
    Activity activity;

    public List_Plans_Adapter(final Activity activity, final List<List_Plans_Model> list_plans){
        this.list_plans = list_plans;
        this.activity = activity;
    }

    @Override
    public PlansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_plans, parent, false);
        return new List_Plans_Adapter.PlansHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlansHolder holder, int position) {
        final List_Plans_Model list_plans_model = list_plans.get(position);

        int val_min_term = list_plans_model.getLimite_min_termos();
        double val_mensal = list_plans_model.getValor_termo() * val_min_term;
        double val_anual = (list_plans_model.getValor_termo() * val_min_term) * 11;

        switch (list_plans_model.getId_servico()) {
            case 1:
                holder.textview_name_plan.setText("Plano " + list_plans_model.getNome_servico());
                holder.price_plan_mensal.setText(Price.real(val_mensal));
                holder.price_plan_anual.setText(Price.real(val_anual));
                holder.textview_number_terms.setVisibility(View.GONE);
                holder.textview_number_journal.setText("Todos os Jornais");
                holder.textview_number_pesq_retro.setText("Pesquisa retroativa de 1 semana");
                holder.textview_number_ocorrency_email.setText("Ocorrências por Email");
                holder.button_add_plan.setText( R.string.button_list_plans_pf );
                holder.button_add_plan_pj.setVisibility( View.VISIBLE );
                break;
            case 2:
                holder.grid_table_prices_plans.setVisibility(View.GONE);
                holder.textview_name_plan.setText("Plano " + list_plans_model.getNome_servico());
                holder.button_add_plan.setText(R.string.label_button_add_plan);
                holder.box_qtd_plans_plus.setVisibility(View.GONE);
                holder.textview_number_terms.setText("Acima de 200 Termos");
                holder.textview_number_journal.setText("Todos os Jornais");
                holder.textview_number_pesq_retro.setText("Pesquisa retroativa de 1 semana");
                holder.textview_number_ocorrency_email.setText("Ocorrências por Email");
                holder.button_add_plan_pj.setVisibility( View.GONE );
                break;
            case 3:
                holder.grid_table_prices_plans.setVisibility(View.GONE);
                holder.textview_name_plan.setText("Plano " + list_plans_model.getNome_servico());
                holder.box_qtd_plans_plus.setVisibility(View.GONE);
                holder.textview_number_journal.setText("1 Jornal");
                holder.textview_number_terms.setText("1 Termo");
                holder.textview_number_pesq_retro.setText("Pesquisa retroativa de 1 dia");
                holder.textview_number_ocorrency_email.setVisibility(View.GONE);
                holder.button_add_plan_pj.setVisibility( View.GONE );
                break;
            default:
                holder.button_add_plan.setText(R.string.button_list_plans);
        }


        holder.button_remove_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTerms(holder, list_plans_model);
            }
        });


        holder.button_add_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTerms(holder, list_plans_model);
            }
        });


        holder.button_add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list_plans_model.getId_servico()){
                    case 1:
                        Intent intent_plus = new Intent(activity, View_New_Account_PF_Plus.class);
                        intent_plus.putExtra("id_servico", list_plans_model.getId_servico());
                        intent_plus.putExtra("nome_servico", list_plans_model.getNome_servico());
                        intent_plus.putExtra("qtd_termos", holder.qtd_terms_plan_plus.getText().toString().trim());
                        intent_plus.putExtra( "valor_termo", list_plans_model.getValor_termo());
                        intent_plus.putExtra( "min_termos", list_plans_model.getLimite_min_termos());
                        intent_plus.putExtra( "max_termos", list_plans_model.getLimite_max_termos());
                        activity.startActivityForResult(intent_plus, 1);
                        break;
                    case 2:
                        Intent intent_more_200 = new Intent(activity, View_Send_Mail_Plan_200.class);
                        activity.startActivityForResult(intent_more_200, 1);
                        break;
                    case 3:
                        Intent intent_free = new Intent(activity, View_New_Account_Free.class);
                        intent_free.putExtra("id_servico", list_plans_model.getId_servico());
                        intent_free.putExtra("nome_servico", list_plans_model.getNome_servico());
                        activity.startActivityForResult(intent_free, 1);
                        break;
                }
            }
        });

        holder.button_add_plan_pj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_plus = new Intent(activity, View_New_Account_PJ_Plus.class);
                intent_plus.putExtra("id_servico", list_plans_model.getId_servico());
                intent_plus.putExtra("nome_servico", list_plans_model.getNome_servico());
                intent_plus.putExtra("qtd_termos", holder.qtd_terms_plan_plus.getText().toString().trim());
                intent_plus.putExtra( "valor_termo", list_plans_model.getValor_termo());
                intent_plus.putExtra( "min_termos", list_plans_model.getLimite_min_termos());
                intent_plus.putExtra( "max_termos", list_plans_model.getLimite_max_termos());
                activity.startActivityForResult(intent_plus, 1);
            }
        } );

    }

    @Override
    public int getItemCount() {
        return list_plans != null ? list_plans.size() : 0;
    }

    public class PlansHolder extends RecyclerView.ViewHolder {

        TextView textview_name_plan;
        TextView price_plan_mensal;
        TextView price_plan_anual;
        TextView textview_number_journal;
        TextView textview_number_terms;
        TextView textview_number_pesq_retro;
        TextView textview_number_ocorrency_email;

        GridLayout grid_table_prices_plans;
        LinearLayout box_qtd_plans_plus;
        LinearLayout box_count_qtd_plan_plus;
        Button button_add_plan;
        Button button_add_plan_pj;

        ImageView button_remove_terms;
        TextView qtd_terms_plan_plus;
        ImageView button_add_terms;


        public PlansHolder(View itemView) {
            super(itemView);

            box_count_qtd_plan_plus = itemView.findViewById(R.id.box_count_qtd_plan_plus);
            box_qtd_plans_plus = itemView.findViewById(R.id.box_qtd_plans_plus);

            textview_name_plan = itemView.findViewById(R.id.textview_name_plan);

            price_plan_anual = itemView.findViewById(R.id.price_plan_anual);
            price_plan_mensal = itemView.findViewById(R.id.price_plan_mensal);

            textview_number_terms = itemView.findViewById(R.id.textview_number_terms);
            textview_number_journal = itemView.findViewById(R.id.textview_number_journal);
            textview_number_pesq_retro = itemView.findViewById(R.id.textview_number_pesq_retro);
            textview_number_ocorrency_email = itemView.findViewById(R.id.textview_number_ocorrency_email);

            grid_table_prices_plans = itemView.findViewById(R.id.grid_table_prices_plans);

            button_add_terms = itemView.findViewById(R.id.button_add_terms);
            qtd_terms_plan_plus = itemView.findViewById(R.id.qtd_terms_plan_plus);
            button_remove_terms = itemView.findViewById(R.id.button_remove_terms);
            button_add_plan = itemView.findViewById(R.id.button_add_plan);
            button_add_plan_pj = itemView.findViewById( R.id.button_add_plan_pj );

        }
    }

    private void removeTerms(PlansHolder holder, List_Plans_Model model) {
        int terms = Integer.parseInt(holder.qtd_terms_plan_plus.getText().toString().trim());
        terms--;
        if(terms <= model.getLimite_min_termos()){
            terms = model.getLimite_min_termos();
        }
        holder.qtd_terms_plan_plus.setText(String.valueOf(terms));
        holder.price_plan_mensal.setText(Price.real( CalcTerms.value_mensal( model.getValor_termo(), terms ) ) );
        holder.price_plan_anual.setText(Price.real( CalcTerms.value_anual( model.getValor_termo(), terms ) ) );

    }

    private void addTerms(PlansHolder holder, List_Plans_Model model) {
        int terms = Integer.parseInt(holder.qtd_terms_plan_plus.getText().toString().trim());
        terms++;
        holder.price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( model.getValor_termo(), terms ) ) );
        holder.price_plan_anual.setText( Price.real( CalcTerms.value_anual( model.getValor_termo(), terms ) ) );
        holder.qtd_terms_plan_plus.setText( String.valueOf( terms ) );
    }
}
