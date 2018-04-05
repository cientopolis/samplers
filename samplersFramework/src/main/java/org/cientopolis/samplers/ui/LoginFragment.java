package org.cientopolis.samplers.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.authentication.AuthenticationManager;
import org.cientopolis.samplers.authentication.GoogleUser;
import org.cientopolis.samplers.authentication.User;

/**
 * Created by Xavier on 31/03/2018.
 */

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 111;

    private LoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_login, container, false);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        Button bt_skip_login = (Button) rootView.findViewById(R.id.bt_skip_login);
        // if authentication is required, skip skip_login button is invisible
        if (!AuthenticationManager.isAuthenticationOptional())
            bt_skip_login.setVisibility(View.INVISIBLE);
        else
            bt_skip_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onLogin(null);
                }
            });

        return rootView;
    }

    private void signIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Starting the intent prompts the user to select a Google account to sign in with
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        User user;
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully.
            user = new GoogleUser(account.getDisplayName(),account.getId());
            AuthenticationManager.login(user);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginFragment", "signInResult:failed code=" + e.getStatusCode());
            user = null;
        }

        mListener.onLogin(user);
    }

    private void myOnAttach(Context context) {
        /*
        * ATENTION:
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


    public interface LoginFragmentInteractionListener {
        void onLogin(@Nullable User user);
    }

}
