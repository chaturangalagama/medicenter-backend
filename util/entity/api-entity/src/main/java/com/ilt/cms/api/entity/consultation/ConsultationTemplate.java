package com.ilt.cms.api.entity.consultation;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsultationTemplate {
    private String id;
    private String name;
    private transient String type;
    private String template;
}
