package com.code.monks.nukkad.context;

import lombok.Data;

import javax.management.relation.Role;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static void setUserContext(UserContext context){
        userContext.set(context);
    }
    public static UserContext getUserContext() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }

    @Data
    public static class UserContext {
        private Long userId;
        private Role role;
    }
}
