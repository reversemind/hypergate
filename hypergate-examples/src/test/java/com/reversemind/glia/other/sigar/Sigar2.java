package com.reversemind.glia.other.sigar;

import org.hyperic.sigar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Sigar2 {

    private static final Logger LOG = LoggerFactory.getLogger(Sigar2.class);

    public static void main(String... args) throws Exception {
        Sigar sigar = new Sigar();

        // enabling logging in the native Sigar code (log4j is required)
        sigar.enableLogging(true);

        Mem mem = sigar.getMem();
        LOG.debug(mem.toString());

        Swap swap = sigar.getSwap();
        LOG.debug(swap.toString());
        LOG.debug("Total swap in readable format: " + Sigar.formatSize(swap.getTotal()));

        Cpu cpu = sigar.getCpu();
        LOG.debug("CPU: " + cpu.toString());

        CpuPerc cpuPerc = sigar.getCpuPerc();
        LOG.debug(cpuPerc.toString());

        CpuInfo[] cpuInfo = sigar.getCpuInfoList();
        for (CpuInfo temp : cpuInfo) {
            LOG.debug(temp.toString());
        }

        ResourceLimit rLimit = sigar.getResourceLimit();
        // a range of values includes core, cpu, mem, opened files etc and depends on platform
        LOG.debug("ResourceLimit: " + rLimit.toString());

        LOG.debug("System uptime in seconds: " + sigar.getUptime().toString());

        long pid = sigar.getPid();
        // also it's possible to get a list of pids

        ProcState pState = sigar.getProcState(pid);
        LOG.debug(pState.toString());
        // also you can get proc mem, state, time, cpu, credentials, descriptors,
        // current working directory, arguments, environment etc by PID

        int port = 12080;
        long pidByPort = sigar.getProcPort(NetFlags.CONN_TCP, port);
        LOG.debug("Name of the process which uses port " + port + ": "
                + sigar.getProcState(pidByPort).getName());

        FileSystem[] fileSystems = sigar.getFileSystemList();
        FileSystemUsage fsu = sigar.getFileSystemUsage(fileSystems[1].getDevName());
        LOG.debug("File system usage: " + fsu.toString());
        //you can find disk reads/writes, total/avail/free size, disk queue etc
    }

}
