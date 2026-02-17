package com.aviation.flight_app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "pilots")
public class Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пилота не может быть пустым")
    @Column(name = "pilot_name", nullable = false)
    private String name;  

    @Min(value = 0, message = "Опыт не может быть отрицательным") 
    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    // СВЯЗЬ: Один пилот — много рейсов
    // mappedBy = "pilot" означает, что в классе Flight должно быть поле "pilot"
    @OneToMany(mappedBy = "pilot") 
    @JsonIgnore // Чтобы не было бесконечного цикла в JSON
    private List<Flight> flights;

    public Pilot() {} // Пустой конструктор для Hibernate

    public Pilot(String name, int experienceYears) {
        this.name = name;
        this.experienceYears = experienceYears;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }

    public void setName(String name) { this.name = name; }
    public void setExperienceYears(int experience) { this.experienceYears = experience; }
}