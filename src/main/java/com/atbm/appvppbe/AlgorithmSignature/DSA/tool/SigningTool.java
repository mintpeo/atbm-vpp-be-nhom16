package com.atbm.appvppbe.AlgorithmSignature.DSA.tool;
import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class SigningTool extends JFrame {

    private JTextArea txtHash;
    private JTextArea txtSignature;
    private JTextField txtKeyPath;
    private File privateKeyFile;
    private String absoluteFilePath;

    public SigningTool() {
        setTitle("Tool Ký Đơn Hàng Bảo Mật");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel nhập liệu chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Phần nhập Mã Băm
        mainPanel.add(new JLabel("1. Nhập mã Order:"));
        txtHash = new JTextArea(4, 50);
        txtHash.setLineWrap(true);
        mainPanel.add(new JScrollPane(txtHash));
        mainPanel.add(Box.createVerticalStrut(10));

        // 2. Phần chọn file Private Key
        mainPanel.add(new JLabel("2. Chọn file Private Key:"));
        JPanel keyPanel = new JPanel(new BorderLayout(5, 5));
        txtKeyPath = new JTextField();
        txtKeyPath.setEditable(false);
        JButton btnBrowse = new JButton("Chọn File...");
        keyPanel.add(txtKeyPath, BorderLayout.CENTER);
        keyPanel.add(btnBrowse, BorderLayout.EAST);
        mainPanel.add(keyPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // 3. Nút bấm Ký đơn hàng
        JButton btnSign = new JButton("TẠO CHỮ KÝ ĐIỆN TỬ");
        btnSign.setFont(new Font("Arial", Font.BOLD, 14));
        btnSign.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnSign);
        mainPanel.add(Box.createVerticalStrut(15));

        // 4. Phần hiển thị Chữ ký kết quả
        mainPanel.add(new JLabel("3. Chữ ký:"));
        txtSignature = new JTextArea(5, 50);
        txtSignature.setLineWrap(true);
        txtSignature.setEditable(false);
        mainPanel.add(new JScrollPane(txtSignature));

        add(mainPanel, BorderLayout.CENTER);

        // Choose File Private Key
        btnBrowse.addActionListener((ActionEvent e) -> {
            choosePrivateKeyFile();
        });

        // BTN signature
        btnSign.addActionListener((ActionEvent e) -> {
            generateSignatureAction();
        });
    }

    // File
    private void choosePrivateKeyFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            privateKeyFile = fileChooser.getSelectedFile();
            // String Path
            absoluteFilePath = privateKeyFile.getAbsolutePath();
            // Hien thi text
            txtKeyPath.setText(absoluteFilePath);

            System.out.println("Đường dẫn file đã chọn: " + absoluteFilePath);
        }
    }

    // Check
    private void generateSignatureAction() {
        String hashText = txtHash.getText().trim();
        if (hashText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã Order!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (privateKeyFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file Private Key!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Đọc và tạo chữ ký
            String signature = signData(hashText);
            txtSignature.setText(signature);
            JOptionPane.showMessageDialog(this, "Ký đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi ký: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Signature
    private String signData(String data) {
        String signature;
        try {
            FileInputStream keyFis = new FileInputStream(absoluteFilePath);
            byte[] rawBytes = keyFis.readAllBytes();
            keyFis.close();

            String keyText = new String(rawBytes, "UTF-8").trim();
            byte[] encKey = Base64.getDecoder().decode(keyText);

            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);

            // Handle Signature
            DSA dsa = new DSA();
            signature = dsa.sign(data, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return signature;
    }

    public static void main(String[] args) {
        // Chạy ứng dụng trên giao diện chuẩn của hệ điều hành
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SigningTool().setVisible(true);
        });
    }
}
