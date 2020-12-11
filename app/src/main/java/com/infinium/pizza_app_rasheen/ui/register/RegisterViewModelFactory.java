package com.infinium.pizza_app_rasheen.ui.register;

        import androidx.annotation.NonNull;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.ViewModelProvider;

        import com.infinium.pizza_app_rasheen.data.RegisterRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(RegisterRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}