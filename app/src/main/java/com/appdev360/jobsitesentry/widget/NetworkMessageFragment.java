package com.appdev360.jobsitesentry.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.appdev360.jobsitesentry.R;


public class NetworkMessageFragment extends DialogFragment {

    private static final String ARGS_IS_FROM_FRAGMENT = "isfromfragment";

    // click listener
    public interface MessageFragmentActionButtonClickListener {

        void onPositiveClicked();

        void onNegativeClicked();
    }

    //--------------

    private String color;

    private View rootView;

    private MessageFragmentActionButtonClickListener mListener;


    public static NetworkMessageFragment newInstance(Fragment targetFragment) {
        NetworkMessageFragment fragment = new NetworkMessageFragment();

        if (fragment != null)
            fragment.setTargetFragment(targetFragment, 0);

        return fragment;
    }

    public NetworkMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialog_network_message_screen, container, false);

        init();

        return rootView;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() != null)
                mListener = (MessageFragmentActionButtonClickListener) getTargetFragment();
            else
                mListener = (MessageFragmentActionButtonClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("activity or fragment must implement ActionButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    // --- helpers ---

    private void init() {
        rootView.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onPositiveClicked();
                dismiss();
            }
        });

        rootView.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onNegativeClicked();
                dismiss();
            }
        });

    }


}
