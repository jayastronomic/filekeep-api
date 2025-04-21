package com.example.filekeep.dtos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.filekeep.enums.LinkAccessType;
import com.example.filekeep.models.ShareableLink;

import lombok.Getter;

@Getter
public class ShareableLinkData {
    private final String shareableUrl;
    private final LinkAccessType linkAccessType;
    private final String token;

    public ShareableLinkData(ShareableLink link){
        this.shareableUrl = "/s/" + 
                            generateShareableUrl(link.getToken()) + 
                            "/" + 
                            getAssetName(link) +
                            "?" +
                            getQueryParams(link);
        this.linkAccessType = link.getAccessType();
        this.token = link.getToken();
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
