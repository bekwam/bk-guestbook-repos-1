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
package us.bekwam.guestbook.profanity.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import us.bekwam.guestbook.profanity.dao.Config;
import us.bekwam.guestbook.profanity.dao.ConfigDAO;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * @author carl
 */
@Stateless
public class WebPurifyClient implements ProfanityFilterClient {

    private final String FORMAT = "json";
    private final String METHOD = "webpurify.live.check";

    @Inject
    ConfigDAO configDAO;

    private Config config;

    @PostConstruct
    public void init() {
        this.config = configDAO.getConfig();
    }

    @Override
    public String filterText(String input) {

        if( StringUtils.isEmpty(config.getWebPurifyAPIKey())
            || StringUtils.isEmpty(config.getWebPurifyURL()) ) {
            throw new IllegalStateException("bkg_config missing records for webpurify config");
        }

        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target(config.getWebPurifyURL())
                .queryParam("api_key", config.getWebPurifyAPIKey())
                .queryParam("method", METHOD)
                .queryParam("text", input)
                .queryParam("format", FORMAT);

        WebPurifyResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .get(WebPurifyResponse.class);

        String found_s = response.getRsp().getFound();

        if( NumberUtils.toInt(found_s) > 0 ) {
            return found_s;
        }

        return "";
    }
}
