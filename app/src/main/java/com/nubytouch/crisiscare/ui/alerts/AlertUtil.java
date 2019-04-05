package com.nubytouch.crisiscare.ui.alerts;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.utils.ResUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class AlertUtil
{
    @ColorInt
    public static int getColorForStatus(int status)
    {
        int colorRes;

        switch (status)
        {
            case Alert.STATUS_OPEN:
            {
                colorRes = R.color.alert_status_open;
                break;
            }
            case Alert.STATUS_RESOLVED:
            {
                colorRes = R.color.alert_status_resolved;
                break;
            }
            case Alert.STATUS_REJECTED:
            {
                colorRes = R.color.alert_status_rejected;
                break;
            }
            case Alert.STATUS_ARCHIVED:
            {
                colorRes = R.color.alert_status_archived;
                break;
            }
            case Alert.STATUS_NEW:
            default:
            {
                colorRes = R.color.alert_status_new;
                break;
            }
        }

        return ResUtils.getColor(colorRes);
    }

    @StringRes
    public static int getStatusName(int status)
    {
        switch (status)
        {
            case Alert.STATUS_NEW:
            {
                return R.string.status_alerte_new;
            }
            case Alert.STATUS_OPEN:
            {
                return R.string.status_alerte_open;
            }
            case Alert.STATUS_RESOLVED:
            {
                return R.string.status_alerte_resolved;
            }
            case Alert.STATUS_REJECTED:
            {
                return R.string.status_alerte_rejected;
            }
            case Alert.STATUS_ARCHIVED:
            {
                return R.string.status_alerte_archived;
            }
        }

        return 0;
    }

    @ColorInt
    public static int getColorForLevel(int level)
    {
        int colorRes;

        switch (level)
        {
            case Alert.PRIORITY_LOW:
            {
                colorRes = R.color.alert_level_low;
                break;
            }
            case Alert.PRIORITY_MEDIUM:
            {
                colorRes = R.color.alert_level_medium;
                break;
            }
            case Alert.PRIORITY_HIGH:
            {
                colorRes = R.color.alert_level_high;
                break;
            }
            case Alert.PRIORITY_NONE:
            default:
            {
                colorRes = R.color.alert_level_none;
                break;
            }
        }

        return ResUtils.getColor(colorRes);
    }
}
