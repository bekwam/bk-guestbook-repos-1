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
package us.bekwam.guestbook.api.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author carl
 */
@Entity
@Table(name="bkg_entry")
@SequenceGenerator(name="entry_id_seq",
        sequenceName="bkg_entry_id_seq",
        allocationSize=1)
public class Entry {

    private Long id;
    private String text;
    private String createdBy;
    private ZonedDateTime createdOn;
    private Integer version;
    private EntryStateType state = EntryStateType.PENDING;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="entry_id_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name="created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name="created_on")
    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Enumerated(EnumType.STRING)
    public EntryStateType getState() {
        return state;
    }

    public void setState(EntryStateType state) {
        this.state = state;
    }
}
