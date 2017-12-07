package com.parser.model;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;

@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TitleNamesMappings extends ImdbComponents {

    @Parsed(field = "tconst")
    private String titleId;

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    //@Convert(conversionClass = WordsToSetConversion.class, args = { ",", "false" })
    @Parsed(field = "principalCast")
    private String casts;

}
