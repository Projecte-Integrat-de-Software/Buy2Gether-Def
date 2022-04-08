package com.pis.buy2gether.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pis.buy2gether.R;
import com.pis.buy2gether.databinding.FragmentFriendsBinding;
import com.pis.buy2gether.ui.favorite.FavoriteFragment;
import com.pis.buy2gether.ui.user.UserFragment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsFragment extends Fragment implements FriendsListAdapter.ItemClickListener {

    private FriendsViewModel friendsViewModel;
    private FriendsListAdapter friendsListAdapter;
    private FragmentFriendsBinding binding;

    //ImageButton btn_return;
    ImageButton btn_amics;
    ImageButton btn_settings;
    ImageButton btn_lan;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_friends,container,false);
        btn_amics = view.findViewById(R.id.btn_amics);
        ImageButton btn_return = view.findViewById(R.id.btn_return);
        btn_settings = view.findViewById(R.id.btn_settings);
        btn_lan = view.findViewById(R.id.btn_lan);

        ArrayList<String> items = new ArrayList<>();
        items.add("Horse");
        items.add("Cow");
        items.add("Camel");
        items.add("Sheep");
        items.add("Chen");
        items.add("Tula");
        items.add("Chica");
        items.add("Golfo");
        items.add("Juanjo");
        items.add("Tupapa");
        items.add("Goat");
        items.add("Goat");
        items.add("Goat");
        items.add("Goat");
        items.add("Goat");
        items.add("Goat");
        items.add("Goat");
        items.add("Sheep");
        items.add("Sheep");
        items.add("Goat");
        items.add("Goat");

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"RETURN",Toast.LENGTH_SHORT).show();
                /* Canviem de fragment al que conté la llista d'amics */
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.friends, new UserFragment());
                fragmentTransaction.commit();
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = binding.friendsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsListAdapter = new FriendsListAdapter(getContext(), items);
        recyclerView.setAdapter(friendsListAdapter);
        friendsListAdapter.setClickListener(this);
        binding.friendsList.setAdapter(friendsListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                friendsListAdapter.swipe((FriendsListAdapter.ViewHolder) viewHolder);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        friendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // data to populate the RecyclerView with
            }

        });

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + friendsListAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}