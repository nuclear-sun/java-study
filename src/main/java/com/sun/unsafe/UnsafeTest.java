package com.sun.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {

    private static final Unsafe unsafe;

    static {

        try {
            Field fieldUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            fieldUnsafe.setAccessible(true);
            unsafe = (Unsafe)fieldUnsafe.get(null);
        } catch (Exception e) {
            throw new Error("cannot happen");
        }
    }

    // all result 16
    private static void testArrayOffsets() {

        int byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);
        int intArrayOffset = unsafe.arrayBaseOffset(int[].class);
        int shortArrayOffset = unsafe.arrayBaseOffset(short[].class);
        int longArrayOffset = unsafe.arrayBaseOffset(long[].class);

        System.out.println("byteArrayOffset: " + byteArrayOffset);
        System.out.println("shortArrayOffset: " + shortArrayOffset);
        System.out.println("intArrayOffset: " + intArrayOffset);
        System.out.println("longArrayOffset: " + longArrayOffset);
    }


    private static void putInt(Object baseObject, long baseOffset, int value) {
        unsafe.putInt(baseObject, baseOffset, value);
    }

    private static void putIntInArray(byte[] target, int index, int value) {

        if(index < 0 || index >= (target.length - 1)/2){
            throw new ArrayIndexOutOfBoundsException();
        }

        int baseOffset = unsafe.arrayBaseOffset(byte[].class);
        int offset = baseOffset + index * 2;
        unsafe.putInt(target, (long)offset, value);
    }

    private static void putBytesInArray(byte[] target, long offset, byte[] content) {
        if(offset < 0 || offset + content.length >= target.length) {
            throw new ArrayIndexOutOfBoundsException("offset: " + offset + ", content length: " + content.length);
        }

        int baseOffset = unsafe.arrayBaseOffset(byte[].class);
        long realOffset = baseOffset + offset;

        for (int i = 0; i < content.length; i++) {
            unsafe.putByte(target, realOffset + i, content[i]);
        }
    }

    private static void getIntInArray(byte[] target, int index) {

        if(index < 0 || index >= (target.length - 1)/2){
            throw new ArrayIndexOutOfBoundsException();
        }

    }

    public static void main(String[] args) {

        long addr = unsafe.allocateMemory(40);
        unsafe.putInt(addr, 23);
        unsafe.putInt(addr+4, 24);
        unsafe.putInt(addr+8, 25);
        int anInt = unsafe.getInt(addr+4);
        System.out.println(anInt);
        unsafe.freeMemory(addr);
    }
}
