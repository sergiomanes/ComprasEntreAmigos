package com.applications.usuario.compraentreamigos.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applications.usuario.compraentreamigos.models.Amigo;
import com.applications.usuario.compraentreamigos.R;

import java.util.List;


public class AdaptadorAmigosDetail extends RecyclerView.Adapter<AdaptadorAmigosDetail.Holder> {

    private List<Amigo> ListaAmigos;

    public AdaptadorAmigosDetail(List<Amigo> listaAmigos) {
        ListaAmigos = listaAmigos;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Amigo amigo = (Amigo) ListaAmigos.get(position);
        String DebeOLeDeben = "PAGA";
        int color = Color.parseColor("#d81b60");
        if (amigo.getHasToPay()>0) { DebeOLeDeben = "LE DEBEN"; color = Color.parseColor("#1976d2");}
        amigo.setHasToPay(Math.abs(amigo.getHasToPay()));
        holder.AsignarDatos(amigo.getName(),DebeOLeDeben,amigo.getHasToPay(), color);
    }

    @Override
    public int getItemCount() {
        return ListaAmigos.size();
    }

    protected static class Holder extends RecyclerView.ViewHolder {
        TextView  TextViewNombre,TextViewDebeLeDeben,TextViewValor;
        public Holder(View itemView) {
            super(itemView);
            TextViewNombre = (TextView) itemView.findViewById(R.id.textViewNombre);
            TextViewDebeLeDeben = (TextView) itemView.findViewById(R.id.textViewDebeLeDeben);
            TextViewValor = (TextView) itemView.findViewById(R.id.textViewValor);
        }

         void AsignarDatos(String name, String debeOLeDeben, double hasToPay, int color) {
            TextViewNombre.setText(name);
            TextViewDebeLeDeben.setText(debeOLeDeben);
            TextViewDebeLeDeben.setTextColor(color);
            TextViewValor.setText(String.format("%.2f", hasToPay));
        }
    }
}
