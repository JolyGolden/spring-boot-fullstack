package com.nazar.customer;


public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age

) {
}