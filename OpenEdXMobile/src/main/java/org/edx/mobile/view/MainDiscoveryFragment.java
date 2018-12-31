package org.edx.mobile.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.inject.Inject;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.edx.mobile.R;
import org.edx.mobile.base.BaseFragment;
import org.edx.mobile.core.IEdxEnvironment;
import org.edx.mobile.databinding.FragmentMainDiscoveryBinding;
import org.edx.mobile.model.FragmentItemModel;
import org.edx.mobile.module.analytics.Analytics;
import org.edx.mobile.view.adapters.FragmentItemPagerAdapter;
import org.edx.mobile.view.dialog.NativeFindCoursesFragment;

import java.util.ArrayList;
import java.util.List;

public class MainDiscoveryFragment extends BaseFragment {
    @Inject
    protected IEdxEnvironment environment;

    @Nullable
    protected FragmentMainDiscoveryBinding binding;

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

        // Init view pager
        final FragmentItemPagerAdapter adapter = new FragmentItemPagerAdapter(this.getChildFragmentManager(), getFragmentItems());
        binding.viewPager.setAdapter(adapter);
        binding.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.option_courses:
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.option_programs:
                        binding.viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    public List<FragmentItemModel> getFragmentItems() {
        ArrayList<FragmentItemModel> items = new ArrayList<>();

        @IdRes
        int checkedId = -1;

        if (environment.getConfig().getCourseDiscoveryConfig().isCourseDiscoveryEnabled()) {
            items.add(new FragmentItemModel(
                    environment.getConfig().getCourseDiscoveryConfig().isWebviewCourseDiscoveryEnabled()
                            ? WebViewDiscoverCoursesFragment.class : NativeFindCoursesFragment.class,
                    getResources().getString(R.string.label_discovery), FontAwesomeIcons.fa_search,
                    environment.getConfig().getCourseDiscoveryConfig().isWebviewCourseDiscoveryEnabled()
                            ? getArguments() : null,
                    new FragmentItemModel.FragmentStateListener() {
                        @Override
                        public void onFragmentSelected() {
                            environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.FIND_COURSES);
                        }
                    }));
            checkedId = R.id.option_courses;
        } else {
            hideTabsBar();
        }

        if (environment.getConfig().getProgramDiscoveryConfig().isProgramDiscoveryEnabled()) {
            items.add(new FragmentItemModel(WebViewDiscoverProgramsFragment.class,
                    getResources().getString(R.string.label_discovery), FontAwesomeIcons.fa_search,
                    new FragmentItemModel.FragmentStateListener() {
                        @Override
                        public void onFragmentSelected() {
                            //TODO: Add program discovery analytics over here
                        }
                    }));
            if (checkedId == -1) {
                checkedId = R.id.option_programs;
            }
        } else {
            hideTabsBar();
        }
        if (checkedId != -1) {
            binding.options.check(checkedId);
        }
        return items;
    }

    public void hideTabsBar() {
        binding.options.setVisibility(View.GONE);
    }
}
