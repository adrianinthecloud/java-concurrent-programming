package com.osfocus.www.basic.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ABAStack {
    AtomicReference<Node> head = new AtomicReference<>();
    public void push(Node newNode) {
        Node oldNode;
        do {
            oldNode = head.get();
            newNode.next = oldNode;
        } while (!head.compareAndSet(oldNode, newNode));
    }

    public Node pop(int time) {
        Node popNode = null;
        Node newHead = null;
        do {
            popNode = head.get();
            if (popNode == null) return null;
            newHead = popNode.next;
            if (popNode.item.equalsIgnoreCase("B"))
            System.out.println("I have a Node B with value = " + popNode.value);

            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!head.compareAndSet(popNode, newHead));
        return popNode;
    }

    public static void main(String args[]) {
        ABAStack abaStack = new ABAStack();
        abaStack.push(new Node("A"));
        Node b = new Node("B");
        b.value = 999;
        abaStack.push(b);

        Thread thread1 = new Thread(() -> {
            Node B = abaStack.pop(5);
            System.out.println("Thread 1 - B = " + B.value);
        });

        Thread thread2 =  new Thread(() -> {
            Node B = abaStack.pop(0);
            B.value = 111;
            abaStack.push(new Node("C"));
            abaStack.push(new Node("D"));
            abaStack.push(B);
        });

        thread1.start();thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("D = " + abaStack.pop(0).item);
        System.out.println("C = " + abaStack.pop(0).item);
    }
}
