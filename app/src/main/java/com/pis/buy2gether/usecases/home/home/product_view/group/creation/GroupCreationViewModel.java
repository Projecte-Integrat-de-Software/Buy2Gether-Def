package com.pis.buy2gether.usecases.home.home.product_view.group.creation;

import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pis.buy2gether.model.session.Session_to_refactor;

import java.util.HashMap;

public class GroupCreationViewModel extends ViewModel {


    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(1000);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    public String createGroupDB(String productName, String productURL, String type, int userLimit, double originalPrice, int groupVisibility, String adminUser){
        HashMap groupInfo = new HashMap();
        groupInfo.put("Product Name",productName);
        groupInfo.put("Product URL",productURL);
        groupInfo.put("Type",type);
        groupInfo.put("User Limit",userLimit);
        groupInfo.put("Original Price",originalPrice);
        groupInfo.put("Group Visibility",groupVisibility);
        groupInfo.put("Admin User",adminUser);

        return Session_to_refactor.INSTANCE.CreateGroupDB(groupInfo);
    }

    public void SendInvite(String userID, String groupID) {
        HashMap inviteInfo = new HashMap();
        inviteInfo.put("UserID",userID);
        inviteInfo.put("GroupID",groupID);
        inviteInfo.put("fromUser",getUser());

        Session_to_refactor.INSTANCE.CreateInvite(inviteInfo);
    }
    public Task<DocumentSnapshot> getUserName(String id){
        return Session_to_refactor.INSTANCE.getUserByID(id);
    }

    public Task<QuerySnapshot> getFriends(){
        return Session_to_refactor.INSTANCE.getFriendsDB(getUser());
    }
    public String getUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}