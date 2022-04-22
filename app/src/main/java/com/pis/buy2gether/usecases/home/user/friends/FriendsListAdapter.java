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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pis.buy2gether.R;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private LinkedHashMap<String,String> usernameFriend;
    private LinkedHashMap<String,String> idFriend;
    private LinkedHashMap<String,String> pfpFriend;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    int row_idx = -1;

    // data is passed into the constructor
    FriendsListAdapter(Context context, ItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.usernameFriend = new LinkedHashMap<>();
        this.idFriend = new LinkedHashMap<>();
        this.pfpFriend = new LinkedHashMap<>();
        this.mClickListener = itemClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String id = (String) usernameFriend.keySet().toArray()[position];

        holder.myTextView.setText(usernameFriend.get(id));
        // Modifiquem la foto de perfil (per implementar)
        setUserPfp(usernameFriend.get(id),holder);
        //holder.acceptButton.setVisibility(View.INVISIBLE);
        holder.selectButton.setVisibility(View.VISIBLE);
    }

    public void setUserPfp(String friend, @NonNull @NotNull ViewHolder holder){
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("profileImages/"+ idFriend.get(friend) + ".jpeg");
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(bm!=null) holder.pfp.setImageBitmap(bm);
            }
        });
    }

    public void addFriend(String friendshipID, String friendID, String friendName){
        usernameFriend.put(friendshipID,friendName);
        idFriend.put(friendName,friendID);
        notifyItemInserted(usernameFriend.size());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return usernameFriend.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ShapeableImageView pfp;
        RadioButton selectButton;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.name);
            pfp = itemView.findViewById(R.id.img_friend);
            selectButton = itemView.findViewById(R.id.friend_SEL);
            selectButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == -1){
                selectButton.setChecked(!selectButton.isChecked());
                // change item background color
                if(selectButton.isChecked()){
                    itemView.setBackgroundColor(Color.parseColor("#E7D3F4"));
                    selectButton.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                    myTextView.setTextColor(Color.BLACK);
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {itemView.setBackground(itemView.getForeground());}
                    else{itemView.setBackgroundColor(Color.TRANSPARENT);}
                    selectButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    myTextView.setTextColor(Color.parseColor("#707070"));
                }
            }

            if(view.getId() == R.id.friend_SEL){
                if (mClickListener != null) mClickListener.onItemClick(view, (String) usernameFriend.keySet().toArray()[getAdapterPosition()]);
            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return usernameFriend.get(id);
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
        void onItemClick(View view, String friendshipID);
        void onClick(View view);
    }
}