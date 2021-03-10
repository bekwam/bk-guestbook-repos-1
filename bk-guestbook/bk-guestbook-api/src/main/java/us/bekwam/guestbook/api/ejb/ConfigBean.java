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
package us.bekwam.guestbook.api.ejb;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Config;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * @author carl
 */
@Stateless
@SecurityDomain("BKGEJBSecurityDomain")
@RolesAllowed("superuser")
public class ConfigBean {
    private Logger log = LoggerFactory.getLogger(ConfigBean.class);

    @PersistenceContext
    EntityManager em;

    public Long create(Config config) {
        em.persist(config);
        return config.getId();
    }

    public void update(Config config) {}

    public void delete(Long id) { em.remove(em.find(Config.class, id)); }

    public List<Config> findAll() {
        TypedQuery<Config> q =
                em.createQuery(
                        "SELECT c FROM Config c ORDER BY c.name", Config.class
                );
        return q.getResultList();
    }

    public Optional<Config> findById(Long id) {
        return Optional.ofNullable(em.find(Config.class, id));
    }
}