package com.example.LdapControl;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Component;

import java.security.cert.CRLException;
import java.security.cert.X509Certificate;

import javax.naming.Name;

@RestController
@RequestMapping("/crl")
public class PersonController {
	

	

    @Autowired
    private PersonService personService;

    @GetMapping("/{commonName}")
    public Person getPerson(@PathVariable String commonName) {
        System.out.println("Calling getPerson with Common Name: " + commonName);
        Person person = personService.findByCommonName(commonName);
        System.out.println("Received Person: " + person);
        return person;
			//return personService.findByCommonName(commonName);
	
    }
    
    
  @GetMapping("/{dn}")
    public Person getPerson1(@PathVariable Name dn) {
       // System.out.println("Calling getPerson with Common Name: " + dn);
        Person person = personService.findByDn(dn);
       // System.out.println("Received Person: " + person);
        return person;
			//return personService.findByCommonName(commonName);
	
    }
    
    
    @GetMapping("/commonName/crl")
    public byte[] getCertificateRevocationList(@PathVariable String commonName) {
    	System.out.print("crl recuperer");
        return personService.getCertificateRevocationList(commonName);
    }
    
    
    @GetMapping("/commonName/cert")
    public byte[] getCertificate(@PathVariable String commonName) {
        return personService.getCertificate(commonName);
    }
    
    @PostMapping("/commonName")
    	public void  updateCRL(@PathVariable String commonName, byte[] newCrlBytes) throws CRLException {
    	 	personService.updateCRL(commonName, newCrlBytes);
    }
    
    
  /*  @PostMapping("/commonName/crl")
    public String uploadCRL(@RequestBody byte[] crlBytes) {
    try {
        // Convertit la chaîne Base64 en tableau de bytes
        byte[] crlBytes = Base64.getDecoder().decode(crlBase64);

        // Convertit les bytes en objet X509CRL (si nécessaire)
        X509CRL x509CRL = personService.convertBytesToX509CRL(crlBytes);

        // Traitez la CRL comme nécessaire (vous pouvez la stocker dans l'annuaire, etc.)
        personService.processCRL(x509CRL);

        return "CRL uploaded successfully!";
    } catch (CRLException e) {
        e.printStackTrace();
        return "Error uploading CRL: " + e.getMessage();
    }
    
    }*/

    // Ajoutez d'autres méthodes pour gérer les opérations CRUD
}
