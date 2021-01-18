package com.applications.usuario.compraentreamigos.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.usuario.compraentreamigos.R;
import com.applications.usuario.compraentreamigos.adapters.AdaptadorAmigosPreview;
import com.applications.usuario.compraentreamigos.models.Amigo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView titulo, pagoGrupal;
    Button calcular;
    EditText textoEditable;
    RecyclerView RecycleLista;
    List<Amigo> Amigos;
    Amigo amigo;
    ImageButton calc,trash,continuar;
    enum faseIngreso {NOMBRE,PAGO}
    faseIngreso fase;
    AdaptadorAmigosPreview Previewadapter;
    double pagoTotalGrupal = 0;

    //para encontrar la calculadora del sistema
    ArrayList<HashMap<String,Object>> items;
    PackageManager pm;
    List<PackageInfo> packs;
    Handler calculatorHandler = new Handler();
    Runnable calculatorRunnable = new Runnable() {
        @Override
        public void run() {
            //obtengo lista de apps para luego buscar en la calculadora
            items = new ArrayList<>();
            pm = getPackageManager();
            packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
            calc.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = findViewById(R.id.textViewTitle);
        pagoGrupal = findViewById(R.id.textViewpagoGrupalPesos);
        continuar = findViewById(R.id.buttonSiguiente);
        calcular = findViewById(R.id.buttonCalcular);
        textoEditable = findViewById(R.id.editTextField);
        RecycleLista = findViewById(R.id.recyclerViewLista);
        calc = findViewById(R.id.imageButtonCalc);
        trash = findViewById(R.id.imageButtonTrash);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalculator();
            }
        });

        Amigos = new ArrayList<>();
        fase = faseIngreso.NOMBRE;

        pagoGrupal.setText(String.format(getResources().getString(R.string.pago_grupal), pagoTotalGrupal));

        RecycleLista.setItemAnimator(new DefaultItemAnimator()); //animacion por default

        RecycleLista.setLayoutManager(new LinearLayoutManager(this));
        Previewadapter = new AdaptadorAmigosPreview(Amigos, new AdaptadorAmigosPreview.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
                DeleteRow(position);
            }
        });
        RecycleLista.setAdapter(Previewadapter);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (fase) {
                    case NOMBRE:
                        if(textoEditable.getText().toString().equals("")){
                            Toast.makeText(MainActivity.this,"Escribí el nombre de tu amigo :@",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        amigo = new Amigo(textoEditable.getText().toString(),0,0);
                        SetFase(R.string.CuantoPago, faseIngreso.PAGO);
                        textoEditable.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        break;

                    case PAGO:
                        amigo.setPaid(Double.parseDouble(textoEditable.getText().toString()));
                        SetFase(R.string.NombreAmigo, faseIngreso.NOMBRE);
                        textoEditable.setInputType(InputType.TYPE_CLASS_TEXT);
                        Amigos.add(amigo);
                        Previewadapter.notifyDataSetChanged();
                        pagoTotalGrupal += amigo.getPaid();
                        pagoGrupal.setText(String.format(getResources().getString(R.string.pago_grupal), pagoTotalGrupal));
                        Toast.makeText(MainActivity.this,"¡Amigo agregado! :D",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Amigos.size()>1)
                {
                    AsignarPagos();
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("MiLista", (ArrayList<? extends Parcelable>) Amigos);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Debemos agregar al menos 2 amigos :(",Toast.LENGTH_SHORT).show();
                }

            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Amigos.size()>0)
                {
                    AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Borrar Listado de amigos");
                    builder.setMessage("¿Querés borrar toda la lista de amigos?");

                    builder.setPositiveButton("¡Si!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ResetAll();
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setNegativeButton("No, mejor no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"La lista de amigos esta vacía",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        calculatorHandler.post(calculatorRunnable);
    }

    private void SetFase(int cuantoPago, faseIngreso pago) {
        titulo.setText(cuantoPago);
        textoEditable.setText("");
        fase = pago;
    }

    private void AsignarPagos(){
        resetHasToPay();
        Amigo amigoAux;

        for(int i = 0; i<Amigos.size();i++)
        {
            amigoAux = Amigos.get(i);

            amigoAux.setHasToPay(amigoAux.getPaid()-(pagoTotalGrupal/Amigos.size()));

            Amigos.set(i,amigoAux);
        }
    }

    // vuelvo a 0 hasToPay
    private void resetHasToPay() {
        for (int i = 0; i < Amigos.size(); i++) {
            Amigos.get(i).setHasToPay(0);
        }
    }

    private void ResetAll()
    {
        Amigos.clear();
        RecycleLista.getAdapter().notifyDataSetChanged();
        fase = faseIngreso.NOMBRE;
        titulo.setText(R.string.NombreAmigo);
        pagoTotalGrupal = 0;
        pagoGrupal.setText(String.format(getResources().getString(R.string.pago_grupal), pagoTotalGrupal));
        textoEditable.setText("");
    }

    private void DeleteRow(int position) {
        pagoTotalGrupal -= Amigos.get(position).getPaid();
        pagoGrupal.setText(String.format(getResources().getString(R.string.pago_grupal), pagoTotalGrupal));
        Amigos.remove(position);
        RecycleLista.getAdapter().notifyItemRemoved(position);

    }

    //Busco entre todos las app, la calculadora del sistema
    private void openCalculator() {
        int d = 0;
        if(items.size() >= 1) {
            for(int j=0; j<items.size(); j++) {
                String AppName = (String) items.get(j).get("appName");
                if(AppName.contains("Calc")) {
                    d=j;
                    break;
                }
            }
            String packageName = (String) items.get(d).get("packageName");
            Intent i = pm.getLaunchIntentForPackage(packageName);
            if (i != null){
                startActivity(i);}
            else {
                Toast.makeText(this,"No es posible abrir la Calculadora :(",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"No es posible abrir la Calculadora :(",Toast.LENGTH_SHORT).show();
        }
    }
}
