package com.lono.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_Publications;
import com.lono.Models.Publicacao_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Publications {

    Activity activity;

    public Service_Publications(Activity activity){
        this.activity = activity;
    }

    public void listOcorrencyToday(final RecyclerView recyclerView, final ProgressBar progressBar, final TextView info_response_pub){
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Server.URL()+"services/obter-todas-materias")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray array = response.getJSONArray("materias");
                                if(array.length() > 0){
                                    List<Publicacao_Model> list = new ArrayList<>();
                                    for (int i  = 0; i < array.length(); i++){
                                        JSONObject jsonObject = array.getJSONObject(i);
                                        Publicacao_Model publicacaoModel = new Publicacao_Model(
                                                jsonObject.getInt("id_cliente"),
                                                jsonObject.getBoolean("materia_conferida"),
                                                jsonObject.getString("sit_cad"),
                                                jsonObject.getBoolean("percentual"),
                                                jsonObject.getString("termo_percentual"),
                                                jsonObject.getString("pagina"),
                                                jsonObject.getBoolean("exportado_email"),
                                                jsonObject.getBoolean("sincronizado_android"),
                                                jsonObject.getBoolean("sincronizado_iphone"),
                                                jsonObject.getBoolean("exportado_pdf"),
                                                jsonObject.getInt("id_publicacao"),
                                                jsonObject.getInt("id_materia"),
                                                jsonObject.getString("titulo_materia"),
                                                jsonObject.getString("subtitulo"),
                                                jsonObject.getString("pre_materia"),
                                                jsonObject.getString("processo"),
                                                jsonObject.getString("materia"),
                                                jsonObject.getString("materia_hash"),
                                                jsonObject.getBoolean("corte_lono"),
                                                jsonObject.getString("dt_divulgacao"),
                                                jsonObject.getString("dt_publicacao"),
                                                jsonObject.getString("edicao_publicacao"),
                                                jsonObject.getInt("id_jornal"),
                                                jsonObject.getString("status_publicacao"),
                                                jsonObject.getString("nome_pesquisa"),
                                                jsonObject.getBoolean("literal"),
                                                jsonObject.getBoolean("oab"),
                                                jsonObject.getString("id_pauta"),
                                                jsonObject.getString("pagina_pauta"),
                                                jsonObject.getString("pauta"),
                                                jsonObject.getString("nome_jornal"),
                                                jsonObject.getString("sigla_jornal"),
                                                jsonObject.getString("nome_orgao"),
                                                jsonObject.getString("sigla_orgao"));
                                        list.add(publicacaoModel);
                                    }
                                    Adapter_Publications adapterPublications = new Adapter_Publications(activity, list);
                                    recyclerView.setAdapter(adapterPublications);
                                    progressBar.setVisibility(View.GONE);
                                    info_response_pub.setVisibility(View.GONE);
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    info_response_pub.setVisibility(View.VISIBLE);
                                    info_response_pub.setText("Não há publicações");
                                }
                                break;

                            default:

                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });

    }


}
