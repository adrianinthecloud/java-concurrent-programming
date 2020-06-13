package com.osfocus.www.basic.threads_interation;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using PipedStream
 * really slow
 */
public class PipedStreamForThreadsInteraction {
    public static void main(String[] args) throws Exception {
        PipedInputStream input1 = new PipedInputStream();
        PipedInputStream input2 = new PipedInputStream();
        PipedOutputStream output1 = new PipedOutputStream();
        PipedOutputStream output2 = new PipedOutputStream();

        input1.connect(output1);
        input2.connect(output2);

        String msg = "Next";

        new Thread(() -> {
            byte[] buffer = new byte[msg.length()];
            Character baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                System.out.print((char) (baseLetter+i));

                try {
                    output2.write(msg.getBytes());
                    input1.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (new String(buffer).equals(msg)) {
                    continue;
                }
            }
        }).start();

        new Thread(() -> {
            byte[] buffer = new byte[msg.length()];

            for (int i = 1; i <= 7; i++) {
                try {
                    input2.read(buffer);
                    if (new String(buffer).equals(msg)) {
                        System.out.print(i);
                    }
                    output1.write(msg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
