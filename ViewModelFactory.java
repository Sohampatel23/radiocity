package com.app.radiocity.ViewModel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T> implements ViewModelProvider.Factory {
    private Activity activity;
    private T binding;

    public ViewModelFactory(Activity activity, T binding) {
        this.activity = activity;
        this.binding = binding;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(activity, binding);
        }
        else if(modelClass == LoginViewModel.class){
            return (T) new LoginViewModel(activity, binding);
        }
        else if(modelClass == ForgotViewModel.class){
            return (T) new ForgotViewModel(activity, binding);
        }

        else if(modelClass == HomeViewModel.class){
            return (T) new HomeViewModel(activity, binding);
        }

        else if(modelClass == AddCafViewModel.class){
            return (T) new AddCafViewModel(activity, binding);
        }
        else if(modelClass == FilePreviewModel.class){
            return (T) new FilePreviewModel(activity, binding);
        }

        else if(modelClass == QueryViewModel.class){
            return (T) new QueryViewModel(activity, binding);
        }
        else if(modelClass == ProfileViewModel.class){
            return (T) new ProfileViewModel(activity, binding);
        }
        else if(modelClass == ProfileDetailViewModel.class){
            return (T) new ProfileDetailViewModel(activity, binding);
        }
        else if(modelClass == CreateCAFViewModel.class){
            return (T) new CreateCAFViewModel(activity, binding);
        }
        else {
            return null;
        }
    }
}
