package com.db.impl;

import com.db.entity.LicenseInfo;
import java.util.List;

public interface ILicenseInfoServer {
    public boolean insertLicenseInfo(LicenseInfo group);

    public boolean deleteLicenseInfoByName(String name);

    public boolean deleteLicenseInfoAll();

    public LicenseInfo[] getLicenseInfoAll();

    public LicenseInfo getLicenseInfoByName(String name);

    public boolean updateLicenseInfo(LicenseInfo group);

    public List<LicenseInfo> getLicenseInfoByProductname(String Productname);

}
