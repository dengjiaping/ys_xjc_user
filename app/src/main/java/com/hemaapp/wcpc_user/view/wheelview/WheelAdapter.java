package com.hemaapp.wcpc_user.view.wheelview;

/**
 * Wheel adapter interface
 * Created by WangYuxia on 2016/5/11.
 * @deprecated Use WheelViewAdapter
 */
public interface WheelAdapter {
    /**
     * Gets items count
     * @return the count of wheel items
     */
    public int getItemsCount();

    /**
     * Gets a wheel item by index.
     *
     * @param index the item index
     * @return the wheel item text or null
     */
    public String getItem(int index);

    /**
     * Gets maximum item length. It is used to determine the wheel width.
     * If -1 is returned there will be used the default wheel width.
     *
     * @return the maximum item length or -1
     */
    public int getMaximumLength();
}

