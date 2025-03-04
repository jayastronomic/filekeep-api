package com.example.filekeep.models;

import lombok.Getter;

@Getter
public class Asset<T> extends ApplicationEntity<T> {
    private ShareableLink shareableLink;
}
