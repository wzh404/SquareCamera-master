package com.wannengyongyao.drug.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

/**
 * C端token存储对象
 */
@Data
public class JwtObject {
    private long userId;
    private Long timestamp;

    public JwtObject(){
        this.timestamp = System.currentTimeMillis();
    }

    public JwtObject(long userId){
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public static JwtObject of(String json){
        try {
            return (new ObjectMapper()).readValue(json, JwtObject.class);
        } catch (IOException e) {
            return null;
        }
    }

    public String toJson(){
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }
}
