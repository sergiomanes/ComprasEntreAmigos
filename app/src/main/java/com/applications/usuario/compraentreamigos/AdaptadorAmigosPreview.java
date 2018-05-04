package com.applications.usuario.compraentreamigos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.Bind(amigo.getName(),amigo.getPaid(),amigo.getDiscount(),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return ListaAmigos.size();
    }




    class Holder extends RecyclerView.ViewHolder {
        TextView TVname,TVpaid,TVdiscount;
        Holder(View itemView) {
            super(itemView);
            TVname = (TextView) itemView.findViewById(R.id.textViewName);
            TVpaid = (TextView) itemView.findViewById(R.id.textPaid);
            TVdiscount = (TextView) itemView.findViewById(R.id.textViewDiscount);
        }

        void Bind(String Name, double paid, double discount, final OnItemClickListener listener) {
        TVname.setText(Name);
        TVpaid.setText(String.valueOf(paid));
        TVdiscount.setText(String.valueOf(discount));
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
