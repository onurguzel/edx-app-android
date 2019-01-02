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
import org.edx.mobile.module.analytics.Analytics;
import org.edx.mobile.view.dialog.NativeFindCoursesFragment;

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
                onFragmentSelected(checkedId);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onFragmentSelected(binding.options.getCheckedRadioButtonId());
    }

    public void onFragmentSelected(@IdRes int resId) {
        switch (resId) {
            case R.id.option_courses:
                showFragment(courseDiscoveryFragment);
                hideFragment(programDiscoveryFragment);
                environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.FIND_COURSES);
                break;
            case R.id.option_programs:
                showFragment(programDiscoveryFragment);
                hideFragment(courseDiscoveryFragment);
                //TODO: Add program discovery analytics over here
                break;
        }
    }

    public void showFragment(@Nullable Fragment fragment) {
        if (fragment == null) {
            return;
        }
        getChildFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

    public void hideFragment(@Nullable Fragment fragment) {
        if (fragment == null) {
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

        if (environment.getConfig().getProgramDiscoveryConfig().isProgramDiscoveryEnabled(environment)) {
            programDiscoveryFragment = new WebViewDiscoverProgramsFragment();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_programs, programDiscoveryFragment, "fragment_programs");
            fragmentTransaction.commit();

            checkedId = R.id.option_programs;
        } else {
            hideTabsBar();
        }

        if (environment.getConfig().getCourseDiscoveryConfig().isCourseDiscoveryEnabled()) {
            if (environment.getConfig().getCourseDiscoveryConfig().isWebviewCourseDiscoveryEnabled()) {
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
            onFragmentSelected(checkedId);
            binding.options.check(checkedId);
        }
    }

    public void hideTabsBar() {
        binding.options.setVisibility(View.GONE);
    }
}
