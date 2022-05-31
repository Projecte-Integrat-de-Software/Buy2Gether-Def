package com.pis.buy2gether.usecases.home.user.friends;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pis.buy2gether.R;
import com.pis.buy2gether.databinding.FragmentFriendsBinding;
import com.pis.buy2gether.model.domain.pojo.User;
import com.pis.buy2gether.model.session.Session;
import com.pis.buy2gether.provider.ProviderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsViewModel extends ViewModel{
    private FragmentFriendsBinding binding;
    private RecyclerView recyclerView;
    android.widget.SearchView searchView;

    private Session session;
    private Context context;

    /* --- ListAdapters --- */
    private FriendsListAdapter friendsListAdapter;
    private UsersListAdapter usersListAdapter;

    /***
     * Constructor
     * @param context context
     * @param friendsListAdapter listAdapter of friends
     * @param usersListAdapter listAdapter of users
     * @param binding binding
     */
    FriendsViewModel(Context context, FriendsListAdapter friendsListAdapter, UsersListAdapter usersListAdapter, FragmentFriendsBinding binding){
        this.session = Session.INSTANCE;
        this.context = context;
        this.friendsListAdapter = friendsListAdapter;
        this.usersListAdapter = usersListAdapter;
        this.binding = binding;
        this.recyclerView = binding.friendsList;
    }

    /***
     * Set up interface
     */
    public void setup(){
        setupUserInfo();
        setUserPfp();
    }

    /***
     * Display current user information at the top
     */
    private void setupUserInfo(){
        String provider = session.getDataSession(context,"provider");

        if(ProviderType.valueOf(provider) != ProviderType.GUEST) {
            String currentUser = session.getCurrentUserID();
            session.getUserByID(currentUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.get("username").toString() != null) binding.txtUser.setText(documentSnapshot.get("username").toString());
                    binding.txtDesc.setText(documentSnapshot.get("email").toString());
                }
            });
        }
    }

    /***
     * Sets de profile image. TODO: Cambiar a ImageData cuando funcione.
     */
    private void setUserPfp(){
        session.getCurrentUserPfpImageRef().getBytes(1024 * 1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imgPfp.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context,"Error al carregar l'imatge de perfil\n"+exception,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /***
     * Returns friends of the current user
     * @return Task
     */
    public Task getFriends(){
        return Session.INSTANCE.getFriendsDB(getUserID());
    }

    /***
     * Returns all users without including friends nor current user
     * @param friends Friends of the current user
     * @return Task
     */
    public Task getUsers(List friends){
        return Session.INSTANCE.getUsers(friends);
    }

    /***
     * Returns current user ID
     * @return String of current user ID
     */
    public String getUserID(){
        String userID = "unknown";
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return userID;
    }

    /***
     * Returns username given an ID
     * @param id ID
     * @return Task
     */
    public Task<DocumentSnapshot> getUserName(String id) {
        return Session.INSTANCE.getUserByID(id);
    }

    /***
     * Delete a friend
     * @param id ID of the friend
     */
    public void deleteFriend(String id){
        Session.INSTANCE.deleteFriend(id);
        Toast.makeText(context, "Amic eliminat", Toast.LENGTH_SHORT).show();
        setList();
    }

    /***
     * Send friend request
     * @param toID ID of user
     */
    public void request(String toID) {
        HashMap inviteInfo = new HashMap();
        inviteInfo.put("fromID",getUserID());
        inviteInfo.put("toID",toID);

        Session.INSTANCE.CreateFriendRequest(inviteInfo);
    }

    public void sendRequest(String toID){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enviar sol·licitud d'amistad");
        builder.setNegativeButton("NO",null);
        builder.setPositiveButton("SÍ",new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                request(toID);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /***
     * Display list of friends
     */
    public void setList(){
        friendsListAdapter.clear();

        recyclerView.setAdapter(friendsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        hideSearch();

        Task task = getFriends().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String[] id = documentSnapshot.getData().keySet().toArray(new String[0]);
                    String friendID = id[0].equals(getUserID()) ? id[1] : id[0];
                    Task<DocumentSnapshot> task = getUserName(friendID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot2) {
                            User user = info(documentSnapshot2.getData(), documentSnapshot);
                            friendsListAdapter.addFriend(user,documentSnapshot.getId());
                        }
                    });
                }
            }
        });
    }

    /***
     * Display list of users
     */
    public void setListUsers(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(usersListAdapter);

        showSearch();

        usersListAdapter.clear();
        /* Generate list of users available to send Friend request */
        List friendIDs = new ArrayList<>();
        Task t = getFriends().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String[] id = documentSnapshot.getData().keySet().toArray(new String[0]);
                    String friendID = id[0].equals(getUserID()) ? id[1] : id[0];
                    friendIDs.add(friendID); // Exclude friends
                }
                friendIDs.add(getUserID()); // Exclude current user
                Task task = getUsers(friendIDs).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshotsUsers) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshotsUsers) {
                            User user = info(documentSnapshot.getData(), documentSnapshot);
                            usersListAdapter.addUser(user);
                        }
                    }
                });
            }
        });
    }

    /***
     * Auxiliar function to generate new users
     * @param data info about the user
     * @param documentSnapshot documentSnapshot
     * @return new User
     */
    public User info(Map<String, Object> data, QueryDocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getId();
        User user = new User();
        Map<String, Object> info = data;

        user.setId(id);
        user.setUsername((String) info.get("username"));
        user.setEmail((String) info.get("email"));
        user.setProvider((String) info.get("provider"));
        return user;
    }

    /* ---- Functions for Query changes ---- */

    /***
     * User presses enter in searchbar
     * @param query input
     */
    public void queryTextSubmit(String query){
        /* User prem enter */
        searchView.setQuery(query,false);
        searchView.clearFocus();
        usersListAdapter.getFilter().filter(query);
    }

    /***
     * User enters new character in searchbar
     * @param newText input
     */
    public void queryTextChange(String newText){
        usersListAdapter.getFilter().filter(newText);
    }

    /* ---- Show/Hide User search bar ---- */

    /***
     * Set visibility of searchView to visible
     */
    public void showSearch(){
        binding.searchViewFriends.setVisibility(View.VISIBLE);
        binding.btnAmics.setVisibility(View.GONE);
        binding.name.setVisibility(View.INVISIBLE);
    }

    /***
     * Set visibility of searchView to invisible
     */
    public void hideSearch(){
        binding.searchViewFriends.setVisibility(View.GONE);
        binding.btnAmics.setVisibility(View.VISIBLE);
        binding.name.setVisibility(View.VISIBLE);
    }
}