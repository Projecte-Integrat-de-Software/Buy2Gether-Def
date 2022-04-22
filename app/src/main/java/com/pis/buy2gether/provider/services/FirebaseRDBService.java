package com.pis.buy2gether.provider.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.List;

/* Write or read data from Firebase Database */
public enum FirebaseRDBService {
    INSTANCE;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void save(String collectionPath, String doc, HashMap data){
        db.collection(collectionPath).document(doc).set(data);
    }

    private void save(String collectionPath, String doc, String data){
        db.collection(collectionPath).document(doc).set(data);
    }

    public void saveAddress(String doc, HashMap data){
        db.collection("users").document(doc).collection("Addresses").document(data.get("Address name").toString()).set(data);
    }


    public void saveGroup(String doc, HashMap data){
        db.collection("Groups").document(doc).set(data);
    }

    public void delete(String collectionPath, String doc){
        db.collection(collectionPath).document(doc).delete();
    }

    public void deleteAddress(String user, String doc){
        db.collection("users").document(user).collection("Addresses").document(doc).delete();
    }

    /**
     * actualitza un sol parametre de users en base de dades
     * @param doc
     * @param field
     * @param value
     */
    public void update(String doc,String field, String value){
        db.collection("users").document(doc).update(field,value);
    }

    public Task<DocumentSnapshot> get(String collectionPath, String doc){
        return db.collection(collectionPath).document(doc).get();
    }


    public Task<DocumentSnapshot> getUserByID(String id){
        return db.collection("users").document(id).get();
    }

    public Task<QuerySnapshot> getUsers(List friends){
        if(!friends.isEmpty()){ return db.collection("users").whereNotIn(FieldPath.documentId(),friends).get(); }
        return db.collection("users").get();
    }

    public Task<DocumentSnapshot> getGroup(String id){
        return db.collection("Groups").document(id).get();
    }

    public Task<QuerySnapshot> getFriends(String userID){
        Query q = db.collection("Friendships").whereNotEqualTo(userID,null);
        return q.get();
    }

    public Task<QuerySnapshot> getAddresses(String doc){
        Query q = db.collection("users").document(doc).collection("Addresses");
        return q.get();
    }

    public void SaveInviteDB(String uuidString, HashMap inviteInfo) {
        save("Invites",uuidString,inviteInfo);
    }

    public void SaveFriendRequestDB(String uuidString, HashMap inviteInfo) {
        save("Requests",uuidString,inviteInfo);
    }

    public void addFriend(String uuidString, HashMap<String,String> friendship) {
        save("Friendships",uuidString,friendship);
    }

    public void joinGroup(String uuidString, HashMap<String, String> membership) {
        save("Memberships",uuidString,membership);
    }

    public Task<QuerySnapshot> getFriendRequests(String user) {
        Query q = db.collection("Requests").whereEqualTo("toID",user);
        return q.get();
    }

    public Task<QuerySnapshot> getGroupInvites(String user) {
        Query q = db.collection("Invites").whereEqualTo("UserID",user);
        return q.get();
    }
}
