package com.example.test.dto.rim.main;

import lombok.Data;
import java.util.List;

@Data
public class CommonSectionDTO {
    private List<MenuIconDTO> menuIcons;
    
    @Data
    public static class MenuIconDTO {
        private String id;
        private String name;
        private String icon;
        private String link;
        private boolean requireLogin;
    }
} 