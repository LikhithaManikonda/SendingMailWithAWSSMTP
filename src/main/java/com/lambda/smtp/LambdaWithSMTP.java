package com.lambda.smtp;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class LambdaWithSMTP
        implements RequestHandler<LambdaWithSMTP.Request, LambdaWithSMTP.Response>{

    static class Request {
        String name;
        String emal;
        Long phoneNumber;
        String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmal() {
            return emal;
        }

        public void setEmal(String emal) {
            this.emal = emal;
        }

        public Long getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(Long phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    static class Response {
        String successMsg;
        String failedMsg;

        public String getSuccessMsg() {
            return successMsg;
        }

        public void setSuccessMsg(String successMsg) {
            this.successMsg = successMsg;
        }

        public String getFailedMsg() {
            return failedMsg;
        }

        public void setFailedMsg(String failedMsg) {
            this.failedMsg = failedMsg;
        }
    }

    Response response = new Response();

    public Response handleRequest(Request request, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log("entering....");
            try {
                logger.log("Sending mail to sendingMailToBusinessOwner method");
                response   = sendingMailToBusinessOwner(logger);
            }
            catch (Exception e) {
                System.out.println("There is an exception"+e);
            }
        if(response!=null)
        logger.log("SUCCESS"+response.getSuccessMsg());
        logger.log("FAILED"+response.getFailedMsg());
        return response;

    }

    static final String FROM = "tester.mailer-rx@codr.co.in";
    static final String TO ="test.mailer@codr.co.in";
    // static final String TO = "likhithamanikonda6@gmail.com";

    static final String FROMNAME = "Likhitha Manikonda";

    static final String SMTP_USERNAME = "AKIAQY2GA5FSGZIHJ2OT";
    static final String SMTP_PASSWORD = "BB3VbbR4OHAI1JprVW/EKlTR974n3ERe+tClriuAAGv+";

    //static final String CONFIGSET = "ConfigSet";
    static final String HOST = "email-smtp.ap-south-1.amazonaws.com";

    // The port you will connect to on the Amazon SES SMTP End point.
    static final int PORT = 587;

    static final String SUBJECT = "Test Email(SMTP interface accessed using Java)";



    public  Response sendingMailToBusinessOwner(LambdaLogger logger) throws Exception
    {
        final String BODY = String.join(
                System.getProperty("line.separator"),
                "<h1>Test Amazon SES SMTP Email Test</h1>",
                "<p>This email was sent for Practice using Amazon SES");

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug","true");

        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/html");

        Transport transport = session.getTransport();


        try
        {
            logger.log("Sending...");
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.log("Email sent!");
            response.setSuccessMsg("Email sent successfully");
            return response;

        }
        catch (Exception ex) {
            logger.log("The email was not sent.");

            logger.log("Error message: " + ex.getMessage());
            response.setFailedMsg("Email failed to sent");
            return response;
        }
        finally
        {
            transport.close();
        }
    }
}

