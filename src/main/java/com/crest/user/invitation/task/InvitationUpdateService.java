package com.crest.user.invitation.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crest.user.invitation.model.Invitation;
import com.crest.user.invitation.model.UserInvitations;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvitationUpdateService {
	
	@Value("${upload.path}")
    private String uploadPath;
	
	private Set<Invitation> userInvitations;
	
	@PostConstruct
    public void init() {
        try {
        	log.info("Upload path is created : {}", uploadPath);
        	log.info("Upload valid json files to update invitations. If JSON Files having error in format, they will ignored");
        	userInvitations = new HashSet<>();
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder ", e);
        }
    }
	
	@Scheduled(fixedRate = 5000)
	public void scheduleTaskWithFixedRate() {
		try {
			log.info("Updating invitations - {}", new Date());
			File uploadDirectory = new File(uploadPath);

			if (!FileUtils.isEmptyDirectory(uploadDirectory)) {
				Collection<File> invitationFiles = FileUtils.listFiles(uploadDirectory, new String[] {"json"}, false);
				
				if(invitationFiles.isEmpty()) {
					log.info("Files found in directory but they are not in JSON formatted file");
					FileUtils.cleanDirectory(uploadDirectory);
					return;
				}
				
				for(File invitationFile : invitationFiles) {
					updateInvitations(invitationFile);
				}
				
			} else {
				log.info("No new files detected in directory");
			}
		} catch (Exception e) {
			log.error("Error in reading invitation files : ", e);
		}
	}
	
	private void updateInvitations(File invitationFile) {
		try {
			String invitations = FileUtils.readFileToString(invitationFile, Charset.defaultCharset());
			ObjectMapper objectMapper = new ObjectMapper();
			UserInvitations invitationUpdates = objectMapper.readValue(invitations, new TypeReference<UserInvitations>() {});
			userInvitations.addAll(invitationUpdates.getInvites());
			log.info("Invite Updated successfully");
			log.info("Invitation size after processing another file : {}", userInvitations.size());
			
		} catch(Exception e) {
			log.error("error in processing invitation file : {}", invitationFile.getName(), e);
		}
		deleteFile(invitationFile);
	}
	
	private void deleteFile(File invitationFile) {
		try {
			FileUtils.delete(invitationFile);
		} catch (Exception e) {
			log.error("error in deleting invitation file : {}", invitationFile.getName(), e);
		}
	}
	
	public Set<Invitation> getUserInvitations() {
		return userInvitations;
	}
}
