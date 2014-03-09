package ejb.server.service;

import ejb.shared.IServiceSimple;

import javax.ejb.Local;
import javax.ejb.Stateless;

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
@Stateless
@Local(IServiceSimple.class)
public class ServiceSimple implements IServiceSimple {

    @Override
    public String functionNumber1(String parameter1, String parameter2) {
        this.delay();
        return "FN #1 - summ par1:" + parameter1 + " par2:" + parameter2 + " thread:" + Thread.currentThread().getName();
    }

    @Override
    public String functionNumber2(String parameter1, String parameter2) {
        this.delay();
        return "FN #2 - summ par1:" + parameter1 + " par2:" + parameter2+ " thread:" + Thread.currentThread().getName();
    }

    @Override
    public String functionNumber3(String parameter1, String parameter2) {
        this.delay();
        return "FN #3 - summ par1:" + parameter1 + " par2:" + parameter2 + " thread:" + Thread.currentThread().getName();
    }

    @Override
    public String functionNumber4(String parameter1, String parameter2) {
        this.delay();
        return "FN #4 - summ par1:" + parameter1 + " par2:" + parameter2 + " thread:" + Thread.currentThread().getName();
    }

    @Override
    public String functionNumber5(String parameter1, String parameter2) {
        this.delay();
        return "FN #5 - summ par1:" + parameter1 + " par2:" + parameter2 + " thread:" + Thread.currentThread().getName();
    }

    private void delay() {
//        try {
//            Thread.sleep(Math.round(1000 * Math.random()));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
