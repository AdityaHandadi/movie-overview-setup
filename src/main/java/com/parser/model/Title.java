package com.parser.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Title extends ImdbComponents {
    @Parsed(field = "tconst")
    private String titleId;

    @Parsed(field = "titleType")
    private String titleType;

    @Parsed(field = "originalTitle")
    private String titleName;

    @Parsed(field = "startYear")
    private String year;
}
