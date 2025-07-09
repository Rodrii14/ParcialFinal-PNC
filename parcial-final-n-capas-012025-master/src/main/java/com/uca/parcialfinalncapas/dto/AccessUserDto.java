package com.uca.parcialfinalncapas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccessUserDto {
    @JsonProperty("correo")
    private String email;
    @JsonProperty("password")
    private String password;
}