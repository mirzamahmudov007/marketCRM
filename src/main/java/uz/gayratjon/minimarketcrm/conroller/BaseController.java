package uz.gayratjon.minimarketcrm.conroller;// File: src/main/java/uz/gayratjon/minimarketcrm/controller/BaseController.java

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.gayratjon.minimarketcrm.security.UserPrincipal;

public abstract class BaseController {

    protected Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getId();
        }
        return null; // Null qaytarish o'rniga, xatolikni yumshoqroq boshqarish
    }
}