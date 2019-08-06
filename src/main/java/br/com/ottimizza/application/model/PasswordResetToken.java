package br.com.ottimizza.application.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.com.ottimizza.application.model.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PasswordResetToken implements Serializable {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @Getter
    @Setter
    @NonNull
    @Column(name = "token")
    private String token;

    @Getter
    @Setter
    @NonNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_users_id", nullable = false)
    private User user;

    @Getter
    @Setter
    @Column(name = "expiry_date")
    private Date expiryDate;

    @PrePersist
    public void setExpiryDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 60);
        this.expiryDate = c.getTime();
    }

}