package com.example.prog_gps;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendmailGmail {
	 public static void sendmail(String fromaddress, String[] toaddress,String pass, String mailtext) throws IOException{
		 //メール送信用パラメタの設定
		 Properties props = new Properties();
		 props.put("mail.smtp.host", "smtp.gmail.com"); // SMTPサーバ名
		 props.put("mail.host", "smtp.gmail.com");      // 接続するホスト名
		 props.put("mail.smtp.port", "587");       // SMTPサーバポート
		 props.put("mail.smtp.auth", "true");    // smtp auth
		 props.put("mail.smtp.starttls.enable", "true");	// STTLS

		 // セッション
		 Session session = Session.getDefaultInstance(props);
		 session.setDebug(true);

		 MimeMessage msg = new MimeMessage(session);
		 //toaddressに含まれるアドレスの数だけメールを作成・送信
		 for(int i=0; i<toaddress.length; i++){
			 try {
				 msg.setSubject("お知らせ！", "utf-8");
				 msg.setFrom(new InternetAddress(fromaddress));
				 msg.setSender(new InternetAddress(fromaddress));
				 msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toaddress[i]));
				 msg.setText(mailtext,"utf-8");

				 Transport t = session.getTransport("smtp");
				 t.connect(fromaddress, pass);
				 t.sendMessage(msg, msg.getAllRecipients());
			 } catch (MessagingException e) {
				 e.printStackTrace();
			 }
		 }
	 }
}
