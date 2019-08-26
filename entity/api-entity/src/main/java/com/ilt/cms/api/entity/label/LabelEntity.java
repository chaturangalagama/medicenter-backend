package com.ilt.cms.api.entity.label;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LabelEntity {
    private String id;
    private String name;
    private String template;
}
