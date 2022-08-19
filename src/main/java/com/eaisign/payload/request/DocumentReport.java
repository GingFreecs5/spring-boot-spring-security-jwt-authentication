package com.eaisign.payload.request;

import com.eaisign.models.Signataire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentReport  {
    private String nom;
    private String signataire;
    private String status ;
    private Date dateAjout;
    private Date dateDernierModification;

}
