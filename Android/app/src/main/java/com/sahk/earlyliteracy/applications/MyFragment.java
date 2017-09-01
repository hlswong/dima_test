package com.sahk.earlyliteracy.applications;

import com.sahk.earlyliteracy.BaseActivity;

public interface MyFragment {

    BaseActivity getContainer();

    void setToolbarMode(int status);

    void checkPermission(int permissionType);
}