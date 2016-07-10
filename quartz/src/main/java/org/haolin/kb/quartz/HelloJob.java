package org.haolin.kb.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Author: haolin
 * On: 3/3/15
 */
public class HelloJob implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("This is hello job.");
    }
}
