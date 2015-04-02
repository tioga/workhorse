/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.workhorse.agent.support;

import org.crazyyak.dev.domain.query.ListQueryResult;
import org.crazyyak.dev.jackson.YakJacksonObjectMapper;
import org.crazyyak.lib.jaxrs.jackson.JacksonReaderWriterProvider;
import org.tiogasolutions.workhorse.agent.view.LocalResource;
import org.tiogasolutions.workhorse.agent.view.Thymeleaf;
import org.tiogasolutions.workhorse.pub.Job;
import org.tiogasolutions.workhorse.pub.JobActionResult;
import org.tiogasolutions.workhorse.pub.JobExecution;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WhReaderWriterProvider extends JacksonReaderWriterProvider {

  public WhReaderWriterProvider(@Context Application application) {
    super(new YakJacksonObjectMapper(), Arrays.asList(MediaType.APPLICATION_JSON_TYPE));
    addSupportedType(Job.class);
    addSupportedType(JobExecution.class);

    addSupportedType(ListQueryResult.class);

    addSupportedType(Thymeleaf.class);
    addSupportedType(LocalResource.class);
  }
}
