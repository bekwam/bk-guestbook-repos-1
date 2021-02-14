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

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Entry;
import us.bekwam.guestbook.api.ejb.EntryBean;
import us.bekwam.guestbook.api.ejb.MessageSender;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author carl
 */
@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntriesResource {

    private Logger log = LoggerFactory.getLogger(EntriesResource.class);

    @Resource
    private Integer recsPerPage;

    @Inject
    EntryBean entryBean;

    @Inject
    MessageSender messageSender;

    @GET
    public List<Entry> getEntries(@QueryParam("page") @DefaultValue("1") String page_s) {
        return entryBean.getEntries(NumberUtils.toInt(page_s), recsPerPage);
    }

    @POST
    public Response addEntry(@Valid Entry input) {
        try {
            Entry entry = entryBean.addEntry(input);
            if( log.isDebugEnabled() ) {
                log.debug("[ADD ENTRY] added entry id={}", entry.getId());
            }
            boolean submitted = messageSender.requestProfanityFilter(entry);
            if( submitted ) {
                if( log.isDebugEnabled() ) {
                    log.debug("[ADD ENTRY] entry added to queue");
                }
                return Response.accepted().build();
            }
        } catch(Exception exc) {
            log.error("error adding entry", exc);
            return Response.serverError().build();
        }
        return null;
    }
}
