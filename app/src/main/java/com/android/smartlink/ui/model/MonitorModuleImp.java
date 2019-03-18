package com.android.smartlink.ui.model;

import android.content.res.Resources;
import android.text.TextUtils;

import com.android.smartlink.Constants;
import com.android.smartlink.R;
import com.android.smartlink.application.manager.AppManager;
import com.android.smartlink.bean.ModulesData.MonitorModuleData;
import com.android.smartlink.ui.model.BaseModule.DefaultBaseModuleImp;
import com.android.smartlink.util.ui.ImageResUtil;
import com.neulion.android.diffrecycler.annotation.DiffContent;
import com.neulion.android.diffrecycler.annotation.DiffItem;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * User: LIUWEI
 * Date: 2017-10-18
 * Time: 15:57
 */
public class MonitorModuleImp extends DefaultBaseModuleImp<MonitorModuleData> implements Serializable, UIMonitorModule
{
    private static final long serialVersionUID = 179763754284831614L;

    @DiffItem
    int mId;

    @DiffContent
    int mStatus;

    @DiffContent
    String mEnergy;

    private MonitorModuleData mModule;

    private final String POWER_KWH;

    private int[] mStatusColor;

    private int[] mTextStatusColor;

    private int mPowerLoad = 0;

    private String[] mStatusArray;

    private DecimalFormat mNumberFormat = new DecimalFormat("0.0");

    private int mImageType;

    private String[] mCurrent;

    private String[] mVoltage;

    public MonitorModuleImp(MonitorModuleData module)
    {
        this(module, ImageType.DRAWABLE_NORMAL);
    }

    public MonitorModuleImp(MonitorModuleData module, @ImageType int imageType)
    {
        super(module);

        mModule = module;

        POWER_KWH = AppManager.getInstance().getApplication().getResources().getString(R.string.format_power);

        mId = getId();

        mEnergy = module.getEnergy();

        mStatus = module.getStatus();

        Resources resources = AppManager.getInstance().getApplication().getResources();

        int green = resources.getColor(R.color.module_status_good);

        int red = resources.getColor(R.color.module_status_error);

        int yellow = resources.getColor(R.color.module_status_warn);

        int white = resources.getColor(R.color.module_status_none);

        mStatusColor = new int[]{green, red, yellow};

        mTextStatusColor = new int[]{white, red, yellow};

        mStatusArray = AppManager.getInstance().getStringArray(R.array.module_status_array);

        if (!TextUtils.isEmpty(mModule.getPower()))
        {
            mPowerLoad = Math.min((int) (Float.valueOf(mModule.getPower()) / 5f * 100), 100);
        }

        mImageType = imageType;

        mCurrent = new String[]{module.getCurrent1(), module.getCurrent2(), module.getCurrent3()};

        mVoltage = new String[]{module.getVoltage1(), module.getVoltage2(), module.getVoltage3()};
    }

    public int getImageRes()
    {
        return ImageResUtil.getImage(getId(), mImageType);
    }

    public String getName()
    {
        return AppManager.getInstance().getModuleName(getId());
    }

    @Override
    public String[] getCurrent()
    {
        return mCurrent;
    }

    @Override
    public String[] getVoltage()
    {
        return mVoltage;
    }

    @Override
    public String getPower()
    {
        return mModule.getPower();
    }

    @Override
    public String getPowerFactor()
    {
        return mModule.getPowerFactor();
    }

    public String getEnergy()
    {
        if (TextUtils.isEmpty(mModule.getEnergy()))
        {
            return "";
        }

        return String.format(POWER_KWH, mNumberFormat.format(Float.valueOf(mModule.getEnergy())));
    }

    public String getStatusFormat()
    {
        return mStatusArray[getModuleStatus()];
    }

    public int getColor()
    {
        return mStatusColor[getModuleStatus() % mStatusColor.length];
    }

    public int getTextColor()
    {
        return mTextStatusColor[getModuleStatus()];
    }

    @Override
    public int getModuleStatus()
    {
        if (isAlarm())
        {
            return Constants.STATUS_WARNING;
        }

        return mModule.getStatus();
    }

    @DiffContent
    public int getPowerLoad()
    {
        return mPowerLoad;
    }

    @Override
    public boolean isToggle()
    {
        return false;
    }

    @Override
    public boolean hasAlarm()
    {
        int status = mModule.getStatus();

        return (status == Constants.STATUS_ERROR) // error
                || (status == Constants.STATUS_NORMAL && getPowerLoad() >= Constants.POWER_LOAD_ALARM); // alert
    }

    public boolean isAlarm()
    {
        int status = mModule.getStatus();

        return status == Constants.STATUS_NORMAL && getPowerLoad() >= Constants.POWER_LOAD_ALARM;
    }

    public boolean isError()
    {
        int status = mModule.getStatus();

        return status == Constants.STATUS_ERROR;
    }

    public String getPowerLoadPercent()
    {
        return getPowerLoad() + "%";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof MonitorModuleImp)
        {
            return getId() == ((MonitorModuleImp) obj).getId();
        }

        return super.equals(obj);
    }

    // --------------------------------------------------------------------------------------------------
    // - Helper
    // --------------------------------------------------------------------------------------------------
    public static int getStatus(MonitorModuleData module)
    {
        int status = module.getStatus();

        int powerLoad = 0;

        if (!TextUtils.isEmpty(module.getPower()))
        {
            powerLoad = Math.min((int) (Float.valueOf(module.getPower()) / 5f * 100), 100);
        }

        if (status == Constants.STATUS_NORMAL && powerLoad >= Constants.POWER_LOAD_ALARM)
        {
            return Constants.STATUS_WARNING;
        }

        return module.getStatus();
    }

    @Override
    public boolean compareData(UIMonitorModule monitorModule)
    {
        return getId() == monitorModule.getId();
    }
}
