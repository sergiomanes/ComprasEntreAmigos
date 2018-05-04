package com.applications.usuario.compraentreamigos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        List<Amigo> Lista = bundle.getParcelableArrayList("MiLista");
        RecyclerView Recyclerlistado = (RecyclerView) findViewById(R.id.RecyclerDetail);

        Recyclerlistado.setHasFixedSize(true);
        Recyclerlistado.setItemAnimator(new DefaultItemAnimator());

        Recyclerlistado.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorAmigosDetail adaptador = new AdaptadorAmigosDetail(Lista);
        Recyclerlistado.setAdapter(adaptador);

    }
}
