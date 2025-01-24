package uz.gayratjon.minimarketcrm.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private boolean isPublic;
}

