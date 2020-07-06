package com.osfocus.www.basic.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABANoIssue {
    AtomicStampedReference<Node> head = new AtomicStampedReference<>(null, 0);

    public void push(Node newNode) {
        Node oldNode = null;
        int oldStamp = 0;
        do {
            oldStamp = head.getStamp();
            oldNode = head.getReference();
            newNode.next = oldNode;
        } while (!head.compareAndSet(oldNode, newNode, oldStamp, oldStamp+1));
    }

    public Node pop(int time) {
        Node popNode = null;
        Node newHead = null;
        int oldStamp = 0;
        do {
            oldStamp = head.getStamp();
            popNode = head.getReference();
            newHead = popNode.next;

            if (popNode.item.equalsIgnoreCase("B"))
                System.out.println("we have a node B with value = " + popNode.value);

            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!head.compareAndSet(popNode, newHead, oldStamp, oldStamp+1));

        return popNode;
    }

    public static void main(String args[]) {
        ABANoIssue aba = new ABANoIssue();
        aba.push(new Node("A"));
        Node b = new Node("B");
        b.value = 999;
        aba.push(b);

        Thread t1 = new Thread(() -> {
           Node pop = aba.pop(5);
           System.out.println(Thread.currentThread().getName() + " pop b with value = " + pop.value);
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            Node pop = aba.pop(0);
            aba.push(new Node("C"));
            aba.push(new Node("D"));
            aba.push(pop);
        });

        t1.start();t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("we pop D = " + aba.pop(0).item);
        System.out.println("we pop C = " + aba.pop(0).item);
    }
}
