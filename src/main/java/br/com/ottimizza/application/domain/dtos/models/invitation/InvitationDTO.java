package br.com.ottimizza.application.domain.dtos.models.invitation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InvitationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private BigInteger id;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private InvitationUserDetailsDTO userDetails;

    @Getter
    @Setter
    private OrganizationDTO organization;

    @Getter
    @Setter
    private Integer type;

    @Getter
    @Setter
    private List<String> authorities;

    @Getter
    @Setter
    private List<String> products;

}
