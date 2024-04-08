package com.example.SMS.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String requestType;
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;


    public String generateBase64Image() {
        return Base64.encodeBase64String(this.image);
    }


}