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
package us.bekwam.guestbook.profanity.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;

/**
 * @author carl
 */
@Stateless
public class ConfigDAO {

    private Logger log = LoggerFactory.getLogger(ConfigDAO.class);

    @Resource(lookup = "java:jboss/datasources/GuestbookDS")
    private DataSource ds;

    private final String SQL = "SELECT name, value FROM bkg_config";

    public Config getConfig() {
        Config cfg = new Config();
        try (
                Connection c = ds.getConnection();
                Statement stmt = c.createStatement();
                ) {
            try (
                    ResultSet rs = stmt.executeQuery(SQL)
                    ) {
                while( rs.next() ) {
                    String name = rs.getString(1);
                    String value = rs.getString(2);

                    if(StringUtils.equalsIgnoreCase(name,
                            "us.bekwam.guestbook.profanity.client.WebPurifyClient.api_key") ) {
                        cfg.setWebPurifyAPIKey(value);
                    }
                    if (StringUtils.equalsIgnoreCase(name,
                            "us.bekwam.guestbook.profanity.client.WebPurifyClient.url")) {
                        cfg.setWebPurifyURL(value);
                    }
                }
            }
        } catch(SQLException exc) {
            log.error("can't read from bkg_config table", exc);
        }
        return cfg;
    }
}
