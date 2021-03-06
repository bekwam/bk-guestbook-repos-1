/*
 * Copyright 2021 Bekwam, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.bekwam.guestbook.api.rs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Config;
import us.bekwam.guestbook.api.ejb.ConfigBean;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author carl
 */
@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    private Logger log = LoggerFactory.getLogger(ConfigResource.class);

    @Inject
    ConfigBean configBean;

    @GET
    public List<Config> findAll() { return configBean.findAll(); }

    @POST
    public Long create(Config config) { return configBean.create(config); }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") Long id, Config config) {
        if( log.isDebugEnabled() ) {
            log.debug("[UPDATE] updating config record id={}", id);
        }
        config.setId(id);
        configBean.update(config);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        configBean.delete(id);
    }
}
