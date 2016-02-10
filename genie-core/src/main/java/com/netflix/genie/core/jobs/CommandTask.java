package com.netflix.genie.core.jobs;

import com.netflix.genie.common.exceptions.GenieException;
import com.netflix.genie.common.exceptions.GenieServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Implementation of the workflow task for processing command information.
 *
 * @author amsharma
 */
@Slf4j
public class CommandTask extends GenieBaseTask implements WorkflowTask {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeTask(
        @NotNull
        final Context context
    ) throws GenieException {
        log.info("Execution Command Task in the workflow.");

        final JobExecutionEnvironment jobExecEnv =
            (JobExecutionEnvironment) context.getAttribute(JOB_EXECUTION_ENV_KEY);

        if (jobExecEnv == null) {
            throw new GenieServerException("Cannot run application task as jobExecutionEnvironment is null");
        }

        final String jobLauncherScriptPath = jobExecEnv.getJobWorkingDir() + "/" + GENIE_JOB_LAUNCHER_SCRIPT;
        final Writer writer = getWriter(jobLauncherScriptPath);

        createDirectory(jobExecEnv.getJobWorkingDir() + "/command/" + jobExecEnv.getCommand().getId());
        final String commandSetupFile = jobExecEnv.getCommand().getSetupFile();

        if (commandSetupFile != null && StringUtils.isNotBlank(commandSetupFile)) {
            final Path setupFilePath = new File(commandSetupFile).toPath();
            final String setupFileLocalPath = jobExecEnv.getJobWorkingDir()
                + "/applications/"
                + jobExecEnv.getCommand().getId()
                + "/"
                + setupFilePath.getFileName();
            appendToWriter(writer, "source " + setupFileLocalPath + ";");

        }
        closeWriter(writer);
    }
}
