package borislavk.gestoreviaggio.controllers;

import borislavk.gestoreviaggio.entities.Dipendente;
import borislavk.gestoreviaggio.exeptions.ValidationException;
import borislavk.gestoreviaggio.payloads.LoginDTO;
import borislavk.gestoreviaggio.payloads.LoginRespDTO;
import borislavk.gestoreviaggio.payloads.NewDipendenteDTO;
import borislavk.gestoreviaggio.payloads.NewDipendenteRespDTO;
import borislavk.gestoreviaggio.services.AuthService;
import borislavk.gestoreviaggio.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendenteService dipendenteService;

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        String accessToken = authService.checkCredentialsAndGenerateToken(body);
        return new LoginRespDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewDipendenteRespDTO save(@RequestBody @Validated NewDipendenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        Dipendente newDipendente = this.dipendenteService.save(payload);
        return new NewDipendenteRespDTO(newDipendente.getId());
    }

}
