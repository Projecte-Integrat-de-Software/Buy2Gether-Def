package com.pis.buy2gether.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.pis.buy2gether.R;
import com.pis.buy2gether.databinding.FragmentProfileBinding;
import com.pis.buy2gether.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentUserBinding binding;

    Button btn_comandes;
    Button btn_adreces;
    Button btn_historial;
    Button btn_ajuda;
    Button btn_amics;
    Button btn_settings;
    Button btn_lan;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
            CODI INTELLIJ

        * userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
        * */

        View view = inflater.inflate(R.layout.fragment_user,container,false);
        btn_comandes = view.findViewById(R.id.btn_comandes);
        btn_adreces = view.findViewById(R.id.btn_adreces);
        btn_ajuda = view.findViewById(R.id.btn_ajuda);
        btn_amics = view.findViewById(R.id.btn_amics);
        btn_historial = view.findViewById(R.id.btn_historial);
        btn_settings = view.findViewById(R.id.btn_settings);
        btn_lan = view.findViewById(R.id.btn_lan);


        /**
         * codi repetitiu -> crear classe interna
         */
        btn_comandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"COMANDES",Toast.LENGTH_SHORT).show();
            }
        });

        btn_adreces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"ADRECES",Toast.LENGTH_SHORT).show();
            }
        });

        btn_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"AJUDA",Toast.LENGTH_SHORT).show();
            }
        });

        btn_amics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"AMICS",Toast.LENGTH_SHORT).show();
            }
        });

        btn_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"HISTORIAL",Toast.LENGTH_SHORT).show();
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"SETTINGS",Toast.LENGTH_SHORT).show();
            }
        });

        btn_lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"LANGUAGE",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}