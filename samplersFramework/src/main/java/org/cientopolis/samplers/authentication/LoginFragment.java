package org.cientopolis.samplers.authentication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;


public abstract class LoginFragment extends Fragment {

    protected LoginFragmentInteractionListener mListener;


    private void myOnAttach(Context context) {
        /*
         * ATTENTION:
         * this method is called twice on API 23-25 because both onAttach(Activity) and onAttach(Context) are executed
         * Don't put creation code here (e.g. new SomeClass())
         */

        if (context instanceof LoginFragmentInteractionListener) {
            mListener = (LoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginFragmentInteractionListener");
        }
    }

    @SuppressWarnings("deprecation") // This method is needed when running on API Levels < 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myOnAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myOnAttach(context);
    }




}
