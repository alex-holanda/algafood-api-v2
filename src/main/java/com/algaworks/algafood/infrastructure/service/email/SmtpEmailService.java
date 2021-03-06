package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.dto.Mensagem;
import com.algaworks.algafood.domain.service.EnvioEmailService;

public class SmtpEmailService implements EnvioEmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailProperties emailProperties;

	@Autowired
	private ProcessadorEmailTemplate processarEmailTemplate;

	@Override
	public void enviar(Mensagem mensagem) {
		try {
			var mimeMessage = criarMimeMessage(mensagem);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar e-mail", e);
		}
	}

	protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
		var corpo = processarEmailTemplate.processarTemplate(mensagem);

		var mimeMessage = mailSender.createMimeMessage();

		var helper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
		helper.setFrom(emailProperties.getRemetente());
		helper.setTo(mensagem.getDestinatarios().toArray(new String[mensagem.getDestinatarios().size()]));
		helper.setSubject(mensagem.getAssunto());
		helper.setText(corpo, true);

//		helper.addAttachment("vendas-diarias.pdf", new ByteArrayResource(mensagem.getAttachment()));

		return mimeMessage;
	}


}