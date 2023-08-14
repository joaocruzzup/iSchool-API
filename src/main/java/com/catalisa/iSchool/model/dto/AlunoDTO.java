package com.catalisa.iSchool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {

    private String nome;

    private Integer idade;

    private String email;

}
