package com.app.security.DTO;

import com.app.security.enumerated.PermissionList;
import lombok.Data;

@Data
public class PermissionDTO {

  private Long id;

  private PermissionList name;

}
