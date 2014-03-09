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
public class SigarCPU {

    private static final Logger LOG = LoggerFactory.getLogger(SigarCPU.class);

    public static void main(String... args) throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();

        Cpu cpu = sigar.getCpu();
        LOG.debug("CPU: " + cpu.toString());

        CpuPerc cpuPerc = sigar.getCpuPerc();
        LOG.debug(cpuPerc.toString());

        CpuInfo[] cpuInfo = sigar.getCpuInfoList();
        for (CpuInfo temp : cpuInfo) {
            LOG.debug(temp.toString());
        }

        while (true) {
            LOG.debug("" + sigar.getCpuPerc().getIdle());
            Thread.sleep(300);
        }
    }

}
