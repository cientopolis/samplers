package org.cientopolis.samplers.authentication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.cientopolis.samplers.R;

public class LoginActivity extends Activity implements LoginFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples_list);

        if (savedInstanceState == null) {
            try {
                LoginFragment loginFragment = AuthenticationManager.getLoginFragmentClass().newInstance();

                getFragmentManager().beginTransaction()
                        .add(R.id.activity_samples_list, loginFragment).commit();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error while instantiating LoginFragment");
            }
        }
    }

    @Override
    public void onLogin(@Nullable User user) {
        this.finish();

    }
}
