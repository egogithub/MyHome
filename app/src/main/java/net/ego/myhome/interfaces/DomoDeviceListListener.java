package net.ego.myhome.interfaces;

import net.ego.myhome.pojo.DmDevice;

import java.util.List;

public interface DomoDeviceListListener {
    public void onDevicesList(List<DmDevice> deviceList);
}
