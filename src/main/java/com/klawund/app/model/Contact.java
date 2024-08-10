package com.klawund.app.model;

public record Contact(Long id, String firstName, String lastName, String email, String zipCode, String rawAddress)
{
}
