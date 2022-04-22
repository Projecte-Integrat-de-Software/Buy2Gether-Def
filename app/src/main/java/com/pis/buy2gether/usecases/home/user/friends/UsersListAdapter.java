package com.pis.buy2gether.usecases.home.user.friends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pis.buy2gether.R;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> implements Filterable {

    private LinkedHashMap<String,String> user;
    private LinkedHashMap<String,String> allUsers;
    private LinkedHashMap<String,String> pfp;
    private ArrayList<String> users;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    int row_idx = -1;

    // data is passed into the constructor
    UsersListAdapter(Context context, ItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.user= new LinkedHashMap<>();
        this.allUsers= new LinkedHashMap<>();
        this.pfp = new LinkedHashMap<>();
        this.mClickListener = itemClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String id = (String) user.keySet().toArray()[position];

        holder.myTextView.setText(user.get(id));
        // Modifiquem la foto de perfil (per implementar)
        setUserPfp(id,holder);

        //holder.acceptButton.setVisibility(View.INVISIBLE);
        holder.selectButton.setVisibility(View.VISIBLE);
    }

    public void setUserPfp(String id, @NonNull @NotNull UsersListAdapter.ViewHolder holder){
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("profileImages/"+ id + ".jpeg");
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(bm!=null) holder.pfp.setImageBitmap(bm);
            }
        });
    }

    public void addUser(String userID, String username){
        user.put(userID,username);
        allUsers.put(userID,username);
        notifyItemInserted(user.size());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return user.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                LinkedHashMap<String,String> filteredUsernameList = new LinkedHashMap<String,String>();
                LinkedHashMap<String,String> filteredPfpList = new LinkedHashMap<String,String>();
                if (charSequence.toString().isEmpty()) {
                    filteredUsernameList.putAll(allUsers);
                } else {
                    for (String idUser : allUsers.keySet()) {
                        if (allUsers.get(idUser).toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredUsernameList.put(idUser,allUsers.get(idUser));
                            filteredPfpList.put(idUser,pfp.get(idUser));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUsernameList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                user.clear();
                user.putAll((Map<? extends String, ? extends String>) filterResults.values);

                notifyDataSetChanged();
            }
        };
        return filter;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        RadioButton selectButton;
        ShapeableImageView pfp;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.name);
            pfp = itemView.findViewById(R.id.img_friend);
            selectButton = itemView.findViewById(R.id.friend_SEL);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == -1){
                selectButton.setChecked(!selectButton.isChecked());
                // change item background color
                if(selectButton.isChecked()){
                    itemView.setBackgroundColor(Color.parseColor("#E7D3F4"));
                    selectButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    myTextView.setTextColor(Color.BLACK);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {itemView.setBackground(itemView.getForeground());}
                    else{itemView.setBackgroundColor(Color.TRANSPARENT);}
                    selectButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    myTextView.setTextColor(Color.parseColor("#707070"));
                }
            }
            if (mClickListener != null) mClickListener.sendRequest(view, (String) user.keySet().toArray()[getAdapterPosition()]);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return user.get(id);
    }

    // convenience method for getting all the usernames
    List<String> getList(){
        return  Arrays.asList(user.values().toString());
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /*public void swipe(ViewHolder viewHolder) {
        usernameFriend.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());
    }*/
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void sendRequest(View view, String userID);
        void onClick(View view);
    }
}