package com.Bookstore.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.Bookstore.model.Order;
import com.Bookstore.model.User;

@Component
public class MailConstructor
{

	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;
   // this methode to send default password on his email.
	public SimpleMailMessage constructorResetTokenEmail(String contextPath, Locale local, String token, User user, String password)
	{
		String url = contextPath + "/editYourProfile?token=" + token;

		String message = "\n please click on this link to verify your mail and edit personal information. Your password is:\n" + password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("NROCK Bookstore - New User");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
	
	public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale locale)
	{
		Context context = new Context();
		
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception
			{
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation -" + order.getId());
				email.setText(text,true);
				email.setFrom(new InternetAddress("knitish5421@gmail.com"));
			}
			
			
		};
		
		return messagePreparator;
	}

}