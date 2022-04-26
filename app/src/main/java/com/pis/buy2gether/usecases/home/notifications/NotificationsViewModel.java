package com.pis.buy2gether.usecases.home.notifications;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pis.buy2gether.model.session.Session_to_refactor;

import java.util.HashMap;

public class NotificationsViewModel extends ViewModel {

    public void addFriend(String id1,String id2){
        HashMap<String,String> friendShip = new HashMap<>();
        friendShip.put(id1,id1);
        friendShip.put(id2,id2);
        Session_to_refactor.INSTANCE.addFriendship(friendShip);
    }

    public void removeGroupInvite(String id){
        Session_to_refactor.INSTANCE.deleteGroupInvite(id);
    }

    public String getUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public void removeFriendRequest(String id){
        Session_to_refactor.INSTANCE.deleteFriendRequest(id);
    }

    public void joinGroup(String user, String extraID) {
        HashMap<String,String> membership = new HashMap<>();
        membership.put("UserID",user);
        membership.put("GroupID",extraID);
        Session_to_refactor.INSTANCE.joinGroup(membership);
    }

    public Task<DocumentSnapshot> getGroup(String id) {
        return Session_to_refactor.INSTANCE.getGroup(id);
    }

    public Task<DocumentSnapshot> getUserName(String id) {
        return Session_to_refactor.INSTANCE.getUserByID(id);
    }

    public Task<QuerySnapshot> getFriendRequests() {
        return Session_to_refactor.INSTANCE.getFriendRequests(getUser());
    }
    public Task<QuerySnapshot> getGroupInvites() {
        return Session_to_refactor.INSTANCE.getGroupInvites(getUser());
    }
}