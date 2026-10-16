
package org.iesalixar.daw2.mariojurado.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario de la tabla users.
 *
 * @author Mario Jurado
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private Boolean active;
    private Boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiresAt;
    private Integer failedLoginAttempts;
    private Boolean emailVerified;
    private Boolean mustChangePassword;
}