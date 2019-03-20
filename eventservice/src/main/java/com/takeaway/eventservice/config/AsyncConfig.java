package com.takeaway.eventservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.Executor;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:24
 * <p/>
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "config")
@Validated
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private boolean asyncEnabled = true;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public Executor getAsyncExecutor()
    {
        if (asyncEnabled)
        {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.initialize();
            return executor;
        }
        else
        {
            return new SyncTaskExecutor();
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
