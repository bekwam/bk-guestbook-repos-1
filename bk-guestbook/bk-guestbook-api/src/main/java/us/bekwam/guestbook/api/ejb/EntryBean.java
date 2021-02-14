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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author carl
 */
@Stateless
public class EntryBean {

    private Logger log = LoggerFactory.getLogger(EntryBean.class);

    @PersistenceContext
    EntityManager em;

    public List<Entry> getEntries(int page, int nrecs) {
        TypedQuery<Entry> q = em.createQuery(
                "SELECT e FROM Entry e WHERE e.state = us.bekwam.guestbook.api.domain.EntryStateType.APPROVED ORDER BY e.createdOn DESC",
                Entry.class
        );
        q.setFirstResult(page-1);
        q.setMaxResults(nrecs);
        return q.getResultList();
    }

    public Entry addEntry(Entry e) {
        e.setCreatedOn(ZonedDateTime.now());
        em.persist(e);
        return e;
    }

    public Optional<Entry> findEntryById(Long id) {
        try {
            return Optional.of(em.find(Entry.class, id));
        } catch(NoResultException exc) {
            if( log.isDebugEnabled() ) {
                log.debug("[FIND ENTRY] no entry record for id={}", id);
            }
        }
        return Optional.empty();
    }

    public void updateEntry(Entry e) { }
}
