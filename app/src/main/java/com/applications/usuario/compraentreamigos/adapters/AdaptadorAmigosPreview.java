package com.applications.usuario.compraentreamigos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applications.usuario.compraentreamigos.models.Amigo;
import com.applications.usuario.compraentreamigos.R;

import java.util.List;

public class AdaptadorAmigosPreview extends RecyclerView.Adapter<AdaptadorAmigosPreview.Holder> {

    private List<Amigo> ListaAmigos;
    private OnItemClickListener itemClickListener;

    public AdaptadorAmigosPreview(List<Amigo> listaAmigos, OnItemClickListener ItemClickListener) {
        ListaAmigos = listaAmigos;
        itemClickListener = ItemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_layout, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Amigo amigo = ListaAmigos.get(position);
        holder.bind(amigo.getName(),amigo.getPaid(),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return ListaAmigos.size();
    }

    protected static class Holder extends RecyclerView.ViewHolder {
        TextView TVname,TVpaid;
        Holder(View itemView) {
            super(itemView);
            TVname = itemView.findViewById(R.id.textViewName);
            TVpaid = itemView.findViewById(R.id.textPaid);
        }

        void bind(String Name, double paid, final OnItemClickListener listener) {
        TVname.setText(Name);
        TVpaid.setText(String.valueOf(paid));
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(getAdapterPosition());
                return true;
            }
        });
        }
    }

    public interface OnItemClickListener{
        void onItemLongClick(int position);
    }
}
