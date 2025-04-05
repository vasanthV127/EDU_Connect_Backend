package com.backendservice.EDU_Connect.payload.request;


import com.backendservice.EDU_Connect.model.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDTO {
    private Long id;
    private String name;
    private Integer semester;
    private List<Resource> resources;
}