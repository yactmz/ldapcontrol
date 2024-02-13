package com.example.LdapControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;

import javax.naming.Name;

@SpringBootApplication
public class LdapControlApplication {

	public static void main(String[] args) throws CRLException  {
		ConfigurableApplicationContext context = SpringApplication.run(LdapControlApplication.class, args);

        PersonController personController = context.getBean(PersonController.class);
        ConvertCrl c1 = new ConvertCrl();
        
        Person person = personController.getPerson("jakoca");
        if (person != null) {
            // Faites quelque chose avec l'objet Person obtenu
            System.out.println("Person Name: " + person.getCommonName());
            byte[] crl = personController.getCertificateRevocationList("jakoca");
            byte[]cert = personController.getCertificate("jakoca");
           /* String hexadecimalRepresentation = bytesToHex(certificate);
            byte[] byteArray = hexToBytes(hexadecimalRepresentation);*/
            X509CRL new_crl = c1.convertBytesToX509CertificateWithBouncyCastle(crl);
            c1.convert(new_crl);
            X509CRL crl_add = c1.writeintocrl(crl);
            System.out.print("\n nouveau serial number \n");
            c1.convert(crl_add);
            personController.updateCRL("jakoca", crl_add);
            try {
				c1.publickey(cert);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           // System.out.println(byteArray);
           
            //System.out.println("Certificate: " + certificate);
        } else {
            System.out.println("Person not found");
        }
        


    
        context.close();
	}


}
