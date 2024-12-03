package org.example;

public class Test {
    String test = "test";

    public String getTest() {
        return test;
    }

    public static void main(String[] args) {
        System.out.println(new Test().getTest());
    }
}
