package com.lono.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lono.Models.Slide_Payment_Model;
import com.lono.R;
import com.lono.Utils.CalcTerms;
import com.lono.Utils.Price;
import com.lono.Views.View_Type_Payment;

import org.w3c.dom.Text;

import java.util.List;

import br.com.uol.pagseguro.domain.direct.Holder;

public class Adapter_Slide_Plans_Payment extends PagerAdapter {

    View view;
    Activity activity;
    List<Slide_Payment_Model> itens_slides;

    public Adapter_Slide_Plans_Payment(final Activity activity, final List<Slide_Payment_Model> list_slides){
        this.activity = activity;
        this.itens_slides = list_slides;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide_payment, container, false);
        final Slide_Payment_Model slide_payment_model = itens_slides.get(position);

        TextView title = view.findViewById(R.id.title_payment_slide);
        TextView suvtitle = view.findViewById(R.id.subtitle_payment_slide);
        final TextView qtd_terms_payment = view.findViewById(R.id.qtd_terms_payment);
        final TextView edit_qtd_terms = view.findViewById(R.id.edit_qtd_terms);
        final TextView price_plan_payment = view.findViewById(R.id.price_plan_payment);
        TextView description_plans = view.findViewById(R.id.description_plans);
        Button button_select_payment = view.findViewById(R.id.button_select_payment);

        switch (slide_payment_model.getType()){
            case 0:
                price_plan_payment.setText(Price.real(CalcTerms.value_anual(slide_payment_model.getValue_termos(), Integer.parseInt(slide_payment_model.getQtd_termos()))));
                break;

            case 1:
                price_plan_payment.setText(Price.real(CalcTerms.value_mensal(slide_payment_model.getValue_termos(), Integer.parseInt(slide_payment_model.getQtd_termos()))));
                break;
        }

        title.setText(slide_payment_model.getTitle());
        suvtitle.setText(slide_payment_model.getSubtitle());
        qtd_terms_payment.setText("Você selecionou\n"+slide_payment_model.getQtd_termos()+" termos");
        description_plans.setText(slide_payment_model.getDescription_plam());
        button_select_payment.setText(slide_payment_model.getLabel_button());

        edit_qtd_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTermsPayment(slide_payment_model, qtd_terms_payment,price_plan_payment);
            }
        });

        button_select_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTypePayment(slide_payment_model);
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        return itens_slides != null ? itens_slides.size() : 0;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }


    private void editTermsPayment(final Slide_Payment_Model slide_payment_model, final TextView textView_qtd, final TextView textView_price){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( activity );
        View view = activity.getLayoutInflater().inflate( R.layout.bottomsweet_edit_terms_payment, null );
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        final ImageView remove = view.findViewById( R.id.terms_remove );
        final ImageView add = view.findViewById( R.id.terms_add );
        final TextView qtd_terms = view.findViewById( R.id.qtd_terms );
        qtd_terms.setText( slide_payment_model.getQtd_termos() );

        remove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TERMS = Integer.parseInt( slide_payment_model.getQtd_termos() );
                TERMS--;
//                if(TERMS <= slide_payment_model.getMin_terms()) {
//                    TERMS = slide_payment_model.getMin_terms();
//                }
                if(TERMS <= 10) {
                    TERMS = 10;
                }
                slide_payment_model.setQtd_termos(String.valueOf(TERMS));
                qtd_terms.setText(slide_payment_model.getQtd_termos());
                textView_qtd.setText("Você selecionou\n"+slide_payment_model.getQtd_termos()+" termos");
                switch (slide_payment_model.getType()){
                    case 0:
                        textView_price.setText( Price.real( CalcTerms.value_anual( slide_payment_model.getValue_termos(), TERMS)));
                        break;

                    case 1:
                        textView_price.setText( Price.real( CalcTerms.value_mensal( slide_payment_model.getValue_termos(), TERMS)));
                        break;
                }
            }
        });

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TERMS = Integer.parseInt( slide_payment_model.getQtd_termos() );
                TERMS++;
//                if(TERMS >= slide_payment_model.getMax_terms()) {
//                    TERMS = slide_payment_model.getMax_terms();
//                }
                if(TERMS >= 200) {
                    TERMS = 200;
                }
                slide_payment_model.setQtd_termos(String.valueOf(TERMS));
                qtd_terms.setText(slide_payment_model.getQtd_termos());
                textView_qtd.setText("Você selecionou\n"+slide_payment_model.getQtd_termos()+" termos");
                switch (slide_payment_model.getType()){
                    case 0:
                        textView_price.setText( Price.real( CalcTerms.value_anual( slide_payment_model.getValue_termos(), TERMS)));
                        break;

                    case 1:
                        textView_price.setText( Price.real( CalcTerms.value_mensal( slide_payment_model.getValue_termos(), TERMS)));
                        break;
                }
            }
        });
    }

    private void openTypePayment(Slide_Payment_Model slide_payment_model){
        switch (slide_payment_model.getType()){
            case 0:
                Intent intent_anual = new Intent( activity, View_Type_Payment.class );
                intent_anual.putExtra("name_plam","Anual");
                intent_anual.putExtra("type_person", slide_payment_model.getType_person());
                intent_anual.putExtra("document", slide_payment_model.getDocument());
                intent_anual.putExtra("name", slide_payment_model.getName());
                intent_anual.putExtra("qtd_terms", slide_payment_model.getQtd_termos());
                intent_anual.putExtra("price", CalcTerms.value_anual(slide_payment_model.getValue_termos(), Integer.parseInt(slide_payment_model.getQtd_termos())));
                intent_anual.putExtra("price_plam", Price.real( CalcTerms.value_anual( slide_payment_model.getValue_termos(),  Integer.parseInt( slide_payment_model.getQtd_termos() ))));
                intent_anual.putExtra("validate_plam", "12 meses");
                activity.startActivity( intent_anual );
                break;

            case 1:
                Intent intent_mensal = new Intent( activity, View_Type_Payment.class );
                intent_mensal.putExtra("name_plam","Mensal");
                intent_mensal.putExtra("type_person", slide_payment_model.getType_person());
                intent_mensal.putExtra("document", slide_payment_model.getDocument());
                intent_mensal.putExtra("name", slide_payment_model.getName());
                intent_mensal.putExtra("qtd_terms", slide_payment_model.getQtd_termos());
                intent_mensal.putExtra("price", CalcTerms.value_mensal(slide_payment_model.getValue_termos(), Integer.parseInt(slide_payment_model.getQtd_termos())));
                intent_mensal.putExtra("price_plam", Price.real( CalcTerms.value_mensal( slide_payment_model.getValue_termos(),  Integer.parseInt( slide_payment_model.getQtd_termos() ))));
                intent_mensal.putExtra("validate_plam", "30 dias");
                activity.startActivity( intent_mensal );
                break;
        }
    }
}
