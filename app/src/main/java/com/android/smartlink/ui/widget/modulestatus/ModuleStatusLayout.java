package com.android.smartlink.ui.widget.modulestatus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.android.smartlink.Constants;
import com.android.smartlink.R;
import com.android.smartlink.bean.Modules.Module;
import com.android.smartlink.ui.model.UIModule;

import java.util.List;

/**
 * User: LIUWEI
 * Date: 2017-11-16
 * Time: 14:47
 */
public class ModuleStatusLayout extends FrameLayout
{
    private LayoutInflater mLayoutInflater;

    private NormalModuleStatusView mNormalModuleStatusView;

    private AlarmModuleStatusView mAlarmModuleStatusView;

    private List<Module> mModules;

    public ModuleStatusLayout(@NonNull Context context)
    {
        super(context);

        initialize(context);
    }

    public ModuleStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        initialize(context);
    }

    public ModuleStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initialize(context);
    }

    private void initialize(Context context)
    {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public final void setModules(List<Module> modules)
    {
        final List<Module> mOldList = mModules;

        if (mModules != modules)
        {
            mModules = modules;

            if (getMaxStatus(modules) == Constants.STATUS_NORMAL)
            {
                if (mNormalModuleStatusView == null)
                {
                    mNormalModuleStatusView = (NormalModuleStatusView) mLayoutInflater.inflate(R.layout.comp_module_status_normal, this, false);
                }

                mNormalModuleStatusView.onModuleChanged(modules, mOldList);

                if (mNormalModuleStatusView.getParent() == null)
                {
                    addView(mNormalModuleStatusView);
                }

                if (mAlarmModuleStatusView != null)
                {
                    removeView(mAlarmModuleStatusView);
                }
            }
            else
            {
                if (mAlarmModuleStatusView == null)
                {
                    mAlarmModuleStatusView = (AlarmModuleStatusView) mLayoutInflater.inflate(R.layout.comp_module_status_alarm, this, false);
                }

                mAlarmModuleStatusView.onModuleChanged(modules, mOldList);

                if (mAlarmModuleStatusView.getParent() == null)
                {
                    addView(mAlarmModuleStatusView);
                }

                if (mNormalModuleStatusView != null)
                {
                    removeView(mNormalModuleStatusView);
                }
            }
        }
    }

    private int getMaxStatus(List<Module> list)
    {
        Module alarmModule = list.get(0);

        for (Module module : list)
        {
            if (module.getStatus() == Constants.STATUS_ERROR)
            {
                alarmModule = module;

                break;
            }

            if (UIModule.getStatus(module) > UIModule.getStatus(alarmModule))
            {
                alarmModule = module;
            }
        }

        return UIModule.getStatus(alarmModule);
    }
}
