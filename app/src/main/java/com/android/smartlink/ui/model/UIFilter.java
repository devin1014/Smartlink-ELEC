package com.android.smartlink.ui.model;


import com.android.smartlink.ui.model.BaseModule.DefaultBaseModuleImp;

/**
 * User: LIUWEI
 * Date: 2017-10-23
 * Time: 17:51
 */
public class UIFilter extends DefaultBaseModuleImp
{
    private boolean mChecked;

    public UIFilter(BaseModule baseModule)
    {
        super(baseModule);

        mChecked = true;
    }

    public boolean isChecked()
    {
        return mChecked;
    }

    public void setChecked(boolean checked)
    {
        mChecked = checked;
    }
}
