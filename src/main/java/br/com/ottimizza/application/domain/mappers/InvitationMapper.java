package br.com.ottimizza.application.domain.mappers;

import br.com.ottimizza.application.domain.dtos.models.invitation.InvitationDTO;
import br.com.ottimizza.application.domain.dtos.models.invitation.InvitationUserDetailsDTO;
import br.com.ottimizza.application.model.invitation.InvitationUserDetails;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;

/**
 * @formatter:off
 * 
 * Classe responsável pelo mapeamento de Entity para DTO e vice-versa. 
 * Normalmente utilizada em Services para converter um DTO recebido pelo 
 * Controller em uma Entity, e para converter uma Entity em DTO novamente 
 * após todas as regras de negócio. Desta forma o cliente nunca tem acesso 
 * direto a camada de banco de dados.
 */
public class InvitationMapper { // @formatter:off

    /**
     * Método responsável pela conversão de uma Entity para DTO.
     * 
     */
    public static InvitationDTO fromEntity(UserOrganizationInvite invitation) { 
        return InvitationDTO.builder()
                .id(invitation.getId())
                .token(invitation.getToken())
                .email(invitation.getEmail())
                .userDetails(fromEntity(invitation.getUserDetails()))
                .organization(OrganizationMapper.fromEntity(invitation.getOrganization()))
                .build();
    }

    /**
     * Método responsável pela conversão de um DTO para Entity.
     * 
     */
    public static UserOrganizationInvite fromDTO(InvitationDTO dto) {
        return UserOrganizationInvite.builder()
                .id(dto.getId())
                .token(dto.getToken())
                .email(dto.getEmail())
                .userDetails(fromDTO(dto.getUserDetails()))
                .organization(OrganizationMapper.fromDTO(dto.getOrganization()))
                .build();
    }

    /**
     * Método responsável pela conversão de um DTO para Entity.
     * 
     */    
    public static InvitationUserDetailsDTO fromEntity(InvitationUserDetails userDetails) {
        return InvitationUserDetailsDTO.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .phone(userDetails.getPhone())
                .build();
    }


    public static InvitationUserDetails fromDTO(InvitationUserDetailsDTO dto) {
        return InvitationUserDetails.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .build();
    }


}
