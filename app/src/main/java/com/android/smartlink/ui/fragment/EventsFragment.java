package com.android.smartlink.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smartlink.R;
import com.android.smartlink.assist.EventsRequestProvider;
import com.android.smartlink.assist.RequestCallback;
import com.android.smartlink.bean.Events;
import com.android.smartlink.ui.fragment.base.BaseSmartlinkFragment;
import com.android.smartlink.ui.widget.LoadingLayout;

import butterknife.BindView;

/**
 * User: NeuLion(wei.liu@neulion.com.com)
 * Date: 2017-10-16
 * Time: 18:00
 */
public class EventsFragment extends BaseSmartlinkFragment implements RequestCallback<Events>, OnRefreshListener
{
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;

    private EventsRequestProvider mRequestProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    private void initComponent(View view)
    {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRequestProvider = new EventsRequestProvider(this);

        mRequestProvider.request("http://localhost:8080/examples/smartlink/events.json");

        mLoadingLayout.showLoading();
    }

    @Override
    public void onDestroyView()
    {
        mRequestProvider.destroy();

        super.onDestroyView();
    }

    @Override
    public void onResponse(Events events)
    {
        mLoadingLayout.showContent();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable)
    {
        mLoadingLayout.showMessage(getString(R.string.request_data_error));

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh()
    {
        // call request
        mRequestProvider.request("http://localhost:8080/examples/smartlink/events.json");

        // hide loading and show blank loading view
        mLoadingLayout.showBlankView();
    }
}
