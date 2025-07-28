package borislavk.gestoreviaggio.services;

import borislavk.gestoreviaggio.entities.Dipendente;
import borislavk.gestoreviaggio.exeptions.UnauthorizedException;
import borislavk.gestoreviaggio.payloads.LoginDTO;
import borislavk.gestoreviaggio.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private JWTTools jwtTools;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Dipendente found = this.dipendenteService.findByEmail(body.email());
        if (found.getPassword().equals(body.password())) {
            // TODO: Migliorare gestione password

            String accessToken = jwtTools.createToken(found);

            return accessToken;
        } else {

            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
