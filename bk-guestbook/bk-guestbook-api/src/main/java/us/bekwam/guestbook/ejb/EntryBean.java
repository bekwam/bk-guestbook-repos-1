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
package us.bekwam.guestbook.ejb;

import us.bekwam.guestbook.entity.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author carl
 */
@Stateless
public class EntryBean {

    @PersistenceContext
    EntityManager em;

    public List<Entry> getEntries(int page) {
        TypedQuery<Entry> q = em.createQuery("SELECT e FROM Entry e ORDER BY e.createdOn DESC", Entry.class );
        return q.getResultList();
    }

    public Entry addEntry(Entry e) {
        e.setCreatedOn(ZonedDateTime.now());
        em.persist(e);
        return e;
    }
}
