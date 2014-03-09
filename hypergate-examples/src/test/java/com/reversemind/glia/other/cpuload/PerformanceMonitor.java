package com.reversemind.glia.other.cpuload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.ManagementFactory;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PerformanceMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(PerformanceMonitor.class);

    static long lastSystemTime = 0;
    static long lastProcessCpuTime = 0;
    public static int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();

    public synchronized double getCpuUsage() {
        ManagementFactory.getThreadMXBean().setThreadCpuTimeEnabled(true);
        if (lastSystemTime == 0) {
            baselineCounters();
            //  return ;
        }

        long systemTime = System.nanoTime();
        long processCpuTime = 0;

        processCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        double cpuUsage = (double) (processCpuTime - lastProcessCpuTime) / (systemTime - lastSystemTime) * 100.0;

        lastSystemTime = systemTime;
        lastProcessCpuTime = processCpuTime;

        return cpuUsage / availableProcessors;
    }

    static float javacpu = 0;
    static double uptime = 0;

    public void _getJavaRuntime() {

        LOG.debug("!!!" + ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());

//        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//        RuntimeMXBean runbean = (RuntimeMXBean) ManagementFactory.getRuntimeMXBean();
//        int nCPUs = osbean.getAvailableProcessors();
//        long prevUpTime = runbean.getUptime();
//        long prevProcessCpuTime = osbean.getProcessCpuTime();
//        try {
//            Thread.sleep(1);
//        } catch (Exception e) { }
//
//        osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//        long upTime = runbean.getUptime();
//        long processCpuTime = osbean.getProcessCpuTime();
//        if (prevUpTime > 0L && upTime > prevUpTime) {
//            long elapsedCpu = processCpuTime - prevProcessCpuTime;
//            long elapsedTime = upTime - prevUpTime;
//            javacpu = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * nCPUs));
//        } else {
//            javacpu = 0.001f;
//        }
//        uptime = runbean.getUptime();
    }

    private void baselineCounters() {
        lastSystemTime = System.nanoTime();

        //lastProcessCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        lastProcessCpuTime = 0;
        long[] ids = ManagementFactory.getThreadMXBean().getAllThreadIds();
        for (int i = 0; i < ids.length; i++) {
            if (ManagementFactory.getThreadMXBean().getThreadCpuTime(ids[i]) >= 0) {
                lastProcessCpuTime += ManagementFactory.getThreadMXBean().getThreadCpuTime(ids[i]);
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
//        LOG.debug(Runtime.getRuntime().availableProcessors());
//        System.exit(0);
        PerformanceMonitor monitor = new PerformanceMonitor();
        while (true) {
            //Thread.sleep(1000);
            start();
            double usage = monitor.getCpuUsage();
            monitor._getJavaRuntime();
            LOG.debug("Current CPU usage in per cents : " + usage + " - " + javacpu + " " + uptime);
        }
    }


//
//    static long lastSystemTime = 0;
//    static long lastProcessCpuTime = 0;
//    public static int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
//
//    // public static int  availableProcessors = Runtime.getRuntime().availableProcessors();
//    public synchronized double getCpuUsage() {
//        ManagementFactory.getThreadMXBean().setThreadCpuTimeEnabled(true);
//        if (lastSystemTime == 0) {
//            baselineCounters();
//            //  return ;
//        }
//
//        long systemTime = System.nanoTime();
//        long processCpuTime = 0;
//
//        processCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
//        double cpuUsage = (double) (processCpuTime - lastProcessCpuTime) / (systemTime - lastSystemTime) * 100.0;
//
//        lastSystemTime = systemTime;
//        lastProcessCpuTime = processCpuTime;
//
//        return cpuUsage / 4;// / availableProcessors;
//    }
//
//    private void baselineCounters() {
//        lastSystemTime = System.nanoTime();
//
//        lastProcessCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
//    }
//
//    public static void main(String[] args) {
////        LOG.debug(Runtime.getRuntime().availableProcessors());
////        System.exit(0);
//        PerformanceMonitor monitor = new PerformanceMonitor();
//        for (int i = 0; i < 10000; i++) {
//            start();
//            double usage = monitor.getCpuUsage();
//            if (usage != 0) LOG.debug("Current CPU usage in % : " + usage);
//        }
//    }

    private static void start() {
        int count = 0;
        for (int i = 0; i < 10000000; i++) {
            count = (int) Math.random() * 100;
        }
    }
}

