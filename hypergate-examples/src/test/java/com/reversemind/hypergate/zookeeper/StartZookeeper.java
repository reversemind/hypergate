package com.reversemind.hypergate.zookeeper;

/**
 *
 */
public class StartZookeeper {

    public static void main(String... args) throws InterruptedException {
        String port = "2181";
        String dataDirectory = System.getProperty("java.io.tmpdir");

        EmbeddedZookeeper.start(new String[]{port, dataDirectory});
        System.out.println(dataDirectory);

        Thread.sleep(60000);
        EmbeddedZookeeper.stop();
    }

}
