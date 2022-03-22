package com.jp.boiler.base.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class Product {

    protected int id;
    protected String name;
    protected String nick;

}


