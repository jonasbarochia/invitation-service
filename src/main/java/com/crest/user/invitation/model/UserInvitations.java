package com.crest.user.invitation.model;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInvitations {

	private Set<Invitation> invites;
}
