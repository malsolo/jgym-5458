package io.jgym.warmups.day20;

import java.math.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class FactorialDemoParallel {
    // https://www.javaspecialists.eu/courses/blackfriday20
    private static LongAccumulator bestTime = new LongAccumulator(
            Long::min, Long.MAX_VALUE
    );
    public static void main(String... args) {
        for (int i = 0; i < 5; i++) {
            test();
        }
        System.out.printf("%nbestTime = %dms%n", (bestTime.longValue() / 1_000_000));
    }

    private static void test() {
        long time = System.nanoTime();
        try {
            BigInteger result = factorial(1_000_000);
            System.out.println("result.bitCount() = " + result.bitCount());
        } finally {
            time = System.nanoTime() - time;
            bestTime.accumulate(time);
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }

    private static BigInteger factorial(int n) {
        return factorial(0, n);
    }

    private static BigInteger factorial(int from, int to) {
        if (from == to) {
            if (from == 0) return BigInteger.ONE;
            return BigInteger.valueOf(from);
        }
        int mid = (from + to) >>> 1;
        var leftTask = new RecursiveTask<BigInteger>() {
            protected BigInteger compute() {
                return factorial(from, mid);
            }
        };
        leftTask.fork();
        var right = factorial(mid + 1, to);
        var left = leftTask.join();
        return left.multiply(right);
    }
}
