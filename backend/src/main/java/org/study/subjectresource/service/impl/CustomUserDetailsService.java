//package org.study.subjectresource.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.study.subjectresource.entity.Users;
//import org.study.subjectresource.repository.UsersRepo;
//
//import java.util.Collections;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UsersRepo usersRepo;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        Optional<Users> user = usersRepo.findByUsername(username);
//        if(user.isEmpty()){
//            throw new UsernameNotFoundException("l'user "+username+" n'existe pas");
//        }
//        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),user.get().getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().getRoleName())));
//    }
//}

package org.study.subjectresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.repository.UsersRepo;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepo.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("L'utilisateur " + username + " n'existe pas");
        }

        // Vérification stricte de la casse
        Users u = user.get();
        if (!u.getUsername().equals(username)) {
            // Si le username récupéré ne correspond pas exactement (même casse) au username demandé
            throw new UsernameNotFoundException("L'utilisateur " + username + " n'existe pas (casse incorrecte)");
        }

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(u.getRole().getRoleName()))
        );
    }
}
