package com.db.impl;

import com.db.entity.LicenseInfo;
import java.util.List;

public interface ILicenseInfoServer {
    public boolean insertLicenseInfo(LicenseInfo group);

    public boolean deleteLicenseInfoByMachineId(String machineId);

    public boolean deleteLicenseInfoAll();

    public LicenseInfo[] getLicenseInfoAll();

    public List<LicenseInfo> getLicenseInfoByMachineId(String machineId);

    public boolean updateLicenseInfo(LicenseInfo licenseInfo);

    public List<LicenseInfo> getLicenseInfoByProductname(String productname);
    public LicenseInfo getLicenseInfoByMachineIdAndProductname(String machineId, String productname);
}
