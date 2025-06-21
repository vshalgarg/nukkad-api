package com.code.monks.nukkad.context;

import com.code.monks.nukkad.dto.User;

public class UserContextHolder {

    private static final ThreadLocal<User> tokenHolder = new ThreadLocal<>();

    public static void setUser(User user){
        tokenHolder.set(user);
    }

    public static User getUser(){
        return tokenHolder.get();
    }

    public static User getRequiredUser(){
        User user=tokenHolder.get();
        if(user==null){
            throw new IllegalStateException("No user found in context holder");
        }
        return user;
    }

    public static void clear() {
        tokenHolder.remove();
    }


}
