package com.example.filekeep.requests;

import java.util.List;
import java.util.UUID;

public record ShareFolderRequest(List<String> userEmails, UUID folderId) {}
