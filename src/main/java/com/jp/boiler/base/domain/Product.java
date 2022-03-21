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
public class Product implements Cloneable {

    protected int id;
    protected String name;
    protected String nick;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setName(String name) {
        this.name = name;
    }

    //    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) return true;
//        if(!(obj instanceof Product)) return false;
//        Product product = (Product) obj;
//        return product.id == id
//                && product.name.equals(name)
//                && product.nick.equals(nick);
//
//    }
//
//    @Override
//    public int hashCode() {
//        // 31은 홀수이면서 소수이기 때문
//        int result = Integer.hashCode(id);
//        result = 31 * result + name.hashCode();
//        result = 31 * result + nick.hashCode();
//        return result;
//    }

//    public void changeId(){
//        this.id = id + 1;
//    }
//
    public static void main(String[] args) throws CloneNotSupportedException, JsonProcessingException {
//        Map<Product, String> map = new HashMap<>();
//        map.put(new Product(1, "jackson", "jack"), "HI!!");
//
//        System.out.println(map.get(new Product(1, "jackson", "jack")));

//        Product product = new Product(1, "jackson", "jack");
//        System.out.println(product.clone());

        TreeSet<String> s = new TreeSet<>();
        Collections.addAll(s, args);
        System.out.println(s);

        BigDecimal big1 = new BigDecimal("1.0");
        BigDecimal big2 = new BigDecimal("1.00");
        HashSet<BigDecimal> bigDecimals = new HashSet<>();
        System.out.println(big1.equals(big2));// FALSE

        bigDecimals.add(big1);
        bigDecimals.add(big2);
        System.out.println(bigDecimals.size()); // 2

        TreeSet<BigDecimal> bigDecimalTreeSet = new TreeSet<>();
        bigDecimalTreeSet.add(big1);
        bigDecimalTreeSet.add(big2);
        System.out.println(bigDecimalTreeSet.size());// 1

    }

    public int getId() {
        return id;
    }
}


