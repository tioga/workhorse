/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.workhorse.agent.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tiogasolutions.workhorse.agent.WhApplication;
import org.tiogasolutions.workhorse.agent.entities.JobStore;
import org.tiogasolutions.workhorse.agent.support.ExecutionContextManager;
import org.tiogasolutions.workhorse.agent.support.WhCouchServer;
import org.tiogasolutions.workhorse.agent.view.Thymeleaf;
import org.tiogasolutions.workhorse.agent.view.ThymeleafViewFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("/")
public class RootResourceV1 extends RootResourceSupport {

  private static final Log log = LogFactory.getLog(RootResourceV1.class);

  private @Context UriInfo uriInfo;

  // TODO inject this resource.
  private final JobStore jobStore;

  // TODO inject this resource
  private final ExecutionContextManager ecm = WhApplication.executionContextManager;

  public RootResourceV1() throws IOException {
    log.info("Created ");

    WhCouchServer couchServer = new WhCouchServer();
    jobStore = new JobStore(couchServer);

//    JobEntity entity = JobEntity.newEntity(
//      new Action(ActionType.osCommand,
//      "echo I love you too > test.txt",
//      "c:\tmp", Long.MAX_VALUE, TimeUnit.SECONDS)
//    );
//    jobStore.create(entity);

  }

  @Override
  public UriInfo getUriInfo() {
    return uriInfo;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getWelcome() throws Exception {
    return new Thymeleaf(ThymeleafViewFactory.WELCOME);
  }

  @GET @Path("/ping")
  @Produces(MediaType.TEXT_HTML)
  public Response healthCheck$GET() {
    return Response.status(Response.Status.OK).build();
  }

  @Path("/api/v1/client")
  public ClientResourceV1 getClientResource() throws IOException {
    return new ClientResourceV1(ecm, jobStore);
  }
}

