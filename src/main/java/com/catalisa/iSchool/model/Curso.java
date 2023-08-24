package com.catalisa.iSchool.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
//    @Enumerated(EnumType.STRING)
    private String nome;

    @Column(nullable = false)
    private Integer cargaHoraria;

    @ManyToMany(mappedBy = "cursos")
    private List<Professor> professores;



}
