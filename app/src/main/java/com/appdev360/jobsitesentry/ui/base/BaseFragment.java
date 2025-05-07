package com.appdev360.jobsitesentry.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.injection.component.ActivityComponent;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.util.GeneralUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {
    public static final String ARGS_INSTANCE = "co.appdev.boilerplate.argsInstance";

    public View parent;
    int mInt = 0;

    @Inject
    public DataManager dataManager;
    public String userToken;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityComponent component = ((BaseActivity) getActivity()).activityComponent();
        if (component == null) {
            GeneralUtils.restartApp(getActivity());
            return;
        }
        component.inject(this);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        userToken = dataManager.getPreferencesHelper().getToken();

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(getLayoutId(), container, false);
        parent.setClickable(true);
        parent.requestFocus();
        ButterKnife.bind(this, parent);
        initViews(parent,savedInstanceState);
        updateFragmentReference();
        return parent;
    }


    public abstract void initViews(View parentView , Bundle saveInstanceState);

    public abstract int getLayoutId();

    public abstract void updateFragmentReference();

    public abstract void updateActionState(boolean action, View v);


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void onTokenExpire(Error error,DataManager dataManager) {
        // MyToast.showMessage(getActivity(), error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }


}
