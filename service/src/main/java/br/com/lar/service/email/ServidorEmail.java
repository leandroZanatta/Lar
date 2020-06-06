package br.com.lar.service.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.eclipse.persistence.internal.oxm.ByteArrayDataSource;

import com.google.gson.Gson;

import br.com.lar.repository.dao.ParametrosDAO;
import br.com.lar.repository.model.Parametros;
import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.ConfiguracaoEmailEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.AttachamentVO;
import br.com.sysdesc.util.vo.CIDVO;
import br.com.sysdesc.util.vo.ConfiguracaoEmailVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServidorEmail {

    private static ServidorEmail servidorEmail;

    private Session session;

    private ConfiguracaoEmailVO configuracaoEmailVO;

    private ServidorEmail(ConfiguracaoEmailVO configuracaoEmailVO) {
        this.configuracaoEmailVO = configuracaoEmailVO;

        createSession();
    }

    private void createSession() {

        this.session = Session.getInstance(getPropertiesFromParametros(configuracaoEmailVO), new UserAuthenticator(configuracaoEmailVO));

        this.session.setDebug(true);
    }

    private Properties getPropertiesFromParametros(ConfiguracaoEmailVO configuracaoEmailVO) {

        return ConfiguracaoEmailEnum.forCodigo(configuracaoEmailVO.getCodigoConfiguracao()).getConfiguracao();
    }

    public boolean sendMessage(String email, String assunto, String mensagem) {

        return sendMessage(email, assunto, mensagem, new ArrayList<>());
    }

    public boolean sendHtmlMessage(String email, String assunto, String mensagem, CIDVO... cids) {

        return sendHtmlMessage(email, assunto, mensagem, new ArrayList<>(), cids);
    }

    public boolean sendMessage(String email, String assunto, String mensagem, List<AttachamentVO> atachaments) {

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(configuracaoEmailVO.getUsuario()));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(assunto);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart contentBodyPart = new MimeBodyPart();

            contentBodyPart.setText(mensagem);

            multipart.addBodyPart(contentBodyPart);

            this.appendSources(atachaments, multipart);

            message.setContent(multipart);

            Transport.send(message);

            return true;

        } catch (MessagingException e) {

            log.error("erro enviando email para: {}", email, e);
        }

        return false;
    }

    public boolean sendHtmlMessage(String email, String assunto, String mensagem, List<AttachamentVO> atachaments, CIDVO... cids) {

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(configuracaoEmailVO.getUsuario()));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(assunto);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart contentBodyPart = new MimeBodyPart();

            contentBodyPart.setContent(mensagem, "text/html; charset=UTF-8");

            multipart.addBodyPart(contentBodyPart);

            this.appendMimes(multipart, cids);

            this.appendSources(atachaments, multipart);

            message.setContent(multipart);

            Transport.send(message);

            return true;

        } catch (MessagingException e) {

            log.error("erro enviando email para: {}", email, e);
        }

        return false;
    }

    private void appendMimes(Multipart multipart, CIDVO... cids) throws MessagingException {

        for (CIDVO cid : Arrays.asList(cids)) {

            MimeBodyPart cidBodyPart = new MimeBodyPart();

            cidBodyPart.setDataHandler(new DataHandler(new FileDataSource(cid.getArquivo())));
            cidBodyPart.setHeader("Content-ID", String.format("<%s>", cid.getCid()));

            multipart.addBodyPart(cidBodyPart);
        }
    }

    private void appendSources(List<AttachamentVO> atachaments, Multipart multipart) throws MessagingException {

        for (AttachamentVO attachament : atachaments) {

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachament.getFile(), attachament.getMime())));

            messageBodyPart.setFileName(attachament.getName());

            multipart.addBodyPart(messageBodyPart);
        }
    }

    public static ServidorEmail getInstance() {

        if (servidorEmail == null) {

            ConfiguracaoEmailVO configuracaoEmailVO = getConfiguracaoFromParametros();

            servidorEmail = new ServidorEmail(configuracaoEmailVO);
        }

        return servidorEmail;
    };

    public static ServidorEmail getTestInstance(ConfiguracaoEmailVO configuracaoEmailVO) {

        return new ServidorEmail(configuracaoEmailVO);
    };

    private static ConfiguracaoEmailVO getConfiguracaoFromParametros() {

        Parametros parametros = new ParametrosDAO().first();

        if (parametros == null || StringUtil.isNullOrEmpty(parametros.getConfigEmail())) {
            throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURAR_PARAMETROS);
        }

        return new Gson().fromJson(CryptoUtil.fromBlowfish(parametros.getConfigEmail()), ConfiguracaoEmailVO.class);
    }

}

class UserAuthenticator extends Authenticator {

    private final ConfiguracaoEmailVO configuracao;

    public UserAuthenticator(ConfiguracaoEmailVO configuracao) {
        this.configuracao = configuracao;
    }

    protected PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(this.configuracao.getUsuario(), this.configuracao.getSenha());
    }
}
