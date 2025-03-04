package com.example.filekeep.models;

import java.security.SecureRandom;
import java.util.Base64;

import com.example.filekeep.enums.LinkAccessType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "shareable_links"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShareableLink extends ApplicationEntity<ShareableLink> {

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    private LinkAccessType accessType;

    @PrePersist
    public void generateToken() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);
        this.token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        this.accessType = LinkAccessType.PUBLIC;
    }
}
