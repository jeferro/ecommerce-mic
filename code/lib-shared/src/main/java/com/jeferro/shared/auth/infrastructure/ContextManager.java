package com.jeferro.shared.auth.infrastructure;

import com.jeferro.shared.ddd.domain.models.auth.AnonymousAuth;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.models.auth.SystemAuth;
import com.jeferro.shared.ddd.domain.models.auth.UserAuth;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class ContextManager {

    public static void signInFromWeb(HttpServletRequest request, String username, Collection<String> roles) {
        var authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                username,
                null,
                authorities);

        WebAuthenticationDetailsSource details = new WebAuthenticationDetailsSource();
        details.buildDetails(request);

        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void signInSystem() {
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                "system",
                null,
                null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void signOut() {
        SecurityContextHolder.clearContext();
    }

    public static Auth getAuth() {
        var locale = getLocale();
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            return AnonymousAuth.create(locale);
        }

        var username = authentication.getPrincipal();

        if(username == null){
            return AnonymousAuth.create(locale);
        }

        if(username.equals("system")){
            return SystemAuth.create(locale);
        }

        var roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        return UserAuth.create(locale, (String) username, roles);
    }

    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
