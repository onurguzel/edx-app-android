package org.edx.mobile.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.edx.mobile.base.BaseSingleFragmentActivity;
import org.edx.mobile.base.WebViewProgramInfoFragment;

public class ProgramInfoActivity extends BaseSingleFragmentActivity {
    public static final String EXTRA_PATH_ID = "path_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Implement the analytics for program info screen
//        environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.COURSE_INFO_SCREEN);
    }

    @Override
    public Fragment getFirstFragment() {
        final WebViewProgramInfoFragment fragment = new WebViewProgramInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}
