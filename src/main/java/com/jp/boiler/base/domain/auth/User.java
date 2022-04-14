package com.jp.boiler.base.domain.auth;

import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.domain.BaseEntity;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "USER_INFO")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public void encodePwd(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.password = bCryptPasswordEncoder.encode(this.password);
    }

}
