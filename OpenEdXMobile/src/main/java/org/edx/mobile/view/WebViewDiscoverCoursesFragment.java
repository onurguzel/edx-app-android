package org.edx.mobile.view;

import android.content.res.Configuration;

import org.edx.mobile.R;

public class WebViewDiscoverCoursesFragment extends WebViewDiscoverFragment {
    protected boolean shouldShowSubjectDiscovery() {
        return getActivity() instanceof MainDashboardActivity &&
                environment.getConfig().getDiscoveryConfig().getCourseDiscoveryConfig().isSubjectFilterEnabled() &&
                getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE;
    }

    protected String getSearchUrl() {
        return environment.getConfig().getDiscoveryConfig().getCourseDiscoveryConfig().getBaseUrl();
    }

    @Override
    protected int getQueryHint() {
        return R.string.search_for_courses;
    }
}
