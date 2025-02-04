package com.psr.nosql.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopularVideoDto {
    private String videoId;
    private String title;
    private String channel;
    private String duration;
    private String url;
    private long views;
}
