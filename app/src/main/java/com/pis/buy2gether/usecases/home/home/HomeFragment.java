package com.pis.buy2gether.usecases.home.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.pis.buy2gether.R;
import com.pis.buy2gether.model.domain.pojo.Grup.Category;
import com.pis.buy2gether.usecases.common.adaptadoresUniversales.SeccionesAdapter;
import com.pis.buy2gether.usecases.home.home.product_view.group.creation.GroupCreationFragment;

public class HomeFragment extends Fragment {

    private Category [] categories = Category.values();

    private HomeViewModel homeViewModel;
    private SeccionesAdapter adapter_categoria;
    private TabLayout tabLayout_categoria;
    private ViewPager viewPager_categoria;
    private EditText search_bar;
    private Button cloud_upload;
    //private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);;


        //inicialitzem els components del fragment home
        tabLayout_categoria = root.findViewById(R.id.tab_layout);
        viewPager_categoria = root.findViewById(R.id.viewPager);
        search_bar = root.findViewById(R.id.search_bar);
        cloud_upload = root.findViewById(R.id.cloud_upload);

        //instanciem l'adaptador per viewpager de home_fragment

        adapter_categoria= new SeccionesAdapter(getChildFragmentManager());
        // Solución temporal para que funcione el viewpager
        for (Category category : categories) {
            adapter_categoria.addFragment(new TabFragment(category), category.toString());
        }


        //adaptem el viewPager amb l'adaptador que acabem de crear
        viewPager_categoria.setAdapter(adapter_categoria);
        tabLayout_categoria.setupWithViewPager(viewPager_categoria);



        cloud_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Ocultem el menú inferior */
                getActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
                /* Canviem de fragment al d'ajuda */
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new GroupCreationFragment());
                fragmentTransaction.addToBackStack("home").commit();
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TabFragment)adapter_categoria.getItem(viewPager_categoria.getCurrentItem())).filterText(search_bar.getText().toString());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}