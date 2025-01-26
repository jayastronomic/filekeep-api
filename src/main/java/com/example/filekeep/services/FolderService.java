package com.example.filekeep.services;

import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.NewFolderDto;
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

    public Folder createFolder(NewFolderDto payload){
        Folder parent = folderRepository.findById(payload.parentId()).orElseThrow();
        Folder newFolder = Folder.builder()
                                .folderName(payload.folderName())
                                .parentFolder(parent)
                                .user(currentUser())
                                .build();
        return this.folderRepository.save(newFolder);
    }


    public Folder getFolder(String folderName) {
        return folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), folderName);
    }
}

