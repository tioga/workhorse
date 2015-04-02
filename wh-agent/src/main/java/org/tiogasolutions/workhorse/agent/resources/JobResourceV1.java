package org.tiogasolutions.workhorse.agent.resources;

import org.crazyyak.dev.common.IoUtils;
import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.StringUtils;
import org.tiogasolutions.workhorse.agent.entities.JobEntity;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;
import org.tiogasolutions.workhorse.pub.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobResourceV1 {

  private final ExecutionContextManager ecm;
  private final JobEntity jobEntity;

  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public JobResourceV1(ExecutionContextManager ecm, JobEntity jobEntity) {
    this.ecm = ecm;
    this.jobEntity = jobEntity;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Job getJob() throws Exception {
    return jobEntity.toJob();
  }


  @POST
  @Path("/execute")
  @Produces(MediaType.APPLICATION_JSON)
  public JobExecution execute(@QueryParam("callbackUrl") String callbackUrl) throws Exception {

    if (StringUtils.isNotBlank(callbackUrl)) {
      executor.submit( () -> executeJob(callbackUrl) );
      return JobExecution.pending();

    } else {
      return executeJob(callbackUrl);
    }
  }

  private JobExecution executeJob(String callbackUrl) throws Exception {
    List<JobActionResult> results = new ArrayList<>();

    for (JobAction jobAction : jobEntity.getJobActions()) {
      ActionType actionType = jobAction.getActionType();
      if (actionType.isOsCommand()) {
        JobActionResult result = processOsCommand(jobAction);
        results.add(result);

      } else {
        String msg = String.format("The action type \"%s\" is not supported.", actionType);
        throw new UnsupportedOperationException(msg);
      }
    }

    return JobExecution.completed(results);
  }

  private JobActionResult processOsCommand(JobAction jobAction) throws Exception {
    String command = jobAction.getCommand();
    List<String> commands = splitCommand(command);
    String[] commandArray = ReflectUtils.toArray(String.class, commands);

    File workingDir = jobAction.getWorkingDirectory();

    Process process = new ProcessBuilder()
      .command(commandArray)
      .directory(workingDir)
      .redirectErrorStream(true)
      .start();

    process.waitFor(jobAction.getTimeout(), jobAction.getTimeoutUnit());

    int exitValue = process.exitValue();
    String output = IoUtils.toString(process.getInputStream());

    return new JobActionResult(exitValue, output);
  }

  public static List<String> splitCommand(String command) {

    boolean inString = false;
    List<String> commands = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    for (char chr : command.toCharArray()) {
      if (inString) {
        if (chr != '"') {
          builder.append(chr);
        } else {
          // we are closing a string.
          inString = false;
          finish(builder, commands);
        }
      } else if (chr == '"') {
        // We are starting a string
        inString = true;

      } else if (Character.isWhitespace(chr)) {
        finish(builder, commands);

      } else {
        builder.append(chr);
      }
    }

    finish(builder, commands);

    return commands;
  }

  private static void finish(StringBuilder builder, List<String> commands) {
    if (builder.length() > 0) {
      commands.add(builder.toString());
    }
    builder.delete(0, builder.length());
  }
}
