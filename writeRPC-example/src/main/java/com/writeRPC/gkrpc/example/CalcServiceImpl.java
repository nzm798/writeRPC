package com.writeRPC.gkrpc.example;

public class CalcServiceImpl implements CalcService{
    public int add(int a, int b) {
        return a+b;
    }

    public int minus(int a, int b) {
        return a-b;
    }
}
