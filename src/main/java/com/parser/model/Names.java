package com.parser.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Names extends ImdbComponents {
    @Parsed(field = "nconst")
    private String nameId;

    @Parsed(field = "primaryName")
    private String name;

}
