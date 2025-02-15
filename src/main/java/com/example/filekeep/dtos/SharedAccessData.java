package com.example.filekeep.dtos;

import java.util.List;

import com.example.filekeep.models.SharedAccess;

public class SharedAccessData {
    public static List<SharedAccessData> transform(List<SharedAccess> sharedAssets){
       return sharedAssets.stream().map(sharedAsset -> {
            if(sharedAsset.getFile() != null){
                return new SharedAccesFile(sharedAsset);
            } else {
                return new SharedAccesFolder(sharedAsset);
            }
        }).toList();
    }
}
