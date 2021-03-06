package com.pis.buy2gether.model.domain.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.pis.buy2gether.model.domain.pojo.Notificacions;
import com.pis.buy2gether.provider.services.FirebaseFactory;
import com.pis.buy2gether.provider.services.FirebaseNotification;
import com.pis.buy2gether.usecases.home.notifications.NotiType;

import java.util.*;

public enum NotificationData {
    INSTANCE;

    private FirebaseNotification firebaseNotification = (FirebaseNotification) FirebaseFactory.INSTANCE.getFirebase("FirebaseNotification");
    private MutableLiveData<ArrayList<Notificacions>> data = new MutableLiveData<ArrayList<Notificacions>>();

    public MutableLiveData<ArrayList<Notificacions>> getData() {
        updateGrups();
        return data;
    }

    public void setData(ArrayList<Notificacions> data) {
        this.data.setValue(data);
    }

    public void updateGrups() {
        Task<QuerySnapshot> data_0 = firebaseNotification.getFriendRequests();
        Task<QuerySnapshot> data_1 = firebaseNotification.getGroupInvites();

        Collection<Task<QuerySnapshot>> tasks = new ArrayList<>();
        tasks.add(data_0);
        tasks.add(data_1);
        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                ArrayList<Notificacions> notificacions = new ArrayList<>();

                // add every notifications whatever the type of notification
                for (Task<QuerySnapshot> querySnapshot : tasks) {
                    QuerySnapshot result = querySnapshot.getResult();

                    for (int i = 0; i < result.size(); i++) {
                        //get information from document
                        Map<String, Object> info = result.getDocuments().get(i).getData();
                        //create a notification from database and add to our notifications list
                        Notificacions notificacions1 = new Notificacions();
                        notificacions1.setIdNotificacion(result.getDocuments().get(i).getId());
                        notificacions1.setFromID((String) info.get("fromID"));
                        notificacions1.setNotiType(NotiType.valueOf((String) info.get("notiType")));
                        notificacions1.setFromUsername((String) info.get("FromUsername"));
                        notificacions1.setGroupName((String) info.get("groupname"));
                        notificacions.add(notificacions1);
                        //notificacions.add(result.getDocuments().get(i).toObject(Notificacions.class));
                        Log.e("NotificationData", " we getNotification!!!!!: " + result.getDocuments().get(i).toObject(Notificacions.class).toString());
                    }
                }
                data.setValue(notificacions);
                data.postValue(notificacions);
            }
        });
    }

    public void saveFriendRequest(Notificacions notificacions) {
        firebaseNotification.saveFriendRequest(notificacions);
    }

    /**
     * save notification data in data base
     * @param data
     */
    public Task<Void> saveNotification(HashMap data){
        return firebaseNotification.saveNotification(data);
    }

    public Task<Void> deleteNotification(String id) {
        return firebaseNotification.deleteNotification(id);
    }

    public Task<QuerySnapshot> getFriendRequests() {
        return firebaseNotification.getFriendRequests();
    }

    public Task<QuerySnapshot> getGroupInvites() {
        return firebaseNotification.getGroupInvites();
    }

}

