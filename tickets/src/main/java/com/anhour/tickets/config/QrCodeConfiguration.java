package com.anhour.tickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.zxing.qrcode.QRCodeWriter;

@Configuration
public class QrCodeConfiguration {

    @Bean
    QRCodeWriter qrCodeWriter() {
        return new QRCodeWriter();
    }
}
