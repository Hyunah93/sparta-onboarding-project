package com.example.delivery.user.entity;

public enum UserRoleEnum {

    OWNER(Authority.OWNER),          // 사장님
    CUSTOMER(Authority.CUSTOMER);    // 손님

    private final String authority;

    UserRoleEnum(String authority){
        this.authority = authority;
    }

    public String getAuthority(){
        return this.authority;
    }

    public static class Authority{
        public static final String OWNER = "ROLE_OWNER";
        public  static final String CUSTOMER = "ROLE_CUSTOMER";

    }
}
