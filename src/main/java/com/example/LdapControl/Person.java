package com.example.LdapControl;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import javax.naming.Name;

@Entry(base = "ou=People",objectClasses = { "applicationProcess", "certificationAuthority-V2", "top" })
public class Person {

    @Id
    private Name dn;

    @Attribute(name = "cn")
    private String commonName;

  

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}


    @Attribute(name = "certificateRevocationList;binary")
    private byte[] certificateRevocationList;

	public byte[] getCertificateRevocationList() {
		return certificateRevocationList;
	}
	
    @Attribute(name = "cACertificate;binary")
    private byte[] certificate;

	public byte[] getCertificate() {
		return certificate;
	}
	
	public void setCertificate(byte[] certificate) {
		this.certificate = certificate;
	}
	

	public void setCertificateRevocationList(byte[] certificateRevocationList) {
		this.certificateRevocationList = certificateRevocationList;
	}



    // Getter and Setter methods
    
    
}
