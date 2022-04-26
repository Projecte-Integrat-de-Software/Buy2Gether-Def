package com.pis.buy2gether.usecases.home.user.friends;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pis.buy2gether.model.session.Session_to_refactor;

import java.util.HashMap;
import java.util.List;

public class FriendsViewModel extends ViewModel {

    public Task getFriends(){
        return Session_to_refactor.INSTANCE.getFriendsDB(getUserID());
    }

    public Task getUsers(List friends){
        return Session_to_refactor.INSTANCE.getUsers(friends);
    }

    public String getUserID(){
        String userID = "unknown";
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return userID;
    }

    public Task<DocumentSnapshot> getUserName(String id) {
        return Session_to_refactor.INSTANCE.getUserByID(id);
    }
    /*public Task<DocumentSnapshot> getFriendshipID(String friendID) {
        return Session.INSTANCE.getFriendshipID(getUserID(), friendID);
    }*/
    public void deleteFriend(String id){
        Session_to_refactor.INSTANCE.deleteFriend(id);
    }

    public void sendRequest(String toID) {
        HashMap inviteInfo = new HashMap();
        inviteInfo.put("fromID",getUserID());
        inviteInfo.put("toID",toID);

        Session_to_refactor.INSTANCE.CreateFriendRequest(inviteInfo);
    }
}