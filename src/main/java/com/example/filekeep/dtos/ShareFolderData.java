package com.example.filekeep.dtos;

import java.util.List;
import java.util.UUID;

public record ShareFolderData(List<String> userEmails, UUID folderId) {}
