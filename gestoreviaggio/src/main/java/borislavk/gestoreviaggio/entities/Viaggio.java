package borislavk.gestoreviaggio.entities;

import borislavk.gestoreviaggio.enums.StatoViggio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Viaggio {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String destinazione;

    @NotNull
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatoViggio stato = StatoViggio.IN_PROGRAMMA;
}
