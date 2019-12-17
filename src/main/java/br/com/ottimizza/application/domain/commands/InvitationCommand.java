package br.com.ottimizza.application.domain.commands;

import java.io.Serializable;
import java.util.List;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private Integer type;

    private List<OrganizationDTO> organizations;

}