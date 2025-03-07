package com.example.filekeep.dtos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.filekeep.models.ShareableLink;

import lombok.Getter;

@Getter
public class ShareableLinkData {
    private final String shareableUrl;
    private static final String baseUrl = "http://localhost:5173/s/";

    public ShareableLinkData(ShareableLink link){
        this.shareableUrl = baseUrl + 
                            generateShareableUrl(link.getToken()) + 
                            "/" + 
                            getAssetName(link) +
                            "?" +
                            getQueryParams(link);
    }

    private String generateShareableUrl(String token) {
       return URLEncoder.encode(token, StandardCharsets.UTF_8);
    } 

    private String getAssetName(ShareableLink link){
        if(link.getFile() != null){
           return link.getFile().getFileName(); 
        } else {
           return link.getFolder().getFolderName();
        }
    }

    private String getQueryParams(ShareableLink link){
        int type = link.getFile() != null ? 0 : 1;
        return "t=" + type;
    }
}
