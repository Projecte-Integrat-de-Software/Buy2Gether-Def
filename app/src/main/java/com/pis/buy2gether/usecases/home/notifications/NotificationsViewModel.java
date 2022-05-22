package com.pis.buy2gether.usecases.home.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pis.buy2gether.model.domain.data.NotificationData;
import com.pis.buy2gether.model.domain.pojo.Grup.Grup;
import com.pis.buy2gether.model.domain.pojo.Notificacions;
import com.pis.buy2gether.model.session.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Notificacions>> notificacions;

    public NotificationsViewModel() {
        notificacions = new MutableLiveData<>();
        init();
    }

    private void init() {
        notificacions = NotificationData.INSTANCE.getData();
    }

    public MutableLiveData<ArrayList<Notificacions>> getNotificacions() {
        return notificacions;
    }

    /**
     * save notification to data base
     * @return
     */
    public Task<Void> saveNotification(NotiType notiType, String idNotification, String fromUsername, String groupname, String fromId){
        HashMap notificationInfo = new HashMap<>();
        notificationInfo.put("notiType", notiType);
        notificationInfo.put("idNotification", idNotification);
        notificationInfo.put("fromUsername",fromUsername);
        notificationInfo.put("groupname",groupname);
        notificationInfo.put("fromId",fromId);
        //should we create the notification class here???
        //Notificacions notificacions = new Notificacions(notiType,idNotification,fromUsername,groupname,fromId);

        return NotificationData.INSTANCE.saveNotification(notificationInfo);
    }

    /**
     * delete notification from data base
     * @param id
     */
    public Task<Void> deleteNotification(String id){
        return NotificationData.INSTANCE.deleteNotification(id);
    }

    public void addFriend(String id1,String id2){
        HashMap<String,String> friendShip = new HashMap<>();
        friendShip.put(id1,id1);
        friendShip.put(id2,id2);
        Session.INSTANCE.addFriendship(friendShip);
    }

    public void joinGroup(String user, String extraID) {
        HashMap<String,String> membership = new HashMap<>();
        membership.put("UserID",user);
        membership.put("GroupID",extraID);
        Session.INSTANCE.joinGroup(membership);
    }

    public String getUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Task<DocumentSnapshot> getGroup(String id) {
        return Session.INSTANCE.getGroup(id);
    }

    public Task<DocumentSnapshot> getUserName(String id) {
        return Session.INSTANCE.getUserByID(id);
    }

    public Task<QuerySnapshot> getFriendRequests() {
        return NotificationData.INSTANCE.getFriendRequests();
    }
    public Task<QuerySnapshot> getGroupInvites() {
        return NotificationData.INSTANCE.getGroupInvites();
    }
}