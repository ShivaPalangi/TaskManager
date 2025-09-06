package com.example.TaskManager.config;

import org.springframework.data.domain.AuditorAware;
import com.example.TaskManager.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        // دریافت اطلاعات کاربر لاگین شده
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated() // کاربر لاگین کرده اما اعتبارش تمام شده
                || authentication instanceof AnonymousAuthenticationToken // کاربر ناشناس
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();  // هویت اصلی کاربر رو برمیگردونه
        return Optional.ofNullable(userPrincipal.getId());
        // اگه null باشه empty برمیگردونه در غیر این صورت مقدارشو برمیگردونه
    }

}
