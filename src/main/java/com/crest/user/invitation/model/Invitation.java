package com.crest.user.invitation.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Invitation {
	
	@JsonProperty("invite_id")
	private Integer inviteId;
	
	@JsonProperty("sender_id")
	private String senderId;
	
	@JsonProperty("sig_id")
	private String sigId;
	
	private String invite;
	
	private String vector;
	
	@JsonProperty("invite_time")
	private Long inviteTime;
	
	@JsonProperty("user_id")
	private String userId;
	
	private String status;
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;

		Invitation invitation = (Invitation) obj;
		return (StringUtils.equals(invitation.getUserId(), this.getUserId())
				&& invitation.getInviteId().intValue() == this.getInviteId().intValue());
	}
}
