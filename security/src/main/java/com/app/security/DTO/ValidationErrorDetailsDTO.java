package com.app.security.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDetailsDTO extends ErrorDetailsDTO {

    List<ValidationDetailDTO> validationDetailDTOList;
}
