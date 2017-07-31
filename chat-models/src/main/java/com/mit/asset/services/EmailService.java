package com.mit.asset.services;

import java.net.URL;
import java.util.Arrays;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mit.feedback.bodies.FeedbackBody;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;

@Service
public class EmailService {
	private final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	@Value("${email.smtp.host:smtp.gmail.com}")
	private String smtpHost;
	@Value("${email.smtp.port:587}")
	private int smtpPort;
	@Value("${email.username:donotreply@homeberapp.com}")
	private String emailUserName;
	@Value("${email.password:Thangloi8$}")
	private String emailPassword;

	@Value("${email2.smtp.host:smtp.office365.com}")
	private String smtpHost2;
	@Value("${email2.smtp.port:587}")
	private int smtpPort2;
	@Value("${email2.username:donotreply@homeberapp.com}")
	private String emailUserName2;
	@Value("${email2.password:Thangloi8$}")
	private String emailPassword2;
	
	@Value("${email.cc:lappham93@gmail.com}")
	private String emailCCs;
	
	@Value("${email.booking.bill.name:Saocoo - Payment}")
	private String bookingBillEmailName;
	@Value("${email.booking.bill.subject:Saocoo - Payment Successful}")
	private String bookingBillEmailSubject;
	
	@Value("${email.feedback.subject:Saocoo - Message from {}}")
	private String feedbackEmailSubject;
	@Value("${email.feedback.name:Saocoo - Contact")
	private String feedbackEmailName;
	@Value("${email.contact:emailTos:lappham93@gmail.com}")
	private String contactEmailTos;
	@Value("${email.contact:Name: {}<br/>Email: {}<br/>Phone: {}<br/>Message: {}}")
	private String contactEmailMessage;
	
	@Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
	@Autowired
	private ProfileRepo profileRepo;

//	public void sendBookingBillEmail(long landscapingId) {
//		logger.info("Send bill email landscapingId=" + landscapingId);
//		BookingBillBody body = new BookingBillBody();
//		body.setLandscapingId(landscapingId);
//		rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.BOOKING_BILL_EMAIL, body);
//	}
	public void sendFeedback(FeedbackBody body) {
		rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.FEEDBACK_EMAIL, body);
	}
	
	public void sendFeedbackContinue(long userId, String message) {
		Profile profile = profileRepo.getById(userId);
		String subject = MessageFormatter.arrayFormat(feedbackEmailSubject, new Object[] {profile.getFullName()}).getMessage();
		message = message.replace("\n", "<br/>");
		String contactMessage = MessageFormatter.arrayFormat(contactEmailMessage, new Object[] {profile.getFullName(), profile.getEmail(), profile.getPhone(), message}).getMessage();
		sendEmail(contactEmailTos, feedbackEmailName, emailCCs, subject, contactMessage, "");
		logger.info("Sending email contact from " + profile.getEmail());
	}
	
	public void sendBookingBillEmailContinue(String emailTos, String message, String attachLink) {
		if (!StringUtils.isEmpty(message)) {
			sendEmail(emailTos, bookingBillEmailName, emailCCs, bookingBillEmailSubject, message, attachLink);
		}
	}
	
	private boolean sendEmail(String emailTos, String emailName, String emailCCs, String subject, String message, String attachLink) {
		boolean success = sendEmail(emailTos, emailName, emailCCs, subject, message, attachLink, smtpHost, 
				smtpPort, emailUserName, emailPassword);
		if (!success) {
			success = sendEmail(emailTos, emailName, emailCCs, subject, message, attachLink, smtpHost2, 
					smtpPort2, emailUserName2, emailPassword2);
		}
		return success;
	}
	
	private boolean sendEmail(String emailTos, String emailName, String emailCCs, String subject, String message, String attachLink,
			String smtpHost, int smtpPort, String emailUserName, String emailPassword) {
		try {
			HtmlEmail sender = new HtmlEmail();
			sender.setHostName(smtpHost);
			sender.setSmtpPort(smtpPort);
			sender.setAuthenticator(new DefaultAuthenticator(emailUserName, emailPassword));
			sender.setStartTLSRequired(true);
			sender.setStartTLSEnabled(true);
			sender.setFrom(emailUserName, emailName);
			sender.setSubject(subject);
			sender.setHtmlMsg(message);
			try {
				EmailAttachment attach = new EmailAttachment();
				attach.setURL(new URL(attachLink));
				sender.attach(attach);
			} catch (Exception e) {
				logger.info("attach link error :" + attachLink);
			}
			for (String emailTo : emailTos.split(",")) {
				try {
					sender.addTo(emailTo);
				} catch (Exception e) {

				}
			}

			String[] emailCCList = emailCCs.split(",");
			if (sender.getToAddresses().isEmpty() && emailCCList.length > 0) {
				sender.addTo(emailCCList[0]);
			}

			if (!emailCCs.isEmpty()) {
				for (String emailCC : emailCCList) {
					sender.addBcc(emailCC);
				}
			}

			for (int i = 0; i < 3; i++) {
				try {
					String rs = sender.send();
					logger.info(emailTos + "\t" + rs);
					return true;
				} catch (EmailException e) {
					logger.info("Send mail error " + emailTos, e);
				}
			}
		} catch (Exception e) {
			logger.info("Send mail error ", e);
		}

		return false;
	}
	
}