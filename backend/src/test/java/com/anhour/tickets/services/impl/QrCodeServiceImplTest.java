package com.anhour.tickets.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.anhour.tickets.domain.entities.QRCodeStatusEnum;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.repositories.QrCodeRepository;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

class QrCodeServiceImplTest {

    @Test
    void generateQrCodeCreatesDecodableImageAndPersistsIt() throws Exception {
        QrCodeRepository repository = mock(QrCodeRepository.class);
        when(repository.save(any(QrCode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QrCodeServiceImpl service = new QrCodeServiceImpl(new QRCodeWriter(), repository);
        Ticket ticket = new Ticket();

        QrCode result = service.generateQrCode(ticket);

        byte[] png = Base64.getDecoder().decode(result.getValue());
        var image = ImageIO.read(new ByteArrayInputStream(png));
        var bitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        String decodedValue = new MultiFormatReader().decode(bitmap).getText();

        assertThat(decodedValue).isEqualTo(result.getId().toString());
        assertThat(result.getStatus()).isEqualTo(QRCodeStatusEnum.ACTIVE);
        assertThat(result.getTicket()).isSameAs(ticket);
    }
}
