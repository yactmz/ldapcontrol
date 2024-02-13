package com.example.LdapControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.ldap.LdapName;

import java.security.cert.CRLException;
import java.security.cert.X509CRL;

import javax.naming.Name;

@Service
public class PersonService  {
    @Autowired
    private PersonRepository personRepository;
    private LdapTemplate ldapTemplate = new LdapTemplate();
    
    
    
    
    public PersonService( LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }
    
    
    

    public Person findByCommonName(String commonName) {
        return personRepository.findByCommonName(commonName);
    }
    
    
    
    public Person findByDn(Name dn) {
    	return personRepository.findByDn(dn);
    }
    
    
    
    
    public byte[] getCertificateRevocationList(String commonName) {
        Person person = personRepository.findByCommonName(commonName);
        if (person != null) {
            return person.getCertificateRevocationList();
        }
        return null;
    }
    
    
    public byte[] getCertificate(String commonName) {
        Person person = personRepository.findByCommonName(commonName);
        if (person != null) {
            return person.getCertificate();
        }
        return null;
    }

	public void updateCRL(String commonName, X509CRL crl_add) throws CRLException {
        // Construire le DN (Distinguished Name) pour l'entrée dans l'annuaire
       // Name dn = new DistinguishedName("ou=CRLs,dc=mycompany,dc=com").add("cn", commonName);
        
        //DistinguishedName dn = new DistinguishedName("ou=People,dc=thalesgroup,dc=com");
		LdapName crlDistinguishedName = LdapUtils.newLdapName("ou=People,dc=thalesgroup,dc=com");
		
		LdapUtils.prepend(crlDistinguishedName,commonName);
		//dn.add("cn", commonName);
        // Récupérer l'entrée existante
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        // Mettre à jour les attributs de l'entrée avec la nouvelle CRL
        updateCRLAttribute(context, crl_add);

        // Mettre à jour l'entrée dans l'annuaire
        ldapTemplate.modifyAttributes(dn, context.getModificationItems());
    }

    public void updateCRLAttribute(DirContextOperations context, X509CRL crl_add) throws CRLException {
        // Supprimer l'ancienne valeur de l'attribut CRL (si elle existe)
        context.setAttributeValues("cRLDistributionPoint", null);

        // Ajouter la nouvelle valeur de l'attribut CRL
        context.setAttributeValue("cRLDistributionPoint", crl_add);
    }

    // Ajoutez d'autres méthodes pour les opérations CRUD, la récupération de CRL, etc.
}

    
    
    
