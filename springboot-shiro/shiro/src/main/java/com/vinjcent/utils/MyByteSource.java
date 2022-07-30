package com.vinjcent.utils;

import org.apache.shiro.util.SimpleByteSource;

import java.io.Serializable;

public class MyByteSource extends SimpleByteSource implements Serializable {

    public MyByteSource(byte[] bytes) {
        super(bytes);
    }
}
