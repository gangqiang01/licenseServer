package com.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "licenseinfo", schema = "public")
public class LicenseInfo implements java.io.Serializable {

	private long id;
	private String name;
	private String productname;
	private long ts;
	private long vts;
	private int num;

	public LicenseInfo() {
	}

	private LicenseInfo(long id) {
		this.id = id;
	}

	public LicenseInfo(String name) {
		this.name = name;
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

	@Column(name = "name", unique = true, length = 256)
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
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

	public long getVts(){
		return this.ts;
	}
	public void setVts(long vts){
		this.vts = vts;
	}

	public int num(){
		return this.num;
	}
	public void setNum(int num){
		this.num = num;
	}

}
