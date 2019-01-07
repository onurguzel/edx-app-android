package org.edx.mobile.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.inject.Inject;

import org.edx.mobile.R;
import org.edx.mobile.base.BaseFragment;
import org.edx.mobile.core.IEdxEnvironment;
import org.edx.mobile.databinding.FragmentMainDiscoveryBinding;
import org.edx.mobile.event.DiscoveryTabSelectedEvent;
import org.edx.mobile.module.analytics.Analytics;
import org.edx.mobile.view.dialog.NativeFindCoursesFragment;

import de.greenrobot.event.EventBus;

public class MainDiscoveryFragment extends BaseFragment {
    @Inject
    protected IEdxEnvironment environment;

    @Nullable
    protected FragmentMainDiscoveryBinding binding;

    private Fragment courseDiscoveryFragment;
    private Fragment programDiscoveryFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_discovery,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragments();
        binding.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onFragmentSelected(checkedId, true);
            }
        });
        EventBus.getDefault().register(this);
    }

    public void onFragmentSelected(@IdRes int resId, final boolean isUserSelected) {
        switch (resId) {
            case R.id.option_courses:
                showFragment(courseDiscoveryFragment);
                hideFragment(programDiscoveryFragment);
                if (isUserSelected) {
                    environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.FIND_COURSES);
                }
                break;
            case R.id.option_programs:
                showFragment(programDiscoveryFragment);
                hideFragment(courseDiscoveryFragment);
                if (isUserSelected) {
                    //TODO: Add program discovery analytics over here
                }
                break;
        }
    }

    public void showFragment(@Nullable Fragment fragment) {
        if (fragment == null || !fragment.isHidden()) {
            return;
        }
        getChildFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

    public void hideFragment(@Nullable Fragment fragment) {
        if (fragment == null || fragment.isHidden()) {
            return;
        }
        getChildFragmentManager().beginTransaction()
                .hide(fragment)
                .commit();
    }

    public void initFragments() {
        @IdRes
        int checkedId = -1;
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (environment.getConfig().getDiscoveryConfig().getProgramDiscoveryConfig() != null &&
                environment.getConfig().getDiscoveryConfig().getProgramDiscoveryConfig().isDiscoveryEnabled(environment)) {
            programDiscoveryFragment = new WebViewDiscoverProgramsFragment();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_programs, programDiscoveryFragment, "fragment_programs");
            fragmentTransaction.commit();

            checkedId = R.id.option_programs;
        } else {
            hideTabsBar();
        }

        if (environment.getConfig().getDiscoveryConfig().getCourseDiscoveryConfig() != null &&
                environment.getConfig().getDiscoveryConfig().getCourseDiscoveryConfig().isDiscoveryEnabled()) {
            if (environment.getConfig().getDiscoveryConfig().getCourseDiscoveryConfig().isWebviewDiscoveryEnabled()) {
                courseDiscoveryFragment = new WebViewDiscoverCoursesFragment();
                courseDiscoveryFragment.setArguments(getArguments());
            } else {
                courseDiscoveryFragment = new NativeFindCoursesFragment();
            }

            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_courses, courseDiscoveryFragment, "fragment_courses");
            fragmentTransaction.commit();
            checkedId = R.id.option_courses;
        } else {
            hideTabsBar();
        }

        if (checkedId != -1) {
            onFragmentSelected(checkedId, false);
            binding.options.check(checkedId);
        }
    }

    public void hideTabsBar() {
        binding.options.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull DiscoveryTabSelectedEvent event) {
        onFragmentSelected(binding.options.getCheckedRadioButtonId(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
