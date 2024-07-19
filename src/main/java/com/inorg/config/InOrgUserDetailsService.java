package com.inorg.config;


import com.inorg.model.Customer;
import com.inorg.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InOrgUserDetailsService implements UserDetailsService {

    CustomerRepository customerRepository;
    @Autowired
    public InOrgUserDetailsService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer=customerRepository.findCustomerByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found for "+username)
                );
//        List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority(customer.getRole()));
        List<GrantedAuthority> authorities = customer.getAuthorities().stream().map(authority -> new
                SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new User(customer.getEmail(),customer.getPwd(),authorities);
    }
}
