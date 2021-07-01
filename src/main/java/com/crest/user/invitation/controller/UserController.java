package com.crest.user.invitation.controller;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crest.user.invitation.model.Invitation;
import com.crest.user.invitation.task.InvitationUpdateService;

@Controller
@RequestMapping("users")
public class UserController {

	@Autowired
	private InvitationUpdateService invitationUpdateService;

	@GetMapping(value = "/{userId}/invitations", produces = "application/json")
	public @ResponseBody Set<Invitation> getUserInvitations(@PathVariable String userId) {
		Set<Invitation> allInvitations = invitationUpdateService.getUserInvitations();
		return allInvitations.stream().filter(i -> StringUtils.equalsIgnoreCase(userId, i.getUserId()))
				.sorted(Comparator.comparingInt(Invitation::getInviteId)).collect(Collectors.toSet());
	}
}
