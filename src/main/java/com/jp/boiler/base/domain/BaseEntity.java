package com.jp.boiler.base.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "reg_id", length = 100)
    private String regId;

    @CreatedDate
    @Column(name = "reg_dtm", nullable = false)
    private LocalDateTime regDtm;

    @LastModifiedBy
    @Column(name = "mod_id", length = 100)
    private String modId;

    @LastModifiedDate
    @Column(name = "mod_dtm", nullable = false)
    private LocalDateTime modDtm;

}
