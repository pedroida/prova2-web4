package br.edu.utfpr.hello.model.User;

import br.edu.utfpr.hello.structures.EntityApplication;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Scope("session")
public class User extends EntityApplication {

    @NotEmpty(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Email é obrigatório")
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "Senha obrigatória")
    @Column(nullable = false)
    @JsonIgnore
    private String password;
}
