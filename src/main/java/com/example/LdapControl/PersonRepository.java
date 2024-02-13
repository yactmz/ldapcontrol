package com.example.LdapControl;
import javax.naming.Name;
import org.springframework.data.ldap.repository.LdapRepository;

public interface PersonRepository extends LdapRepository<Person> {
    Person findByCommonName(String commonName);
    Person findByDn(Name dn);
}
