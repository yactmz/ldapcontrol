package com.example.LdapControl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.cert.CRLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;

public class ConvertCrl {
	

    public X509CRL convertBytesToX509CertificateWithBouncyCastle(byte[] certbyte) {
        try {
        	/*//String hexadecimalRepresentation = bytesToHex(myByteArray);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(certbyte);
            //X509CertificateHolder certificateHolder = new X509CertificateHolder(inputStream);
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            return x509Certificate;
            //return new JcaX509CertificateConverter().getCertificate(certificateHolder);*/
        	X509CRLHolder crlHolder = new X509CRLHolder(certbyte);
        	
        	return new JcaX509CRLConverter().getCRL(crlHolder);
        	
        } catch (Exception e) {
            e.printStackTrace(); // Gérez l'exception selon vos besoins
            return null;
        }
        
    }
    
    
    
    //@SuppressWarnings("deprecation")
	public void convert (X509CRL cert ) {
		
		
    	
        if (cert != null) {
            System.out.println("Version: " + cert.getVersion());
            System.out.println("Issuer: " + cert.getIssuerX500Principal());
            System.out.println("This update: " + cert.getThisUpdate());
            cert.getRevokedCertificates().forEach(revokedCert -> {
                System.out.println("Revoked Serial Number: " + revokedCert.getSerialNumber());
            });
        } 
        
        else {
            System.out.println("Certificate is null or invalid");
        }
    }
	
	
	
	public void publickey (byte[] certbyte) throws IOException {
		X509CertificateHolder caCertificateHolder = new X509CertificateHolder (certbyte);
		SubjectPublicKeyInfo caPublicKeyInfo = caCertificateHolder.getSubjectPublicKeyInfo();
		System.out.println("Algorithm: " + caPublicKeyInfo.getAlgorithm().getAlgorithm());
		System.out.println("Encoded Public Key: " + caPublicKeyInfo.getPublicKeyData().toString());
	}
	
	
	public X509CRL writeintocrl (byte[] crlbytes) {
    	BigInteger serialNumber = new BigInteger("3830ECC471A9DC2ADB63BB9EAE0D2A8B0D4E97E8",16); // Numéro de série du certificat révoqué
    	Date revocationDate = new Date(); // Date de révocation du certificat
    	int reasonCode = CRLReason.keyCompromise; 
    	KeyPairGenerator keyPairGenerator;
    	//FileInputStream inputStream = new FileInputStream(Filepath);
        CertificateList certificateList = CertificateList.getInstance(crlbytes);
        X509CRLHolder crlHolder = new X509CRLHolder(certificateList);
    	
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	    	KeyPair keyPair = keyPairGenerator.generateKeyPair();
	    	PrivateKey privateKey = keyPair.getPrivate();
	        //X509CRLHolder crlHolder = new X509CRLHolder();

	    	

	    	// Créez un objet ContentSigner pour signer la CRL
	    	ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);

	    	// Créez un objet X509v2CRLBuilder en fournissant les paramètres nécessaires
	    	X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(crlHolder/* ajoutez les paramètres nécessaires */);

	    	// Ajoutez une entrée de révocation (par exemple)
	    	crlBuilder.addCRLEntry(serialNumber, revocationDate, reasonCode/* ajoutez les détails de l'entrée de révocation */);

	    	//Date de mis à jour
	    	Date nextUpdate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // 7 jours
            crlBuilder.setNextUpdate(nextUpdate);
	    	// Continuez avec la construction de la CRL en utilisant le ContentSigner
	    	X509CRLHolder crl = crlBuilder.build(contentSigner);
            return  new JcaX509CRLConverter().getCRL(crl);
       
	    	
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("pas reussi a build");
			return null;
		}



}
}
