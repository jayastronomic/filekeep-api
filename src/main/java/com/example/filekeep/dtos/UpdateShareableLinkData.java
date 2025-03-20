package com.example.filekeep.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateShareableLinkData( @NotBlank String linkAccessType) {}
