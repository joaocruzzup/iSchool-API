package com.catalisa.iSchool.model.dto;


import com.catalisa.iSchool.model.Aluno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaDTO {
    private LocalDate dataMatricula;
    private String aluno;
    private String curso;
}
