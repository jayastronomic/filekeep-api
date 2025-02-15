package com.example.filekeep.dtos;

import java.util.List;
import java.util.UUID;

public record ShareFileData(List<String> userEmails, UUID fileId) {}