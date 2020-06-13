package com.osfocus.www.basic.threads_interation;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using TransferQueue.
 */
public class TransferQueueForThreadsInteraction {
    public static void main(String[] args) {
        char[] letters = "ABCDEFG".toCharArray();
        char[] numbers = "1234567".toCharArray();

        TransferQueue<Character> queue = new LinkedTransferQueue<>();
        new Thread(() -> {
            for (char number : numbers) {
                try {
                    System.out.print(queue.take());
                    queue.transfer(number);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            for (char letter : letters) {
                try {
                    queue.transfer(letter);
                    System.out.print(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();
    }
}
