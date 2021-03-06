package com.suporttiapp.suporttitecnico;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class Registrar extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREFER_NAME = "Ref";
    private static final String SENHA_ACESSO = "senha";

    private String senha = null;
    private RequestQueue rQueue;
    private EditText nomeTecnico;
    private EditText nomeEmpresa;
    private EditText nomefuncao;
    private Button btCadastrarTecnico;
    private TextView mgsRecebeSenha;
    private TextView tvTecnicoCadastrado;
    private TextView senhaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        rQueue = Volley.newRequestQueue(this);

        nomeTecnico = findViewById(R.id.etNomeTecnico);
        nomeEmpresa = findViewById(R.id.etNomeEmpresa);
        nomefuncao = findViewById(R.id.etNomeFuncao);
        senhaView = findViewById(R.id.mostrarSenha);
        mgsRecebeSenha = findViewById(R.id.tvMensagemSenha);
        tvTecnicoCadastrado = findViewById(R.id.tvTecnicoCadastrado);

        btCadastrarTecnico = findViewById(R.id.btCadastrarTecnico);
        btCadastrarTecnico.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                requisicao();
                nomeTecnico.setText("");
                nomeEmpresa.setText("");
                nomefuncao.setText("");




            }
        });

    }

    //requisicao de cadastro

    private void requisicao(){
        String url = "http://192.168.1.7:8080/SuporteWS/api/tecnico/novo/"+
                nomeTecnico.getText().toString()+"/"+
                nomeEmpresa.getText().toString()+"/"+
                nomefuncao.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           // String senhaRecebida = response.getString("senha");

                            tvTecnicoCadastrado.setText(R.string.tvTecnicoCadastrado);
                            mgsRecebeSenha.setText(R.string.tvMensagemSenha);
                            senhaView.setText(response.getString("senha"));

                            sharedPreferences  = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString(SENHA_ACESSO, senhaView.getText().toString());
                            editor.commit();


                            SharedPreferences preferences = getSharedPreferences(PREFER_NAME,Context.MODE_PRIVATE);
                            String senhaSalva = preferences.getString(SENHA_ACESSO, "");
                            Toast toast = Toast.makeText(getApplicationContext(), senhaSalva, Toast.LENGTH_SHORT);
                            toast.show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            tvTecnicoCadastrado.setText(R.string.tvFalhaDeRede);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        ;

        rQueue.add(request);
    }// fim requisicao


    private void salvarPreferencias(){

    }
}
