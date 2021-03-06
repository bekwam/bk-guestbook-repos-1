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
public class ProfanityFilterResponse implements Serializable {

    private static final long serialVersionUID = 6477160749895916036L;

    private Long id;
    private ProfanityFilterResponseType response;
    private String explanation = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfanityFilterResponseType getResponse() {
        return response;
    }

    public void setResponse(ProfanityFilterResponseType response) {
        this.response = response;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
