package com.pis.buy2gether.usecases.home.user.friends;

import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pis.buy2gether.R;
import com.pis.buy2gether.databinding.FragmentFriendsBinding;
import com.pis.buy2gether.model.session.Session;
import com.pis.buy2gether.provider.ProviderType;
import com.pis.buy2gether.usecases.home.user.UserFragment;
import com.pis.buy2gether.usecases.home.user.address.AddressListAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment implements FriendsListAdapter.ItemClickListener,UsersListAdapter.ItemClickListener {

    private FriendsViewModel friendsViewModel;
    private FriendsListAdapter friendsListAdapter;
    private UsersListAdapter usersListAdapter;
    private RecyclerView recyclerView;
    private FragmentFriendsBinding binding;
    private ArrayList<ClipData.Item> list;

    TextView username,description;
    android.widget.SearchView searchView;

    //ImageButton btn_return;
    ImageButton btn_amics,btn_settings,btn_lan,btn_return;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_friends,container,false);

        username = view.findViewById(R.id.txt_user);
        description = view.findViewById(R.id.txt_desc);
        searchView = view.findViewById(R.id.searchViewFriends);
        //setupUserInfo(view);

        btn_amics = view.findViewById(R.id.btn_amics);
        btn_return = view.findViewById(R.id.btn_return);
        btn_settings = view.findViewById(R.id.btn_settings);
        btn_lan = view.findViewById(R.id.btn_lan);

        binding.btnReturn.setOnClickListener(this::onClick);
        binding.btnAmics.setOnClickListener(this::onClick);

        // set up the RecyclerView
        setListUsers();
        setList();

        return root;
    }

    /*private void setupUserInfo(View view){
        String provider = Session.INSTANCE.getDataSession(getContext(),"provider");
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(ProviderType.valueOf(provider) != ProviderType.GUEST) {
            Session.INSTANCE.getUserByID(currentUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    username.setText(documentSnapshot.get("username").toString());
                    description.setText(documentSnapshot.get("email").toString());
                }
            });
        }
    }*/

    @Override
    public void onItemClick(View view, String friendshipID) {
        friendsViewModel.deleteFriend(friendshipID);
        setList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        boolean process = view.getId() != R.id.shareDummy;
        switch (view.getId()) {
            case R.id.btn_return:
                Toast.makeText(getActivity(), "RETURN", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().beginTransaction().replace(R.id.friends, new UserFragment()).commit();
            break;
            case R.id.btn_amics:
                Toast.makeText(getActivity(), "CERCA D'AMICS", Toast.LENGTH_SHORT).show();
                setListUsers();
                break;
            default:
                break;
        }
    }

    private void setList(){
        recyclerView = binding.friendsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsListAdapter = new FriendsListAdapter(getContext(),this);
        recyclerView.setAdapter(friendsListAdapter);
        binding.friendsList.setAdapter(friendsListAdapter);
        friendsListAdapter.setClickListener(this);

        hideSearch();

        Task task = friendsViewModel.getFriends().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String[] id = documentSnapshot.getData().keySet().toArray(new String[0]);
                    String friendID = id[0].equals(friendsViewModel.getUserID()) ? id[1] : id[0];
                    Task<DocumentSnapshot> task = friendsViewModel.getUserName(friendID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot2) {
                            friendsListAdapter.addFriend(documentSnapshot.getId(),friendID,documentSnapshot2.get("username").toString());

                        }
                    });
                }
            }
        });
    }

    private void setListUsers(){
        recyclerView = binding.friendsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListAdapter = new UsersListAdapter(getContext(),this);
        recyclerView.setAdapter(usersListAdapter);
        binding.friendsList.setAdapter(usersListAdapter);
        usersListAdapter.setClickListener(this);

        showSearch();

        /* Generate list of users available to send Friend request */
        List friendIDs = new ArrayList<>();
        Task t = friendsViewModel.getFriends().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String[] id = documentSnapshot.getData().keySet().toArray(new String[0]);
                    String friendID = id[0].equals(friendsViewModel.getUserID()) ? id[1] : id[0];
                    friendIDs.add(friendID); // Exclude friends
                }
                friendIDs.add(friendsViewModel.getUserID()); // Exclude current user
                Task task = friendsViewModel.getUsers(friendIDs).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshotsUsers) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshotsUsers) {
                            String username = documentSnapshot.get("username")== null ? "unknown" : documentSnapshot.get("username").toString();
                            usersListAdapter.addUser(documentSnapshot.getId(), username);
                        }
                    }
                });
            }
        });
    }

    private void showSearch(){
        binding.searchViewFriends.setVisibility(View.VISIBLE);
        binding.btnAmics.setVisibility(View.GONE);
        binding.name.setVisibility(View.INVISIBLE);
    }

    private void hideSearch(){
        binding.searchViewFriends.setVisibility(View.GONE);
        binding.btnAmics.setVisibility(View.VISIBLE);
        binding.name.setVisibility(View.VISIBLE);
    }

    public void sendRequest(View view, String toID){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enviar sol·licitud d'amistad");
        builder.setNegativeButton("NO",null);
        builder.setPositiveButton("SÍ",new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                friendsViewModel.sendRequest(toID);
                setList();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}