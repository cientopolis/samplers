package org.cientopolis.samplers.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.DeadEvent;
import com.squareup.otto.Subscribe;

import org.cientopolis.samplers.R;

import org.cientopolis.samplers.authentication.AuthenticationManager;
import org.cientopolis.samplers.authentication.LoginActivity;
import org.cientopolis.samplers.authentication.User;
import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.LoginEvent;
import org.cientopolis.samplers.framework.Workflow;
import org.cientopolis.samplers.ui.samples_list.SamplesListActivity;


public abstract class SamplersMainActivity extends Activity {
    protected TextView lb_main_welcome_message;
    protected Button bt_main_login;
    protected TextView lb_main_user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplers_main);

        lb_main_welcome_message = (TextView) findViewById(R.id.lb_main_welcome_message);
        bt_main_login = (Button) findViewById(R.id.bt_main_login);
        lb_main_user_name = (TextView) findViewById(R.id.lb_main_user_name);

        // Register to the bus to receive messages
        BusProvider.getInstance().register(this);

        Log.e("SamplersMainActivity","onCreate");
    }

    @Override
    public void onDestroy () {
        Log.e("SamplersMainActivity","onDestroy");

        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AuthenticationManager.isAuthenticationEnabled()) {
            updateUserUI(AuthenticationManager.getUser(getApplicationContext()));
        }
        else {
            lb_main_user_name.setVisibility(View.INVISIBLE);
            bt_main_login.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ErrorMessaging.showErrorMessage(this, "Settings");
            return true;
        }
        else if (id == R.id.action_samples) {
            startSamplesListActivity();
            return true;
        }
        else if (id == R.id.action_help) {
            startHelpActivity();
            return true;
        }
        else if (id == R.id.action_logout) {
            logout();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void takeSample(View view){

        startTakeSampleActivity();
    }

    protected void startTakeSampleActivity() {
        Intent intent = new Intent(this, getTakeSampleActivityClass());

        Workflow workflow = getWorkflow();
        intent.putExtra(getTakeSampleActivityWorkflowParamName(), workflow);

        startActivity(intent);
    }

    protected Class getTakeSampleActivityClass() {
        return TakeSampleActivity.class;
    }

    protected String getTakeSampleActivityWorkflowParamName() {
        return TakeSampleActivity.EXTRA_WORKFLOW;
    }

    protected abstract Workflow getWorkflow();

    protected void startSamplesListActivity() {
        Intent intent = new Intent(this, SamplesListActivity.class);
        startActivity(intent);
    }

    protected void startHelpActivity() {
        Integer help_resource_id = getMainHelpResourceId();

        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra(HelpActivity.HELP_RESOURCE_ID, help_resource_id);
        startActivity(intent);
    }

    protected abstract Integer getMainHelpResourceId();

    protected void logout() {
        AuthenticationManager.logout(getApplicationContext());
        BusProvider.getInstance().post(new LoginEvent(null));
    }

    public void login(View view){

        startLoginActivity();
    }

    protected void startLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        // UPDATE UI
        Log.e("onLoginEvent","user:"+loginEvent.user);

        updateUserUI(loginEvent.user);
    }

    @Subscribe
    public void onDeadEvent(DeadEvent event) {
        // DEAD EVENT
        Log.e("onDeadEvent","event:"+event.toString());
    }

    private void updateUserUI(User user) {
        if (user != null) {
            lb_main_user_name.setText(user.getUserName());
            lb_main_user_name.setVisibility(View.VISIBLE);
            bt_main_login.setVisibility(View.INVISIBLE);
        }
        else { // no user logged
            lb_main_user_name.setVisibility(View.INVISIBLE);
            bt_main_login.setVisibility(View.VISIBLE);
        }

    }

}
