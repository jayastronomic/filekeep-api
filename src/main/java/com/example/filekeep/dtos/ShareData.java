package com.example.filekeep.dtos;

import java.util.List;
import java.util.UUID;

public record ShareData(List<String> userEmails, UUID id) {}