package borislavk.gestoreviaggio.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dipendente {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String username;

    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;

    @Email
    private String email;

    private String imgUrl;

    private String password;

    public Dipendente(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.username = nome.toLowerCase() + "." + cognome.toLowerCase();
    }
}
