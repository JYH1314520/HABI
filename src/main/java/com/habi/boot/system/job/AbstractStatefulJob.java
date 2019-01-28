package com.habi.boot.system.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class AbstractStatefulJob extends AbstractJob {
    public AbstractStatefulJob() {
    }
}
