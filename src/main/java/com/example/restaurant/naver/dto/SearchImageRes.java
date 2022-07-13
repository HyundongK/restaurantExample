package com.example.restaurant.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchImageRes {

    private String lastBuilDate;
    private int total;
    private int start;
    private int display;
    private List<SearchLocalItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchLocalItem{
        private String title;
        private String link;
        private String thumbnail;
        private String sizeheight;
        private String sizewidth;
    }
}
