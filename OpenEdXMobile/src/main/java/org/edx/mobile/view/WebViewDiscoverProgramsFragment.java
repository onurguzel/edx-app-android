package org.edx.mobile.view;

public class WebViewDiscoverProgramsFragment extends WebViewDiscoverCoursesFragment {
    @Override
    protected boolean shouldShowSubjectDiscovery() {
        return false;
    }

    @Override
    protected String getSearchUrl() {
        return environment.getConfig().getProgramDiscoveryConfig().getProgramSearchUrl();
    }
}
