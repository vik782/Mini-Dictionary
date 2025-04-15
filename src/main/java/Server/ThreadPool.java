/**
 * Vincent Kurniawan
 *
 * ThreadPool.java: Thread Pool to store worker threads
 *
 */

package Server;

import Response.ErrorExceptions;
import Server.DictionaryThread;

import java.util.concurrent.PriorityBlockingQueue;


public class ThreadPool {

    private final static int poolSize = 5;
    private final static int queueLimit = 10;

    private final DictionaryThreads[] dictionaryThreads;

    private PriorityBlockingQueue<DictionaryThread> dictQueue;

    private class DictionaryThreads extends Thread {

        @Override
        public void run() {
            while (true) {
                DictionaryThread threadTask = null;
                synchronized (dictQueue) {
                    while (dictQueue.isEmpty()) {

                        // waits till a task thread is inputted into queue
                        try {
                            dictQueue.wait();
                        } catch (InterruptedException e) {
                            ErrorExceptions.priorityBlockingQueueError(e);
                        }
                    }

                    // pop task from the queue
                    try {
                        threadTask = dictQueue.take();
                    } catch (InterruptedException e) {
                        ErrorExceptions.priorityBlockingQueueError(e);
                    }

                }

                // execute task of thread
                try {
                    threadTask.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }

    public ThreadPool() {
        this.dictQueue = new PriorityBlockingQueue<>(queueLimit);
        dictionaryThreads = new DictionaryThreads[poolSize];

        for (int i = 0; i < poolSize; i++) {
            dictionaryThreads[i] = new DictionaryThreads();
            dictionaryThreads[i].start();
        }
    }

    public void execute(DictionaryThread task) {
        synchronized (dictQueue) {
            // task added to queue
            dictQueue.add(task);

            // notify dictionary threads that a task is added
            dictQueue.notify();
        }
    }

    public void shutdown(){
        // set each worker thread to null
        for (int i = 0; i < poolSize; i++) {
            dictionaryThreads[i] = null;
        }
    }

    public static int getPoolSize(){
        return poolSize;
    }

    public static int getQueueLimit(){
        return queueLimit;
    }

}
