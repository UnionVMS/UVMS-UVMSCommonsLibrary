/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.service.interceptor;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@Slf4j
public class TracingInterceptor {

    @AroundInvoke
    public Object logTraceAndPerformance(InvocationContext context) throws Exception {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            log.info("START {} {}", context.getMethod().getName(), Arrays.toString(context.getParameters()));
            return context.proceed();
        }
        finally{
            log.info("STOP Elapsed time ==> {}", stopwatch);
        }
    }
}