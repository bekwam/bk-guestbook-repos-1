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
package us.bekwam.guestbook.commons.messages;

import java.io.Serializable;

/**
 * @author carl
 */
public class ProfanityFilterRequest implements Serializable {

    private static final long serialVersionUID = -4927257482235827179L;

    private Long id;
    private String text; // max 1024
    private ProfanityFilterRequestModeType mode = ProfanityFilterRequestModeType.TEST;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ProfanityFilterRequestModeType getMode() {
        return mode;
    }

    public void setMode(ProfanityFilterRequestModeType mode) {
        this.mode = mode;
    }
}
