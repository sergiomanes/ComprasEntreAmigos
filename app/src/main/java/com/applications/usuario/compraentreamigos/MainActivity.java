package com.applications.usuario.compraentreamigos;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ImageButton calc,trash,continuar,helper;
    enum  faseIngreso {NOMBRE,PAGO,DESCUENTO}; //me sirve para saber si debe ingresar el nombre, lo que pago o lo que se le debe descontar.
    faseIngreso fase;
    AdaptadorAmigosPreview Previewadapter;

    //para encontrar la calculadora del sistema
    ArrayList<HashMap<String,Object>> items;
    PackageManager pm ;
    List<PackageInfo> packs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = (TextView) findViewById(R.id.textViewTitle);
        pagoGrupal = (TextView) findViewById(R.id.textViewpagoGrupalPesos);
        continuar = (ImageButton) findViewById(R.id.buttonSiguiente);
        calcular = (Button) findViewById(R.id.buttonCalcular);
        textoEditable = (EditText) findViewById(R.id.editTextField);
        RecycleLista = (RecyclerView) findViewById(R.id.recyclerViewLista);
        calc = (ImageButton) findViewById(R.id.imageButtonCalc);
        trash = (ImageButton) findViewById(R.id.imageButtonTrash);
        helper = (ImageButton) findViewById(R.id.imageButtonHelp);

        Amigos = new ArrayList<>();
        fase = faseIngreso.NOMBRE;


        RecycleLista.setHasFixedSize(true); //mejora performance si se que no va a cambiar el tamano de la letra
        RecycleLista.setItemAnimator(new DefaultItemAnimator()); //animacion por default

        RecycleLista.setLayoutManager(new LinearLayoutManager(this));
        Previewadapter = new AdaptadorAmigosPreview(Amigos, new AdaptadorAmigosPreview.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
                DeleteRow(position);
            }
        });
        RecycleLista.setAdapter(Previewadapter);

        //obtengo lista de apps para luego buscar en la calculadora
        items =new  ArrayList<HashMap<String,Object>>();
        pm = getPackageManager();
        packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("appName", pi.applicationInfo.loadLabel(pm));
            map.put("packageName", pi.packageName);
            items.add(map);
        }


      calc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openCalculator();
        }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {

                try {
                    switch (fase)
                    {
                        case NOMBRE:
                            if(textoEditable.getText().toString().equals("")){
                                Toast.makeText(MainActivity.this,"Escribí el nombre de tu amigo :@",Toast.LENGTH_SHORT).show();
                                break;
                            }
                            amigo = new Amigo(textoEditable.getText().toString(),0,0,0);
                            SetFase(R.string.CuantoPago,"",faseIngreso.PAGO);
                            textoEditable.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                            break;

                        case PAGO:
                            amigo.setPaid(Double.parseDouble(textoEditable.getText().toString()));
                            SetFase(R.string.Descontar,"0",faseIngreso.DESCUENTO);
                            break;

                        case DESCUENTO:
                            amigo.setDiscount(Double.parseDouble(textoEditable.getText().toString()));
                            SetFase(R.string.NombreAmigo,"",faseIngreso.NOMBRE);
                            textoEditable.setInputType(InputType.TYPE_CLASS_TEXT);
                            Amigos.add(amigo);
                            Previewadapter.notifyDataSetChanged();
                            Double PagoGrupalActual = Double.parseDouble(pagoGrupal.getText().toString().replace(",", "."))+amigo.getPaid();
                            pagoGrupal.setText(String.format("%.2f", PagoGrupalActual));
                            Toast.makeText(MainActivity.this,"¡Amigo agregado! :D",Toast.LENGTH_SHORT).show();
                            break;

                        default: break;

                    }

                } catch (Exception excepcion)
                {
                    Toast.makeText(MainActivity.this,"Acá tenes que escribir un número :|",Toast.LENGTH_SHORT).show();
                }

            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Amigos.size()>1)
                {
                    AsignarPagos();
                    Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
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

        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SetFase(int cuantoPago, String relleno, faseIngreso pago) {
        titulo.setText(cuantoPago);
        textoEditable.setText(relleno);
        fase = pago;
    }

    private void AsignarPagos(){
        double totalAAplicar = Double.parseDouble(pagoGrupal.getText().toString().replace(",","."));
        this.calcularDescuentoAAplicar();
        Amigo amigoAux;

        for(int i = 0; i<Amigos.size();i++)
        {
            amigoAux = Amigos.get(i);

            amigoAux.setHasToPay(amigoAux.getPaid()-(totalAAplicar/Amigos.size())+amigoAux.getDiscount()-amigoAux.getHasToPay()); //

            Amigos.set(i,amigoAux);
        }
    }

    //toma el descuento de cada amigo y lo distribuye al total a pagar de cada amigo
    private void calcularDescuentoAAplicar(){

        double DescuentoAAplicar;

        ResetHasToPay();

        for (int i = 0; i < Amigos.size(); i++)
        {
            DescuentoAAplicar = 0;
            Amigo AmigoConDesc = Amigos.get(i);
            if(AmigoConDesc.getDiscount()>0)
            {
                DescuentoAAplicar = AmigoConDesc.getDiscount()/(Amigos.size()-1);
                for (int j = 0; j < Amigos.size(); j++ )
                {
                    if (i!=j)
                    {
                        Amigos.get(j).setHasToPay(Amigos.get(j).getHasToPay()+DescuentoAAplicar);
                    }
                }
            }

        }

    }

    // vuelvo a 0 hasToPay
    private void ResetHasToPay(){
        for (int i = 0; i < Amigos.size(); i++)
        {
            Amigos.get(i).setHasToPay(0);
        }
    }

    private void ResetAll()
    {
        Amigos.clear();
        RecycleLista.getAdapter().notifyDataSetChanged();
        fase = faseIngreso.NOMBRE;
        titulo.setText(R.string.NombreAmigo);
        pagoGrupal.setText("0");
        textoEditable.setText("");
    }

    private void DeleteRow(int position){

        Double PagoGrupalActual = Double.parseDouble(pagoGrupal.getText().toString().replace(",", "."))-Amigos.get(position).getPaid();
        pagoGrupal.setText(String.format("%.2f", PagoGrupalActual));
        Amigos.remove(position);
        RecycleLista.getAdapter().notifyItemRemoved(position);

    }

    //Busco entre todos las app, la calculadora del sistema
    public void openCalculator(){
        int d=0;
        if(items.size()>=1){
            int j=0;
            for(j=0;j<items.size();j++){
                String AppName = (String) items.get(j).get("appName");
                if(AppName.contains("Calc"))
                {
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
