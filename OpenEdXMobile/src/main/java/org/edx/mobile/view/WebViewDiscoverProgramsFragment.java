package org.edx.mobile.view;

import org.edx.mobile.R;

public class WebViewDiscoverProgramsFragment extends WebViewDiscoverFragment {
    @Override
    protected boolean shouldShowSubjectDiscovery() {
        return false;
    }

    @Override
    protected String getSearchUrl() {
        return environment.getConfig().getDiscoveryConfig().getProgramDiscoveryConfig().getBaseUrl();
    }

    @Override
    protected int getQueryHint() {
        return R.string.search_for_programs;
    }
}
