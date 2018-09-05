package com.lono.Views;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.MaskCNPJ;
import com.lono.Utils.MaskCPF;

import java.util.ArrayList;
import java.util.List;

public class View_NewAccount extends AppCompatActivity implements View.OnClickListener {

    int TYPE_PERSON_REGISTER;

    Toolbar toolbar;
    MenuItem button_register;

    TextInputLayout layout_document_register;
    TextInputLayout layout_razao_social_register;
    TextInputLayout layout_name_register;
    TextInputLayout layout_email_register;
    TextInputLayout layout_cellphone_register;
    TextInputLayout layout_password_register;
    TextInputLayout layout_conf_password_register;
    TextInputLayout layout_genre_register;

    EditText document_register;
    EditText razao_social_register;
    EditText name_register;
    EditText email_register;
    EditText cellphone_register;
    EditText password_register;
    EditText conf_password_register;
    EditText genre_register;

    @SuppressLint({"NewApi", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_newaccount);
        createToolbar(toolbar);

        TYPE_PERSON_REGISTER = 1;

        layout_document_register = findViewById(R.id.layout_document_register);
        layout_razao_social_register = findViewById(R.id.layout_razao_social_register);
        layout_name_register = findViewById(R.id.layout_name_register);
        layout_email_register = findViewById(R.id.layout_email_register);
        layout_cellphone_register = findViewById(R.id.layout_cellphone_register);
        layout_password_register = findViewById(R.id.layout_password_register);
        layout_conf_password_register = findViewById(R.id.layout_conf_password_register);
        layout_genre_register = findViewById(R.id.layout_genre_register);

        document_register = findViewById(R.id.document_register);
        razao_social_register = findViewById(R.id.razao_social_register);
        name_register = findViewById(R.id.name_register);
        email_register = findViewById(R.id.email_register);
        cellphone_register = findViewById(R.id.cellphone_register);
        password_register = findViewById(R.id.password_register);
        conf_password_register = findViewById(R.id.conf_password_register);
        genre_register = findViewById(R.id.genre_register);
        genre_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(View_NewAccount.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_genre, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                final List<String> genre = new ArrayList<>();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Keyboard.close(View_NewAccount.this, v);
                    }
                }, 30);

                alertDialog.show();
                ListView listView = view.findViewById(R.id.list_genre);
                genre.clear();
                genre.add("Masculino");
                genre.add("Feminino");
                ArrayAdapter arrayAdapter = new ArrayAdapter(View_NewAccount.this, android.R.layout.simple_list_item_1, genre);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        genre_register.setText(parent.getItemAtPosition(position).toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });


        switch (TYPE_PERSON_REGISTER){
            case 1:
                layout_document_register.setHint("CPF");
                document_register.addTextChangedListener(MaskCPF.insert(document_register));
                layout_razao_social_register.setVisibility(View.GONE);
                break;
            case 2:
                layout_document_register.setHint("CNPJ");
                document_register.addTextChangedListener(MaskCNPJ.insert(document_register));
                layout_razao_social_register.setVisibility(View.VISIBLE);
                break;
            case 3:
                layout_document_register.setHint("CPF");
                document_register.addTextChangedListener(MaskCPF.insert(document_register));
                layout_razao_social_register.setVisibility(View.GONE);
                break;
        }

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_newaccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_new_acoount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newaccount, menu);
        button_register = menu.findItem(R.id.send_new_account).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.send_new_account:
                registerNewAccount();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    private void registerNewAccount() {
    }
}
