package com.pis.buy2gether.usecases.home.home.product_view.group.creation;

import android.content.Context;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pis.buy2gether.model.session.Session;

import java.util.HashMap;

public class GroupCreationViewModel extends ViewModel {


    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(1000);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    public String createGroupDB(String productName, String productURL, String type, int userLimit, double originalPrice){
        HashMap groupInfo = new HashMap();
        groupInfo.put("Product Name",productName);
        groupInfo.put("Product URL",productURL);
        groupInfo.put("Type",type);
        groupInfo.put("User Limit",userLimit);
        groupInfo.put("Original Price",originalPrice);

        return Session.INSTANCE.CreateGroupDB(groupInfo);
    }

}