package com.nazar.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}