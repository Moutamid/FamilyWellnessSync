package com.moutimid.familywellness.Helper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moutimid.familywellness.user.model.GPTModel;

public class GPTViewModel extends ViewModel {
    MutableLiveData<GPTModel> message = new MutableLiveData<>();

    MutableLiveData<GPTModel> getMesajlar() {
        if (message == null) {
            message = new MutableLiveData<>();
        }
        return message;
    }
}
