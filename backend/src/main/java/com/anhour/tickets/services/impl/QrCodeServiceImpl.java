package com.anhour.tickets.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.anhour.tickets.domain.entities.QRCodeStatusEnum;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.exceptions.QrCodeGenerationException;
import com.anhour.tickets.repositories.QrCodeRepository;
import com.anhour.tickets.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QrCodeServiceImpl implements QrCodeService{
    
    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQrCode(Ticket ticket){
        UUID uniqueId = UUID.randomUUID();
        String qrCodeImage = generateQrCodeImage(uniqueId);

        QrCode qrCode = QrCode.builder()
                .id(uniqueId)
                .status(QRCodeStatusEnum.ACTIVE)
                .value(qrCodeImage)
                .ticket(ticket)
                .build();
        return qrCodeRepository.save(qrCode);
    }

    private String generateQrCodeImage(UUID uniqueId){
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var bitMatrix = qrCodeWriter.encode(
                    uniqueId.toString(), BarcodeFormat.QR_CODE, 300, 300);
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "PNG", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        } catch (WriterException | IOException exception) {
            throw new QrCodeGenerationException("Failed to generate QR code", exception);
        }
    }
}
