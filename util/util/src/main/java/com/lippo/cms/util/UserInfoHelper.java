package com.lippo.cms.util;


//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoHelper {

    public static boolean isDoctor(Principal principal) {
//        if (principal instanceof Authentication) {
            return checkRole(principal, "ROLE_DOCTOR");
//        }
//        return false;
    }

    public static boolean isCA(Principal principal) {
//        if (principal instanceof Authentication) {
            return checkRole(principal, "ROLE_CA");
//        }
//        return false;
    }

    private static boolean checkRole(Principal principal, String role) {
        return true;
//        Authentication authentication = (Authentication) principal;
//        return authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals(role));
    }


    public static String loginName(Principal principal) {
        if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }
    }

    public static List<String> getUserRoles(Principal principal){
//        if(principal instanceof Authentication){
//            return ((Authentication) principal).getAuthorities().stream()
//                    .map(auth -> auth.toString())
//                    .collect(Collectors.toList());
//        }else{
            return new ArrayList<>();
//        }
    }
}
