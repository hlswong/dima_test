package com.sahk.earlyliteracy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sahk.earlyliteracy.applications.MyFragment;
import com.sahk.earlyliteracy.fragments.SplashFragment;

public class MainActivity extends BaseActivity implements MyFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(Bundle savedInstanceState) {
        addFragment(SplashFragment.newInstance());
    }

    @Override
    public BaseActivity getContainer() {
        return this;
    }

    @Override
    public void checkPermission(int permissionType) {

    }
}