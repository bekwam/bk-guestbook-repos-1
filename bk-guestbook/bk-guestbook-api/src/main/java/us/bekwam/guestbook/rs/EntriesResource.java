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
package us.bekwam.guestbook.rs;

import org.apache.commons.lang3.math.NumberUtils;
import us.bekwam.guestbook.ejb.EntryBean;
import us.bekwam.guestbook.entity.Entry;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author carl
 */
@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntriesResource {

    @Inject
    EntryBean entryBean;

    @GET
    public List<Entry> getEntries(@QueryParam("page") @DefaultValue("0") String page_s) {
        return entryBean.getEntries(NumberUtils.toInt(page_s));
    }

    @POST
    public Entry addEntry(@Valid Entry entry) {
        return entryBean.addEntry(entry);
    }
}
