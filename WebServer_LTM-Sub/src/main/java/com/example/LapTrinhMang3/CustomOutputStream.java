package com.example.LapTrinhMang3;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Chuyển byte đến JTextArea dưới dạng chuỗi
        textArea.append(String.valueOf((char)b));
        // Tự động cuộn xuống dòng cuối cùng khi có thêm dữ liệu
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
