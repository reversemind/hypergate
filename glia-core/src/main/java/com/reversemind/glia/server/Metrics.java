package com.reversemind.glia.server;
//
//import org.hyperic.sigar.Sigar;
//import org.hyperic.sigar.SigarException;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2013 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Measurable parameters for GliaServer - include native Sigar libs into your java
 */
public class Metrics implements Serializable {

    private Date startDate;
    private long requests = 0L;
    private double timePerRequest = 0.0d;
    private double processingTime = 0.0d;
    private double cpuIdle;

    //private Sigar sigar = null;             // include native libs in your java

    public Metrics() {
        this.startDate = new Date();
        try {
            //this.sigar = new Sigar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cpuIdle = 99.0d;
    }

    // TODO need to rename method
    public void addRequest(long deltaTimePerRequest) {
        synchronized (this) {
            requests++;

            this.processingTime += deltaTimePerRequest;
            if (this.requests > 0) {
                this.timePerRequest = (this.processingTime / this.requests);
            }

            // just update CPU load
            this.getCpuIdle();
        }
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - this.startDate.getTime();
    }

    public double getTimePerRequest() {
        return timePerRequest;
    }

    public void setTimePerRequest(double timePerRequest) {
        this.timePerRequest = timePerRequest;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getRequests() {
        return requests;
    }

    public double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(double processingTime) {
        this.processingTime = processingTime;
    }

    /**
     * Get CPU idle in per cents using Sigar lib
     *
     * @return
     */
    public double getCpuIdle() {
        try {
            this.cpuIdle = 90.0d;//sigar.getCpuPerc().getIdle();
        } catch (Exception e) {
            // just hide exception - other just use special LOG level
        }
        return cpuIdle;
    }

    public void setCpuIdle(double cpuIdle) {
        this.cpuIdle = cpuIdle;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "startDate=" + startDate +
                ", requests=" + requests +
                ", timePerRequest=" + timePerRequest +
                ", processingTime=" + processingTime +
                ", cpuIdle=" + cpuIdle + "% " +
                '}';
    }
}
