package com.android.smartlink.util;

import android.content.Context;

/**
 * User: NeuLion(wei.liu@neulion.com.com)
 * Date: 2017-10-25
 * Time: 10:12
 */
public class HttpUrl
{
    public static String getHomeUrl()
    {
        return "http://localhost:8080/examples/smartlink/main.json";
    }

    public static String getWeatherUrl(Context context, String city)
    {
        return "https://free-api.heweather.com/v5/weather?city=" + city + "&key=" + Utils.getApplicationMetaData(context, "weatherKey");
    }

    public static String getWeatherUrl(Context context, double[] locations)
    {
        return "https://free-api.heweather.com/v5/weather?city=" + locations[0] + "," + locations[1] + "&key=" + Utils.getApplicationMetaData(context, "weatherKey");
    }
}
