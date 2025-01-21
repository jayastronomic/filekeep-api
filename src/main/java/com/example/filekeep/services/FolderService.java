package com.example.filekeep.services;

import org.springframework.stereotype.Service;

import com.example.filekeep.models.Folder;
import com.example.filekeep.repositories.FolderRepository;

@Service
public class FolderService extends ApplicationService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Folder getRoot(){
        return folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), "/");
    }
}

