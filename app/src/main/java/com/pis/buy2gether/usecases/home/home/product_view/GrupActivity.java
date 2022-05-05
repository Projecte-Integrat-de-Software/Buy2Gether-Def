package com.pis.buy2gether.usecases.home.home.product_view;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.pis.buy2gether.R;

import java.util.ArrayList;

public class GrupActivity extends AppCompatActivity {
    private String product_id;
    private ImageButton back;
    private RecyclerView valoracion;
    private GrupViewModel viewModel;
    private ShapeableImageView image;
    private TextView description;
    private TextView precio;
    private Button unirse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_id = extras.getString("product_id");
        }

        binding();
        addListeners();

        setContentView(R.layout.activity_product_view);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        valoracion = findViewById(R.id.lista_valoraciones);
        description = findViewById(R.id.descripcion);


        ArrayList<String> valoraciones = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            valoraciones.add("Hola" + i);
        }

        ValoracionsAdapter adapter = new ValoracionsAdapter(valoraciones);
        valoracion.setLayoutManager(new LinearLayoutManager(this));
        valoracion.setAdapter(adapter);
        GrupViewModel viewModel = new GrupViewModel(product_id);
        observeView(viewModel);
    }

    private void binding() {
        image = findViewById(R.id.foto_producto);
        unirse = findViewById(R.id.btn_unirse);
        unirse.setText(viewModel.btn_text());
        precio = findViewById(R.id.precios);
        description = findViewById(R.id.descripcion);
    }

    private void addListeners(){
        unirse.setOnClickListener(v -> {
            viewModel.btn_action();
        } );
    }

    private void observeView(GrupViewModel viewModel) {
        description.setText(viewModel.getDescription());
        precio.setText(viewModel.getPrecio());
        image.setImageBitmap(viewModel.getPhoto());
    }
}