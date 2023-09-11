package ru.smarts.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Builder
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+", message = "Может содержать только буквы")
    private String fio;
    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Номер телефона должен содержать 10 цифр")
    private String phone;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{6})", message = "Номер паспорта должен содержать 6 цифр")
    private String passport;
}
