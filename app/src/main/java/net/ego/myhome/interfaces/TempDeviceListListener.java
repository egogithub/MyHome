package net.ego.myhome.interfaces;

import net.ego.myhome.pojo.TempDevice;

import java.util.List;

public interface TempDeviceListListener {
    void onTempDeviceList(List<TempDevice> tempDeviceList);
}
