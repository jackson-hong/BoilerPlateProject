package com.jp.boiler.base.common.context;

public class CryptoDataContext {

    private static final ThreadLocal<Boolean> CONTEXT = new ThreadLocal<>();

    public static void setCryptoDataContext(boolean shouldCrypto) {CONTEXT.set(shouldCrypto);}

    public static boolean getCryptoDataContext(){
        final Boolean b = CONTEXT.get();
        return b != null ? b : false;
    }

    public static void clear(){CONTEXT.remove();}
}
