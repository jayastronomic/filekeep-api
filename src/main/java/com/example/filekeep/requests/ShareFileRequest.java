package com.example.filekeep.requests;

import java.util.List;
import java.util.UUID;

public record ShareFileRequest(List<String> userEmails, UUID fileId) {}