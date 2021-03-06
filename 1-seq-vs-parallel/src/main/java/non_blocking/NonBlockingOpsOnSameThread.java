package non_blocking;

import util.BlockingOps;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * Do the operations on same thread with .thenApply
 * [ORIGINAL THREAD]: ForkJoinPool.commonPool-worker-3
 * [THREAD]: ForkJoinPool.commonPool-worker-3
 * [THREAD]: ForkJoinPool.commonPool-worker-3
 * https://stackoverflow.com/a/47489654/432903
 */
public class NonBlockingOpsOnSameThread {

    private static final ForkJoinPool DEFAULT_EXECUTOR = ForkJoinPool.commonPool();

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("[ORIGINAL THREAD]: " + Thread.currentThread().getName());
            return blockingOps1(2);
        }, DEFAULT_EXECUTOR).thenApply($ -> {
            System.out.println("[THREAD]: " + Thread.currentThread().getName());
            return blockingOps2($);
        }).thenApply($ -> {
            System.out.println("[THREAD]: " + Thread.currentThread().getName());
            return blockingOps2($);
        }).join();
    }

    private static int blockingOps1(int i) {
        BlockingOps.block(1 * 1000);
        return i * 2;
    }

    private static int blockingOps2(int i) {
        BlockingOps.block(1 * 1000);
        return i * 3;
    }
}
