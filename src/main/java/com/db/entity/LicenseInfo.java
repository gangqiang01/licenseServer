package com.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "licenseinfo", schema = "public")
public class LicenseInfo implements java.io.Serializable {

	private long id;
	private String machineid;
	private String productname;
	private long ts;
	private String authcode;

	public LicenseInfo() {
	}

	private LicenseInfo(long id) {
		this.id = id;
	}

	public LicenseInfo(String machineid) {
		this.machineid = machineid;
	}


	@Id
	@SequenceGenerator(name = "licenseinfo_id_seq", allocationSize = 1, initialValue = 1, sequenceName = "licenseinfo_id_seq")
	@GeneratedValue(generator = "licenseinfo_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	private void setId(long id) {
		this.id = id;
	}

	@Column(name = "machineid", unique = true, length = 256)
	public String getMachineid(){
		return this.machineid;
	}
	public void setMachineid(String machineid){
		this.machineid = machineid;
	}

	public String getProductname() {
		return this.productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}

	public long getTs(){
		return this.ts;
	}
	public void setTs(long ts){
		this.ts = ts;
	}

	public String getAuthcode(){
		return this.authcode;
	}
	public void setAuthcode(String authcode){
		this.authcode = authcode;
	}


}
