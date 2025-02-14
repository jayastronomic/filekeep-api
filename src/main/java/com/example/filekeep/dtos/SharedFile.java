package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SharedFile {
    private final File file;
    private final UUID ownerId;
}
